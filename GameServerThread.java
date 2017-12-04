
import java.net.*;
import java.io.*;
import java.awt.Color;
import java.util.Iterator;



public class GameServerThread extends Thread implements Constants{
	private GameServer gameServer;
    
    private int portNumber;
	private int playerCount = 0;
	private int playerLimit;
	private int gameStatus = WAITING_FOR_PLAYERS;
	private String playerData;
	private DatagramSocket serverSocket = null;
	private GameState game;
	private WordFetcher getter = new WordFetcher();
	private int numOfStages;
	private int numOfRounds = 3;
	private int stage = 0;
	private int round = 0;
	public GameServerThread(GameServer gameServer, int portNumber, int playerLimit){
		this.gameServer = gameServer;
		this.portNumber = portNumber;
		this.playerLimit = playerLimit;
		setUpDrawingServer();
		new Thread(this);
		start();
	}

   private void setUpDrawingServer(){
	   try{
		   serverSocket = new DatagramSocket(portNumber);
		   serverSocket.setSoTimeout(100);
	   }catch(IOException e){
		   System.err.println("Could not listen to port: "+portNumber);
		   System.exit(1);
	   }
	   game = new GameState();
	   System.out.println("Game Started!");
   }

	@Override
	public void run(){

		while(true){

		byte[] buf = new byte[256];
      	DatagramPacket packet = new DatagramPacket(buf,buf.length);
		try{
			serverSocket.receive(packet);
		}catch(Exception ioe){}
		
		
		playerData = new String(buf);
		playerData = playerData.trim();
		switch(gameStatus){
		case WAITING_FOR_PLAYERS:
			if(playerData.startsWith("CONNECT")){

				String tokens[] = playerData.split(" ");
				GamePlayer player = new GamePlayer(tokens[1],packet.getAddress(),packet.getPort());
				game.update(tokens[1],player);
				System.out.println("Player connected "+tokens[1]);
				broadcast("CONNECTED "+tokens[1]);
				// change limit
				playerCount++;
				if(playerCount == playerLimit){
					gameStatus=GAME_START;
				}
			}
			break;
		case GAME_START:
			if(stage == numOfStages){
				round++;
				stage = 0;
			}
			if(round >= numOfRounds){
				broadcast("endGame");
				System.out.println("GAME_END");
				gameStatus = GAME_END;
			}
			if(gameStatus != GAME_END){
				numOfStages = game.getNumOfPlayers();
				broadcast("Round "+round+" Stage "+(stage+1)+"/"+numOfStages);
				broadcast("START");
				broadcast("PLAYERCLEAR");
				broadcast("ARTIST "+game.getRandomPlayer());
				String wordToGuess = suitWord();
				broadcast(wordToGuess);
				stage++;

				// stage ++;
				// if stage = numOfPlayahs
				// reset stage
				// round++
				// if round = 3
				// get status
				gameStatus = IN_PROGRESS;
				
				
			}
			break;
		case IN_PROGRESS:
			//select word from bag of words
			//display to user
			if(playerData.startsWith("nextArtistPlease")){
				gameStatus = GAME_START;
			}else if(playerData.startsWith("divideTime")){
				broadcast("divideTime");
			}else if(playerData.startsWith("PLAYERCLEAR")){
				String message = playerData;
				broadcast(message);
			}else if(playerData.startsWith("PLAYER")){
				String[] playerInfo = playerData.split(" ");
				String playerName = playerInfo[1];
				
				int x = Integer.parseInt(playerInfo[2].trim());
				int y = Integer.parseInt(playerInfo[3].trim());
				int newX = Integer.parseInt(playerInfo[4].trim());
				int newY = Integer.parseInt(playerInfo[5].trim());
				float brushSize = Float.parseFloat(playerInfo[6].trim());
				Color color = null;
				if(playerInfo[7].trim().equals("java.awt.Color[r=255,g=0,b=0]")){
					color = Color.RED;
				}else{
					color = Color.BLACK;
				}
				GamePlayer player = game.getPlayers().get(playerName);
				
				player.setX(x);
				player.setY(y);
				player.setNewX(newX);
				player.setNewY(newY);
				player.setBrushColor(color);
				player.setBrushSize(brushSize);
			
				// game.update(playerName, player);
				broadcast(game.toString());
			}else if(playerData.startsWith("addScore")){
				String[] playerInfo = playerData.split(" ");
				String name = playerInfo[1];
				int score = Integer.parseInt(playerInfo[2]);
				game.addScore(name,score);
			}
			break;
		case GAME_END: System.out.println("GAME ENDS!!!!!!!");
			break;
			}
		}

	}


	public void send(GamePlayer player, String message){
		DatagramPacket packet;	
		byte buf[] = message.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
   public void broadcast(String message) {
	// TODO Auto-generated method stub
	   for(Iterator<?> ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			GamePlayer player=game.getPlayers().get(name);			
			System.out.println(message);
			send(player,message);

		}
	}

	private String suitWord(){
		String wordIndicator = "WordToGuess ";
		String word = this.getter.fetch();
		String wordToGuess = wordIndicator + word;
		return wordToGuess;
	}


}