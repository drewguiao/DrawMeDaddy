import java.net.*;
import java.io.*;
import java.awt.Color;
import java.util.Iterator;


public class GameServer implements Runnable,Constants
{  private ChatServerThread clients[];
   private ServerSocket server = null;
   private Thread       thread = null;
   private int clientCount = 0;

   
   private int portNumber;
	private int playerCount = 0;
	private int playerLimit;
	private int gameStatus = WAITING_FOR_PLAYERS;
	private String playerData;
	private DatagramSocket serverSocket = null;
	private GameState game;
	
	private GameServerThread gServer= null;
	
   public GameServer(int port, int limit)
   
   {  
   	 this.clients = new ChatServerThread[limit];
	   this.portNumber = port;
	   this.playerLimit = limit;
	   
	  setUpChatServer();
	  // setUpDrawingServer();
	  start();

	   
   }
   
   private void setUpChatServer(){
	   try
	      {  System.out.println("Binding to port " + portNumber + ", please wait  ...");
	         server = new ServerSocket(portNumber);  
	         System.out.println("Server started: " + server);
	          	          }
	      catch(IOException ioe)
	      {  System.out.println("Can not bind to port " + portNumber + ": " + ioe.getMessage()); }
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


public void start()
   {  if (thread == null)
      {  thread = new Thread(this);
      	 gServer = new GameServerThread(this,portNumber);
         thread.start();
      }
   }
   
   public void run()
   {  while (thread != null)
      {
 //      	byte[] buf = new byte[256];
 //      	DatagramPacket packet = new DatagramPacket(buf,buf.length);
	// 	try{
	// 		serverSocket.receive(packet);
	// 	}catch(Exception ioe){}
		
		
	// 	playerData = new String(buf);
	// 	playerData = playerData.trim();
	// 	switch(gameStatus){
	// 	case WAITING_FOR_PLAYERS:
	// 		if(playerData.startsWith("CONNECT")){

	// 			String tokens[] = playerData.split(" ");
	// 			GamePlayer player = new GamePlayer(tokens[1],packet.getAddress(),packet.getPort());
	// 			game.update(tokens[1],player);
	// 			System.out.println("Player connected "+tokens[1]);
			
	// 			broadcast("CONNECTED "+tokens[1]);
	// 			// change limit
	// 			playerCount++;
	// 			if(playerCount == playerLimit){
	// 				gameStatus=GAME_START;
	// 			}
	// 		}
	// 		break;
	// 	case GAME_START:
	// 		System.out.println("game State: Start");
	// 		broadcast("START");
	// 		gameStatus = IN_PROGRESS;
	// 		break;
	// 	case IN_PROGRESS:
	// 		//select word from bag of words
	// 		//display to user
	// 		if(playerData.startsWith("PLAYER")){
				
	// 			String[] playerInfo = playerData.split(" ");

	// 			String playerName = playerInfo[1];
				
	// 			int x = Integer.parseInt(playerInfo[2].trim());
	// 			int y = Integer.parseInt(playerInfo[3].trim());
	// 			int newX = Integer.parseInt(playerInfo[4].trim());
	// 			int newY = Integer.parseInt(playerInfo[5].trim());
	// 			float brushSize = Float.parseFloat(playerInfo[6].trim());
	// 			Color color = null;
	// 			if(playerInfo[7].trim().equals("java.awt.Color[r=255,g=0,b=0]")){
	// 				color = Color.RED;
	// 			}else{
	// 				color = Color.BLACK;
	// 			}
	// 			GamePlayer player = game.getPlayers().get(playerName);
				
	// 			player.setX(x);
	// 			player.setY(y);
	// 			player.setNewX(newX);
	// 			player.setNewY(newY);
	// 			player.setBrushColor(color);
	// 			player.setBrushSize(brushSize);
			
	// 			game.update(playerName, player);
	// 			broadcast(game.toString());
	// 		}
	// 		break;
	// }



        try
         {  System.out.println("Waiting for a client ..."); 
            addThread(server.accept()); 
        }
         catch(IOException ioe)
         {  System.out.println("Server accept error: " + ioe); stop(); }
      
     
      
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

   public void stop()
   {  if (thread != null)
      {  thread.stop(); 
         thread = null;
      }
   }
   private int findClient(int ID)
   {  for (int i = 0; i < clientCount; i++)
         if (clients[i].getID() == ID)
            return i;
      return -1;
   }
   public synchronized void handle(String ipAddress, int ID, String input)
   {  if (input.equals(".bye"))
      {  clients[findClient(ID)].send(".bye");
         remove(ID); }
      else
         for (int i = 0; i < clientCount; i++)
            clients[i].send(ipAddress + "/"+ID+": " + input);   
   }
   public synchronized void remove(int ID)
   {  int pos = findClient(ID);
      if (pos >= 0)
      {  ChatServerThread toTerminate = clients[pos];
         System.out.println("Removing client thread " + ID + " at " + pos);
         if (pos < clientCount-1)
            for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
         clientCount--;
         try
         {  toTerminate.close(); }
         catch(IOException ioe)
         {  System.out.println("Error closing thread: " + ioe); }
         toTerminate.stop(); }
   }
   private void addThread(Socket socket)
   {  if (clientCount < clients.length)
      {  System.out.println("Client accepted: " + socket);
         clients[clientCount] = new ChatServerThread(this, socket);
         try
         {  clients[clientCount].open(); 
            clients[clientCount].start();  
            clientCount++; }
         catch(IOException ioe)
         {  System.out.println("Error opening thread: " + ioe); } }
      else
         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }
   public static void main(String args[])
   {  GameServer server = null;
      if (args.length != 2)
         System.out.println("Usage: java GameServer port limit");
      else
         server = new GameServer(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
   }
}