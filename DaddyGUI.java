
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
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
import javax.swing.JScrollPane;


public class DaddyGUI{
	private JFrame frame;
	private GameClient client;
	private Container content;
	private DrawingArea2 drawingArea;

	private JScrollPane chatScrollPane;

	private JTextField inputField,timerField,wordToGuessField;
	private JPanel chatPanel,controlPanel,timerAndPlayerListPanel;
	private JTextArea chatArea,playerListField;
	private String playerName;
	private JButton eraseButton, sendButton, clearButton, toRedButton, toBlackButton, toBlueButton, toGreenButton, toYellowButton, toMagentaButton, smallButton, mediumButton, largeButton;
	private CountdownTimer timer;

	public DaddyGUI(GameClient client, String playerName){
		this.playerName = playerName;
		this.frame = new JFrame("DrawMeDaddy:" +playerName);
		this.frame.setSize(800, 800);
		this.client = client;
		this.playerName = playerName;


		content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		

		// Control Panel
		///// BUTTON ICONS ///// 
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

	    ImageIcon smallDot = new ImageIcon("icons/dot.png");
	    Image smallDotImage = smallDot.getImage();
	    Image newSmallDot = smallDotImage.getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH);
	    smallDot = new ImageIcon(newSmallDot);

	    ImageIcon mediumDot = new ImageIcon("icons/dot.png");
	    Image mediumDotImage = mediumDot.getImage();
	    Image newMediumDot = mediumDotImage.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH);
	    mediumDot = new ImageIcon(newMediumDot);

	    ImageIcon largeDot = new ImageIcon("icons/dot.png");
	    Image largeDotImage = largeDot.getImage();
	    Image newLargeDot = largeDotImage.getScaledInstance(80,80,java.awt.Image.SCALE_SMOOTH);
	    largeDot = new ImageIcon(newLargeDot);

	    ImageIcon clearIcon = new ImageIcon("icons/clear.png");
	    Image clearImage = clearIcon.getImage();
	    Image newClearIcon = clearImage.getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH);
	    clearIcon = new ImageIcon(newClearIcon);

	    ImageIcon eraser = new ImageIcon("icons/erase.png");
	    Image eraserIcon = eraser.getImage();
	    Image newEraserIcon = eraserIcon.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
	    eraser = new ImageIcon(newEraserIcon);

	    JPanel controlPanel = new JPanel();
	    JPanel wordPanel = new JPanel();
	    JLabel wordLabel = new JLabel("GUESS ME");
	    wordPanel.add(wordLabel);
	    
		controlPanel = new JPanel();
		clearButton = new JButton(clearIcon);
		//Brush Color
	    toRedButton = new JButton(icon);
	    toBlackButton = new JButton(icon2);
	    toBlueButton = new JButton(icon3);
	    toGreenButton = new JButton(icon4);
	    toYellowButton = new JButton(icon5);
	    toMagentaButton = new JButton(icon6);
	    eraseButton = new JButton(eraser);

	    //Adjusting button sizes
	    toBlackButton.setPreferredSize(new Dimension(20,20));
	    toRedButton.setPreferredSize(new Dimension(20,20));
	    toBlueButton.setPreferredSize(new Dimension(20,20));
	    toGreenButton.setPreferredSize(new Dimension(20,20));
	    toYellowButton.setPreferredSize(new Dimension(20,20));
	    toMagentaButton.setPreferredSize(new Dimension(20,20));
	    eraseButton.setPreferredSize(new Dimension(20,20));
	   
	    //Brush Sizes
	    smallButton = new JButton(smallDot);
	    mediumButton = new JButton(mediumDot);
	    largeButton = new JButton(largeDot);	    

		smallButton.setOpaque(false);
		smallButton.setContentAreaFilled(false);
		smallButton.setBorderPainted(false);

		mediumButton.setOpaque(false);
		mediumButton.setContentAreaFilled(false);
		mediumButton.setBorderPainted(false);

		largeButton.setOpaque(false);
		largeButton.setContentAreaFilled(false);
		largeButton.setBorderPainted(false);

		clearButton.setOpaque(false);
		clearButton.setContentAreaFilled(false);
		clearButton.setBorderPainted(false);

		eraseButton.setOpaque(false);
		eraseButton.setContentAreaFilled(false);
		eraseButton.setBorderPainted(false);

	    smallButton.setPreferredSize(new Dimension(20,20));
	    mediumButton.setPreferredSize(new Dimension(30,30));
	    largeButton.setPreferredSize(new Dimension(30,30));
	    clearButton.setPreferredSize(new Dimension(30,30));

	    clearButton.addActionListener(clearAreaViaButton());
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
	      
	     clearButton.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				SwingUtilities.invokeLater(new Runnable() {

   							 @Override
    					public void run() {
    			ImageIcon clr = new ImageIcon("icons/clear_hover.png");
				Image clrImage = clr.getImage();
				Image newClr = clrImage.getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH);
				clr = new ImageIcon(newClr);

				clearButton.setIcon(clr);
     				 }
				});
			}

			public void mouseExited(MouseEvent e){
				SwingUtilities.invokeLater(new Runnable() {

   							 @Override
    					public void run() {
    			ImageIcon clrOld = new ImageIcon("icons/clear.png");
				Image clrImageOld = clrOld.getImage();
				Image oldClr = clrImageOld.getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH);
				clrOld = new ImageIcon(oldClr);

				clearButton.setIcon(clrOld);
     				 }
				});
			}
		});

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
		chatArea.setBackground(Color.black);
		chatArea.setForeground(Color.white);
		chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
       		public void adjustmentValueChanged(AdjustmentEvent e) {  
           		e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
        	}
  		});
		
		displayInstructions();

		chatPanel.add(inputField,BorderLayout.CENTER);
		chatPanel.add(sendButton,BorderLayout.EAST);
		chatPanel.add(chatScrollPane,BorderLayout.NORTH);
		
		initializeTimerAndPlayerListPanel();


		content.add(timerAndPlayerListPanel, BorderLayout.WEST);
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
         }
         // else if(e.getSource() == eraseButton){
         //    drawingArea.eraseDrawing();
         // }
         
      }
   };

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
	
	public JPanel getControlPanel(){
		return this.controlPanel;
	}

	public DrawingArea2 getDrawingArea(){
		return this.drawingArea;
	}

	private void initializeTimerAndPlayerListPanel(){
		timerAndPlayerListPanel = new JPanel();
		timerAndPlayerListPanel.setLayout(new BorderLayout());
		
		timerField = new JTextField(5);
		timerField.setEditable(false);
		// timer = new CountdownTimer(80);

		wordToGuessField = new JTextField(5);
		wordToGuessField.setEditable(false);

		playerListField = new JTextArea(10,10);
		playerListField.setEditable(false);
		
		timerAndPlayerListPanel.add(wordToGuessField,BorderLayout.CENTER);
		timerAndPlayerListPanel.add(playerListField, BorderLayout.WEST);
		timerAndPlayerListPanel.add(timerField,BorderLayout.NORTH);
		content.add(timerAndPlayerListPanel,BorderLayout.WEST);

		timer = new CountdownTimer(20,timerField);

	}

	public JTextField getTimerField(){
		return this.timerField;
	}

	public void initializeTimer(){
		timer = new CountdownTimer(20,timerField);
	}

	public void startTimer(){
		timer.start();
	}

	public JTextArea getPlayerListField(){
		return this.playerListField;
	}

	public JTextField getWordToGuessField(){
		return this.wordToGuessField;
	}

	public CountdownTimer getTimer(){
		return this.timer;
	}
	
	public void render() {
		frame.setVisible(true);
	}

	
}
