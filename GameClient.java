
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class GameClient implements Runnable{
	
//	CHAT FIELDS
	private Socket socket = null;
	private Thread thread = null;
	private DataInputStream console = null;
	private DataOutputStream streamOut = null;
	private ChatClientThread client;
	
//	GAME FIELDS
	private DatagramSocket datagramSocket = new DatagramSocket();
	private String serverData = null;
	private boolean gameConnected = false;
	private DaddyGUI gui;
	private String wordToGuess="";
	private String playerName,serverName;
	private int portNumber;
	private boolean enableWordHints = true;
	private boolean isArtist = false;
	private boolean enableDrawingArea = true;
	private int dataCounter = 0;
	private int timerCounter = 0;
	private String currentArtist = "";
	public GameClient(String serverName, int portNumber, String playerName) throws IOException{
		this.playerName = playerName;
		this.serverName = serverName;
		this.portNumber = portNumber;
		connectChat();
		connectDrawing();
		start();
	}
	
	private void connectChat(){
		System.out.println("Establishing connection!");
		try{
			socket = new Socket(serverName, portNumber);
			System.out.println("Connected:"+socket);
//			start();
		}catch(UnknownHostException uhe){
			System.out.println("Host unknown: "+uhe.getMessage());
		}catch(IOException ioe){
			System.out.println("Unexpected exception: "+ioe.getMessage());
		}
		
	}
	
	private void connectDrawing(){
		try{
		// datagramSocket = new DatagramSocket(portNumber);
		datagramSocket.setSoTimeout(100);
			
	}catch(SocketException ioe){

	}
		
	}
	
	private void start() throws IOException {
		// TODO Auto-generated method stub
		// console = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
		if(thread == null){
			client = new ChatClientThread(this,socket);
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run(){
		gui = new DaddyGUI(this,playerName);
		gui.render();

		while(true){
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			try{
				datagramSocket.receive(packet);
			}catch(Exception e){
				
			}
			
			serverData = new String(buf);
			serverData = serverData.trim();
			if(!gameConnected && serverData.startsWith("CONNECTED")){
				gameConnected = true;
				System.out.println("Connected!!");
			}else if(!gameConnected){
				sendGameData("CONNECT "+playerName);
			}else if(gameConnected){
				if(serverData.startsWith("divideTime")){
					this.gui.getTimer().divide();
					timerCounter = 0;
				}else if(serverData.startsWith("clearDrawingArea")){
					gui.getDrawingArea().clear();
					gui.getDrawingArea().repaint();
				}else if(serverData.startsWith("COORDINATE")){
					translateCoordinateData(serverData);
				}else if(serverData.startsWith("COORDINATEB")){
					translateCoordinateData(serverData);
				}else if(serverData.startsWith("ARTIST")){
					String[] artistInfo = serverData.split(" ");
					String artist = artistInfo[1];
					currentArtist = artist;
					if(artist.equals(playerName)){
						isArtist = true;
						enableDrawingArea();
					}else{
						isArtist = false;
						disableDrawingArea();
					}
				}else if(serverData.startsWith("SCORELIST")){
					translateScoreData(serverData);
				}else if(serverData.startsWith("rewardArtist")){
					sendGameData("addScore "+currentArtist+" "+10);
					this.gui.getTimer().cancel();
				}else if(serverData.startsWith("WORDTOGUESS")){
					String[] wordInfo = serverData.split(" ");
					this.wordToGuess = wordInfo[1];
					enableWordHints = true;
					if(isArtist){
						this.gui.getWordToGuessField().setText(wordToGuess);
					}else{
						displayWordAsUnderscores();
					}
					this.gui.initializeTimer();
					this.gui.startTimer(80);
					dataCounter = 0;
				}else if(this.gui.getTimer().getRemainingTime() == 0 && dataCounter == 0){
					ensureBroadcastOnce("nextArtistPlease");
				} else if(serverData.startsWith("FINALSCORELIST")){
					translateFinalScoreData(serverData);
				}else if(wordToGuess.length()!=0 && this.gui.getTimer().getRemainingTime()%wordToGuess.length() == 0 && timerCounter == 0){
					timerCounter++;
					showLetter();
				}
			}
		}
		
	}

	private void showLetter(){
		// wordToGuess
		Random randomizer = new Random();
		
		int index = randomizer.nextInt(wordToGuess.length());
		String toShow="";
		for(int i = 1; i < wordToGuess.length();i++){
			if(i-1 == index){
				toShow+=wordToGuess.charAt(index)+" ";
			}
			toShow+="- ";
		}
		if(!isArtist){
			this.gui.getWordToGuessField().setText(toShow);
		}
		// timerCounter = 0;
	}

	private void ensureBroadcastOnce(String message){
		dataCounter++;
		if(dataCounter == 1){
			sendGameData(message);
		}
	}

	private void resetFields(){
		enableWordHints = true;
		isArtist = false;
	}
	
	public void handle(String message) {
		gui.getChatArea().append(message+"\n");
	}
	

	public void sendChat() {
		// TODO Auto-generated method stub
		String message = gui.getInputField().getText();
		boolean allowSending = true;
		allowSending = checkDataToSend(message);
		
		if(allowSending){
			try {
				streamOut.writeUTF(playerName+":"+message);
				streamOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkDataToSend(String message){
		String formalMessage = message.toUpperCase();
		String formalWordToGuess = "";
		if(wordToGuess.equals("")){
			return true;
		}else{
			formalWordToGuess = wordToGuess.toUpperCase();
		}

		if(enableWordHints && !isArtist){
			if(formalMessage.equals(formalWordToGuess)){
				message = null;
				System.out.println("YOU GOT THE WORD! "+wordToGuess);
				enableWordHints = false;
				sendGameData("addScore "+playerName+" "+this.gui.getTimer().getRemainingTime());
				sendGameData("divideTime");
				return false;
			}
		}else{
			if(formalMessage.indexOf(formalWordToGuess) != -1){
				return false;
			}
		}
		return true;
	}

	public void enableDrawingArea(){
		this.gui.getDrawingArea().setEnableDrawingArea();
	}

	public void disableDrawingArea(){
		this.gui.getDrawingArea().setDisableDrawingArea();
	}
	

	private void translateCoordinateData(String serverData){
			String[] coordinateInfo = serverData.split(" ");
			int x = Integer.parseInt(coordinateInfo[1]);
			int y = Integer.parseInt(coordinateInfo[2]);
			int newX = Integer.parseInt(coordinateInfo[3]);
			int newY = Integer.parseInt(coordinateInfo[4]);
			float brushSize = Float.parseFloat(coordinateInfo[5]);
			//Color color = Color.BLACK;
			Color color = null;
			String str_color = coordinateInfo[6].trim();
			//System.out.println("Color: " + str_color);
			
			//System.out.println("String color:" + str_color);
			if(str_color.equals("java.awt.Color[r=255,g=0,b=0]")){
				color = Color.red;
			}else if (str_color.equals("java.awt.Color[r=0,g=0,b=255]")){
				color = Color.blue;
			}else if (str_color.equals("java.awt.Color[r=255,g=255,b=0]")){
				color = Color.yellow;
			}else if (str_color.equals("java.awt.Color[r=0,g=255,b=0]")){
				color = Color.green;
			}else if (str_color.equals("java.awt.Color[r=255,g=0,b=255]")){
				color = Color.magenta;
			}else if (str_color.equals("java.awt.Color[r=255,g=255,b=255]")){
				color = Color.white;
			}else if (str_color.equals("java.awt.Color[r=0,g=0,b=0]")){
				color = Color.black;
			}

			//System.out.println("Color: " + color);

			gui.getDrawingArea().getGraphicsObject().setPaint(color);
			gui.getDrawingArea().getGraphicsObject().setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
			gui.getDrawingArea().getGraphicsObject().drawLine(x,y,newX,newY);
			gui.getDrawingArea().repaint();
		
	}
	private void translateScoreData(String serverData){
		String playersScore = "";
		String[] playersInfo = serverData.split(":");
		for(int i =0; i < playersInfo.length-1;i++){
			String[] playerInfo = playersInfo[i].split(" ");
			String playerName = playerInfo[1];
			int score = Integer.parseInt(playerInfo[2]);

			playersScore+= playerName + " " + score + " " + "\n";

		}
		this.gui.getPlayerListField().setText(playersScore);
	}

	private void translateFinalScoreData(String serverData){
		String playersScore = "";
		String[] playersInfo = serverData.split(":");
		for(int i =0; i < playersInfo.length-1;i++){
			String[] playerInfo = playersInfo[i].split(" ");
			String playerName = playerInfo[1];
			int score = Integer.parseInt(playerInfo[2]);

			playersScore+= playerName + " " + score + " " + "\n";

		}
		
		FinalScore finale = new FinalScore(playersScore);
	}

	public void sendGameData(String message){
		try{
			byte[] buf = message.getBytes();
			InetAddress address = InetAddress.getByName(serverName);
			DatagramPacket packet = new DatagramPacket(buf,buf.length,address,portNumber);
			datagramSocket.send(packet);
		}catch(Exception e){	
			System.out.println(e.getMessage());
		}
	}

	private void displayWordAsUnderscores(){
		int wordLength = this.wordToGuess.length();
		String word = "";
		for(int i = 0 ; i < wordLength; i++){
			word += "- " ;
		}
		this.gui.getWordToGuessField().setText(word);
	}


	public String getPlayerName() {
		// TODO Auto-generated method stub
		return playerName;
	}	

	public String getServerName(){
		return this.serverName;
	}

	public int getPortNumber(){
		return this.portNumber;
	}
	
	public static void main(String[] args) throws IOException{
		GameClient client = null;

		if(args.length != 3){
			System.out.println("Usage: java GameClient server port name");
		}

		String server = args[0];
		int port = Integer.parseInt(args[1]);
		String name = args[2];
		
		client = new GameClient(server,port,name);
	}

}
