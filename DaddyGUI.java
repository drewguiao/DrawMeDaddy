
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DaddyGUI {
	private JFrame frame;
	private GameClient client;
	private Container content;
	private DrawingArea2 drawingArea;
	private JTextField inputField;
	private JButton sendButton,clearButton;
	private JPanel chatPanel,controlPanel;
	private JTextArea chatArea;
	//	private ChatArea chatArea;
	private String playerName;
	
	
	public DaddyGUI(GameClient client, String playerName){
		this.playerName = playerName;
		this.frame = new JFrame("DrawMeDaddy:" +playerName);
		this.frame.setSize(800, 800);
		this.client = client;
		this.playerName = playerName;
		content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		

		// Control Panel
		controlPanel = new JPanel();
		clearButton = new JButton("Clear");
		clearButton.addActionListener(clearAreaViaButton());
		controlPanel.add(clearButton);

		// Chat Panel
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		drawingArea = new DrawingArea2(this.client);
		
		inputField = new JTextField(10);
		inputField.addKeyListener(sendViaEnter());
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(sendViaButton());
		
		chatArea = new JTextArea(40,30);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		
		displayInstructions();
		
		chatPanel.add(inputField,BorderLayout.CENTER);
		chatPanel.add(sendButton,BorderLayout.EAST);
		chatPanel.add(chatArea,BorderLayout.NORTH);
		
		content.add(controlPanel,BorderLayout.NORTH);
		content.add(chatPanel,BorderLayout.EAST);
		content.add(drawingArea,BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	private ActionListener clearAreaViaButton(){
		ActionListener actionPerformed = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				drawingArea.clear();
				client.sendGameData("PLAYERCLEAR");
				drawingArea.repaint();
			}
		};
	return actionPerformed;
	}
	
	private ActionListener sendViaButton() {
		ActionListener actionPerformed = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendChat();
				clearField();
			}
		};
		return actionPerformed;
	}

	private KeyListener sendViaEnter() {
		KeyListener actionPerformed = new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					client.sendChat();
					clearField();
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		};
		return actionPerformed;
	}
	
	private void displayInstructions(){
		String instructions = "Players must guess the secret word based on the drawings that "
        + "the artist performs. With every turn, the word would be different and you have "
        + "80 seconds to guess it right. Points earned will depend on the time it took "
        + "you to guess. Good luck!\n";
    
		this.chatArea.append(instructions);

	}

	private void clearField(){
		inputField.setText("");
	}
	
	public JTextArea getChatArea(){
		return this.chatArea;
	}
	
	public JTextField getInputField(){
		return this.inputField;
	}
	
	public DrawingArea2 getDrawingArea(){
		return this.drawingArea;
	}
	
	public void render() {
		frame.setVisible(true);
	}

	
}
