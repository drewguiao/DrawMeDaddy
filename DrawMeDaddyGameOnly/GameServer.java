
import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;

public class GameServer implements Runnable, Constants{
	private int portNumber;
	private int playerCount = 0;
	private int gameStatus = WAITING_FOR_PLAYERS;
	private String playerData;
	private Thread t = new Thread(this);
	private DatagramSocket serverSocket = null;
	private GameState game;
	
	
	public GameServer(int portNumber) {
		// TODO Auto-generated constructor stub
		this.portNumber = portNumber;
		try{
			serverSocket = new DatagramSocket(portNumber);
			serverSocket.setSoTimeout(100);
		}catch(IOException e){
			System.err.println("Could not listen on port: "+portNumber);
			System.exit(-1);
		}
		game = new GameState();
		System.out.println("Game Started!");
		t.start();
		
	}
	
	public void broadcast(String message){
		for(Iterator<?> ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			GamePlayer player=game.getPlayers().get(name);			
			System.out.println(message);
			send(player,message);

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
	
	@Override
	public void run() {
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
						if(playerCount == 2){
							gameStatus=GAME_START;
						}
					}
					break;
				case GAME_START:
					System.out.println("game State: Start");
					broadcast("START");
					gameStatus = IN_PROGRESS;
					break;
				case IN_PROGRESS:
					if(playerData.startsWith("PLAYER")){
						
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
					
						game.update(playerName, player);
						broadcast(game.toString());
					}
					break;
			}
			
		}
		// TODO Auto-generated method stub
		
	}
	public static void main(String args[]){
		if(args.length != 1){
			System.out.println("Usage: java GameServer <port number>");
			System.exit(1);
		}
		new GameServer(Integer.parseInt(args[0]));
	}
}
