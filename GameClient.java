
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
	private String wordToGuess;
	private String playerName,serverName;
	private int portNumber;
	private boolean enableWordHints = true;
	


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
				}else if(serverData.startsWith("PLAYERCLEAR")){
					gui.getDrawingArea().clear();
					gui.getDrawingArea().repaint();
				}else if(serverData.startsWith("PLAYER")){
					translateCoordinateData(serverData);
				}else if(serverData.startsWith("PLAYERA")){
					translateCoordinateData(serverData);
				}
				else if(serverData.startsWith("WordToGuess")){
					String[] wordInfo = serverData.split(" ");
					this.wordToGuess = wordInfo[1];
					this.gui.startTimer();
				}
				
			}
		}
		
	}
	
	public void handle(String message) {
		// TODO Auto-generated method stub
		gui.getChatArea().append(message+"\n");
	}
	

	public void sendChat() {
		// TODO Auto-generated method stub
		String message = gui.getInputField().getText();
		boolean allowSending = true;
		allowSending = checkDataToSend(message);
		
		if(allowSending){
			try {
				streamOut.writeUTF(message);
				streamOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkDataToSend(String message){
		String formalMessage = message.toUpperCase();
		String formalWordToGuess = wordToGuess.toUpperCase();
		if(enableWordHints){
			if(formalMessage.equals(formalWordToGuess)){
				message = null;
				System.out.println("YOU GOT THE WORD!");
				// add score (time = score)
				// increment round stages
				// reset: enableWordHints
				enableWordHints = false;
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
	
	private void translateCoordinateData(String serverData){
		String[] playersInfo = serverData.split(":");
		for(int i = 0; i< playersInfo.length;i++){
			String[] playerInfo = playersInfo[i].split(" ");
			String playerName = playerInfo[1];
			int x = Integer.parseInt(playerInfo[2]);
			int y = Integer.parseInt(playerInfo[3]);
			int newX = Integer.parseInt(playerInfo[4]);
			int newY = Integer.parseInt(playerInfo[5]);
			float brushSize = Float.parseFloat(playerInfo[6]);
			Color color = null;
			if(playerInfo[7].trim().equals("java.awt.Color[r=255,g=0,b=0]")){
				color = Color.RED;
			}else{
				color = Color.BLACK;
			}
			gui.getDrawingArea().getGraphicsObject().setPaint(color);
			gui.getDrawingArea().getGraphicsObject().setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
			gui.getDrawingArea().getGraphicsObject().drawLine(x,y,newX,newY);
			gui.getDrawingArea().repaint();
		}
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
