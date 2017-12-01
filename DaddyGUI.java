
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;


import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class DaddyGUI {
	private JFrame frame;
	private GameClient client;
	private Container content;
	private DrawingArea2 drawingArea;
	private JTextField inputField;
	private JButton sendButton;
	private JPanel chatPanel;
	private JTextArea chatArea;
	//	private ChatArea chatArea;
	private String playerName;
	JButton clearButton, eraseButton, toRedButton, toBlackButton, toBlueButton, toGreenButton, toYellowButton, toMagentaButton, smallButton, mediumButton, largeButton;
	
	
	public DaddyGUI(GameClient client, String playerName){
		this.playerName = playerName;
		this.frame = new JFrame("DrawMeDaddy:" +playerName);
		this.frame.setSize(800, 800);
		this.client = client;
		this.playerName = playerName;
		content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		
		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		drawingArea = new DrawingArea2(this.client);
		
		ImageIcon icon = new ImageIcon("icons/red.png");
	    Image image = icon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(20, 20,java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    icon = new ImageIcon(newimg);  // transform it back
	      
	    ImageIcon icon2 = new ImageIcon("icons/black.png");
	    Image image2 = icon2.getImage();
	    Image newimg2 = image2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
	    icon2 = new ImageIcon(newimg2);

        ImageIcon icon3 = new ImageIcon("icons/blue.png");
	    Image image3 = icon3.getImage();
	    Image newimg3 = image3.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
	    icon3 = new ImageIcon(newimg3);

	    ImageIcon icon4 = new ImageIcon("icons/green.png");
	    Image image4 = icon4.getImage();
	    Image newimg4 = image4.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
	    icon4 = new ImageIcon(newimg4);

	    ImageIcon icon5 = new ImageIcon("icons/yellow.png");
	    Image image5 = icon5.getImage();
	    Image newimg5 = image5.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
	    icon5 = new ImageIcon(newimg5);

	    ImageIcon icon6 = new ImageIcon("icons/magenta.png");
	    Image image6 = icon6.getImage();
	    Image newimg6 = image6.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
	    icon6 = new ImageIcon(newimg6);

	     JPanel controlPanel = new JPanel();
	    JPanel wordPanel = new JPanel();
	    JLabel wordLabel = new JLabel("GUESS ME");
	    wordPanel.add(wordLabel);
	    //Area Functions
	    clearButton = new JButton("Clear");
	    eraseButton = new JButton("Erase");

	    //Brush Color
	    toRedButton = new JButton(icon);
	    toBlackButton = new JButton(icon2);
	    toBlueButton = new JButton(icon3);
	    toGreenButton = new JButton(icon4);
	    toYellowButton = new JButton(icon5);
	    toMagentaButton = new JButton(icon6);

	    toBlackButton.setPreferredSize(new Dimension(20,20));
	    toRedButton.setPreferredSize(new Dimension(20,20));
	    toBlueButton.setPreferredSize(new Dimension(20,20));
	    toGreenButton.setPreferredSize(new Dimension(20,20));
	    toYellowButton.setPreferredSize(new Dimension(20,20));
	    toMagentaButton.setPreferredSize(new Dimension(20,20));
	    //Brush Sizes
	    smallButton = new JButton("S");
	    mediumButton = new JButton("M");
	    largeButton = new JButton("L");

	    smallButton.setPreferredSize(new Dimension(30,30));
	    mediumButton.setPreferredSize(new Dimension(30,30));
	    largeButton.setPreferredSize(new Dimension(30,30));
	      
	    clearButton.addActionListener(actionListener);
	    toRedButton.addActionListener(actionListener);
	    toBlackButton.addActionListener(actionListener);
	    toBlueButton.addActionListener(actionListener);
	    toGreenButton.addActionListener(actionListener);
	    toYellowButton.addActionListener(actionListener);
	    toMagentaButton.addActionListener(actionListener);
	    smallButton.addActionListener(actionListener);
	    mediumButton.addActionListener(actionListener);
	    largeButton.addActionListener(actionListener);
	    eraseButton.addActionListener(actionListener);
	      
	    controlPanel.add(clearButton);
	    controlPanel.add(toRedButton);
	    controlPanel.add(toBlackButton);
	    controlPanel.add(toBlueButton);
	    controlPanel.add(toGreenButton);
	    controlPanel.add(toYellowButton);
        controlPanel.add(toMagentaButton);
        controlPanel.add(smallButton);
	    controlPanel.add(mediumButton);
	    controlPanel.add(largeButton);
	    controlPanel.add(eraseButton);
	      
	    content.add(controlPanel,BorderLayout.NORTH);
	      
        content.add(wordPanel,BorderLayout.SOUTH);
              

		inputField = new JTextField(10);
		inputField.addKeyListener(sendViaEnter());
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(sendViaButton());
		
		chatArea = new JTextArea(40,30);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		
		chatPanel.add(inputField,BorderLayout.CENTER);
		chatPanel.add(sendButton,BorderLayout.EAST);
		chatPanel.add(chatArea,BorderLayout.NORTH);
		
		content.add(chatPanel,BorderLayout.EAST);
		content.add(drawingArea,BorderLayout.CENTER);
//		frame.add(content);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	private ActionListener sendViaButton() {
		ActionListener actionPerformed = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
//				client.send
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

	ActionListener actionListener = new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
         if(e.getSource() == clearButton){
            drawingArea.clear();
         }else if(e.getSource() == toBlackButton){
            drawingArea.setBrushToBlack();
         }else if(e.getSource() == toRedButton){
            drawingArea.setBrushToRed();
         }else if(e.getSource() == toBlueButton){
            drawingArea.setBrushToBlue();
         }else if(e.getSource() == toGreenButton){
            drawingArea.setBrushToGreen();
         }else if(e.getSource() == toYellowButton){
            drawingArea.setBrushToYellow();
         }else if(e.getSource() == toMagentaButton){
            drawingArea.setBrushToMagenta();
         }else if(e.getSource() == smallButton){
            drawingArea.setBrushSmall();
         }else if(e.getSource() == mediumButton){
            drawingArea.setBrushMedium();
         }else if(e.getSource() == largeButton){
            drawingArea.setBrushLarge();
         }else if(e.getSource() == eraseButton){
            drawingArea.eraseDrawing();
         }
         
      }
   };
	
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
