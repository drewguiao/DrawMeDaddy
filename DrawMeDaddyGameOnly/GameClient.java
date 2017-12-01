import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameClient extends JPanel implements Runnable{
	
	private String playerName;
	private String serverName;
	private DatagramSocket socket = new DatagramSocket();
	private Thread t = new Thread(this);
	private String serverData;
	private int portNumber;
	private boolean connected = false;
	
//	UI variables
//	private JComponent drawingArea;
	private DrawingArea drawingArea;
	private float brushSize;
	private Color brushColor;
	private JButton clearButton, toRedBrushButton, toBlackBrushButton, toSmallButton,toMediumButton,toLargeButton;
	private int currentX, currentY, previousX, previousY;

	private ActionListener actionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == clearButton){
				drawingArea.clear();
			}else if(e.getSource() == toBlackBrushButton){
				drawingArea.setBrushToBlack();
			}else if(e.getSource() == toRedBrushButton){
				drawingArea.setBrushToRed();
			}else if(e.getSource() == toSmallButton){
				drawingArea.setBrushSmall();
			}else if(e.getSource() == toMediumButton){
				drawingArea.setBrushMedium();
			}else if(e.getSource() == toLargeButton){
				drawingArea.setBrushLarge();
			}
		}
		
	};
	
	public GameClient(String serverName, int portNumber, String playerName) throws SocketException{
		this.playerName = playerName;
		this.serverName = serverName;
		this.portNumber = portNumber;
//		Set up game UI
		JFrame gameFrame = new JFrame();
		gameFrame.setTitle("DrawMedaddy: "+playerName);
		socket.setSoTimeout(100);
		Container content = gameFrame.getContentPane();
		content.setLayout(new BorderLayout());
		
//		Set up drawing Component
		drawingArea = new DrawingArea();
		
//		Set up component listeners
		drawingArea.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				brushSize = drawingArea.getBrushSize();
				brushColor = drawingArea.getBrushColor();
				
				previousX = e.getX();
				previousY = e.getY();
				currentX = previousX;
				currentY = previousY;
				send("PLAYER " +playerName+ " "+previousX+" "+ previousY+" "+currentX + " "+currentY+" " + brushSize + " " + brushColor);
				// send data here
				// data to send are coordinates, brush status
				drawingArea.getGraphicsObject().setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				drawingArea.getGraphicsObject().drawLine(previousX, previousY, previousX, previousY);
				drawingArea.repaint();
			}
			
		});
		drawingArea.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				currentX = e.getX();
				currentY = e.getY();
				//send data here
				// send("PLAYER " +playerName+ " "+currentX + " "+currentY+" " + brushSize + " " + brushColor);
				send("PLAYERa " +playerName+ " "+previousX+" "+ previousY+" "+currentX + " "+currentY+" " + brushSize + " " + brushColor);
				if(drawingArea.getGraphicsObject() != null){
					drawingArea.getGraphicsObject().setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
					drawingArea.getGraphicsObject().drawLine(previousX, previousY, currentX, currentY);
					drawingArea.repaint();
					previousX = currentX;
					previousY = currentY;
				}
				
			}
		});
		
		
		JPanel controlPanel = new JPanel();
		JPanel wordPanel = new JPanel();
		JLabel wordLabel = new JLabel("Guess me");
		wordPanel.add(wordLabel);
		
		clearButton = new JButton("Clear");
		toRedBrushButton = new JButton("Red");
		toRedBrushButton.setEnabled(false);		
		toBlackBrushButton = new JButton("Black");
		toSmallButton = new JButton("S");
		toMediumButton = new JButton("M");
		toLargeButton = new JButton("L");
		
		clearButton.addActionListener(actionListener);
		toRedBrushButton.addActionListener(actionListener);
		toBlackBrushButton.addActionListener(actionListener);
		toSmallButton.addActionListener(actionListener);
		toMediumButton.addActionListener(actionListener);
		toLargeButton.addActionListener(actionListener);
		
		controlPanel.add(clearButton);
		controlPanel.add(toRedBrushButton);
		controlPanel.add(toBlackBrushButton);
		controlPanel.add(toSmallButton);
		controlPanel.add(toMediumButton);
		controlPanel.add(toLargeButton);
		
		content.add(drawingArea, BorderLayout.CENTER);
		content.add(controlPanel, BorderLayout.NORTH);
		content.add(wordPanel, BorderLayout.SOUTH);
		
		gameFrame.setSize(800,800);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
		
		t.start();
	}//end of constructor GameClient
	
	public void send(String message){
		try{
			byte[] buf = message.getBytes();
			InetAddress address = InetAddress.getByName(serverName);
			DatagramPacket packet = new DatagramPacket(buf,buf.length,address,portNumber);
			socket.send(packet);
		}catch(Exception e){
			
		}
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
			
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			try{
				socket.receive(packet);
			}catch(Exception ioe){}
			
			
			
			serverData = new String(buf);
			serverData = serverData.trim();
			if(!connected && serverData.startsWith("CONNECTED")){
				connected = true;
				System.out.println("Connected!");
			}else if(!connected){
				send("CONNECT "+playerName);
			}else if(connected){
				if(serverData.startsWith("PLAYER")){
					String[] playersInfo = serverData.split(":");
					for(int i=0;i<playersInfo.length;i++){
						String[] playerInfo = playersInfo[i].split(" ");
						String playerName = playerInfo[1];
						int x = Integer.parseInt(playerInfo[2]);
						int y = Integer.parseInt(playerInfo[3]);
						int newX = Integer.parseInt(playerInfo[4]);
						int newY = Integer.parseInt(playerInfo[5]);
						float size = Float.parseFloat(playerInfo[6]);
						Color color = null;
						if(playerInfo[7].trim().equals("java.awt.Color[r=255,g=0,b=0]")){
							color = Color.RED;
						}else{
							color = Color.BLACK;
						}
						drawingArea.getGraphicsObject().setPaint(color);
						drawingArea.getGraphicsObject().setStroke(new BasicStroke(size,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
						drawingArea.getGraphicsObject().drawLine(x,y,newX,newY);
						drawingArea.repaint();
					}
				}
				else if(serverData.startsWith("PLAYERA")){
					String[] playersInfo = serverData.split(":");
					for(int i=0;i<playersInfo.length;i++){
						String[] playerInfo = playersInfo[i].split(" ");
						String playerName = playerInfo[1];
						int x = Integer.parseInt(playerInfo[2]);
						int y = Integer.parseInt(playerInfo[3]);
						int newX = Integer.parseInt(playerInfo[4]);
						int newY = Integer.parseInt(playerInfo[5]);
						float size = Float.parseFloat(playerInfo[6]);
						Color color = null;
						if(playerInfo[7].trim().equals("java.awt.Color[r=255,g=0,b=0]")){
							drawingArea.setBrushToRed();
						}else{
							drawingArea.setBrushToBlack();
						}
						drawingArea.getGraphicsObject().setStroke(new BasicStroke(size,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
						drawingArea.getGraphicsObject().drawLine(x,y,newX,newY);
						drawingArea.repaint();
					}
				}
			}
			
			
			
			
		}
	}

	
	public static void main(String[] args) throws SocketException{
		if(args.length != 3){
			System.out.println("Usage: java DrawMeDaddyClient <server> <portNumber> <player name>");
			System.exit(1);
		}
		new GameClient(args[0],Integer.parseInt(args[1]),args[2]);
	}

}