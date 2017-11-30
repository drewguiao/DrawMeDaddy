import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient implements Runnable{
	
	private Socket socket = null;
	private Thread thread = null;
	private DataInputStream console = null;
	private DataOutputStream streamOut = null;
	private DaddyGUI gui;
	private String playerName;
	
	public GameClient(String serverName, int portNumber, String playerName){
		this.playerName = playerName;
		System.out.println("Establishing connection!");
		try{
			socket = new Socket(serverName, portNumber);
			System.out.println("Connected:"+socket);
			start();
		}catch(UnknownHostException uhe){
			System.out.println("Host unknown: "+uhe.getMessage());
		}catch(IOException ioe){
			System.out.println("Unexpected exception: "+ioe.getMessage());
		}
	}
	
	private void start() throws IOException {
		// TODO Auto-generated method stub
		console = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
		if(thread == null){
//			client = new ClientThread(this,socket);
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		gui = new DaddyGUI(this,playerName);
		gui.render();

		try {
			console = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			try {
				this.handle(console.readUTF());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void handle(String message) {
		// TODO Auto-generated method stub
		gui.getChatArea().append(message+"\n");
	}
	

	public void sendChat() {
		// TODO Auto-generated method stub
		String message = gui.getInputField().getText();
		
		try {
			streamOut.writeUTF(message);
			streamOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void broadCastCoordinates(int x, int y){
		System.out.println("X: "+x+" Y: "+y);
	}
	
	public static void main(String[] args){
		GameClient client = null;

		String server = "localhost";
		int port = 1024;
		String name = "Drew";
		
		client = new GameClient(server,port,name);
	}	
}
