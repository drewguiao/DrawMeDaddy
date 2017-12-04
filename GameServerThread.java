
import java.net.*;
import java.io.*;
import java.awt.Color;
import java.util.Iterator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


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
	private int guessedRight = 0;
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
				playerCount++;
				if(playerCount == playerLimit){
					gameStatus=GAME_START;
				}
				broadcast(this.game.getScoreList());
			}
			break;
		case GAME_START:
			if(playerData.startsWith("addScore")){
				String[] playerInfo = playerData.split(" ");
				String name = playerInfo[1];
				int score = Integer.parseInt(playerInfo[2]);
				game.addScore(name,score);
			}
			if(stage == numOfStages){
				round++;
				stage = 0;
			}
			if(round > numOfRounds){
				broadcast("endGame");
				System.out.println("GAME_END");
				gameStatus = GAME_END;
			}
			if(gameStatus != GAME_END){
				numOfStages = game.getNumOfPlayers();
				broadcast("Round "+round+" Stage "+(stage+1)+"/"+numOfStages);
				broadcast("START");
				broadcast("clearDrawingArea");
				broadcast("ARTIST "+game.getRandomPlayer());
				String wordToGuess = suitWord();
				broadcast(wordToGuess);
				stage++;
				gameStatus = IN_PROGRESS;
			}
			break;
		case IN_PROGRESS:
			if(playerData.startsWith("nextArtistPlease")){
				gameStatus = GAME_START;
			}else if(playerData.startsWith("addScore")){
				String[] playerInfo = playerData.split(" ");
				String name = playerInfo[1];
				int score = Integer.parseInt(playerInfo[2]);
				game.addScore(name,score);
				broadcast(this.game.getScoreList());
			}else if(playerData.startsWith("divideTime")){
				guessedRight++;
				if(guessedRight == game.getNumOfPlayers()-1){
					
					gameStatus = GAME_START;
					broadcast("rewardArtist");
					guessedRight = 0;
				}else{
					broadcast("divideTime");	
				}
				
			}else if(playerData.startsWith("clearDrawingArea")){
				String message = playerData;
				broadcast(message);
			}else if(playerData.startsWith("COORDINATE")){
				String[] coordinateInfo = playerData.split(" ");
				String identifier = coordinateInfo[0];
				int oldX = Integer.parseInt(coordinateInfo[1]);
				int oldY = Integer.parseInt(coordinateInfo[2]);
				int newX = Integer.parseInt(coordinateInfo[3]);
				int newY = Integer.parseInt(coordinateInfo[4]);
				float brushSize = Float.parseFloat(coordinateInfo[5]);
				Color color = Color.BLACK;
				broadcast(identifier+" "+oldX+" "+oldY+" "+newX+" "+newY+" "+brushSize+" "+color);
			}
			break;
		case GAME_END: System.out.println("GAME ENDS!!!!!!!");   
					   broadcast("FINAL"+this.game.getScoreList());
					   gameStatus = WAITING_FOR_PLAYERS;
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
		String wordIndicator = "WORDTOGUESS ";
		String word = this.getter.fetch();
		String wordToGuess = wordIndicator + word;
		return wordToGuess;
	}


}