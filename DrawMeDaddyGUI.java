import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawMeDaddyGUI extends JFrame implements ActionListener{
		//declarations
		Button sendButton;
		TextArea chatArea;
		TextField inputField;

	public DrawMeDaddyGUI(){
		//header
		Frame frame =new Frame("Chat Console");
		frame.setLayout(new FlowLayout());

		//send button
		sendButton = new Button("Send");
		sendButton.addActionListener(this);

		//text field
		inputField = new TextField(35);

		//text area
		chatArea = new TextArea(30,40);
		chatArea.setEditable(false);

		//add all components to frame
		frame.add(chatArea);
		frame.add(inputField);
		frame.add(sendButton);

		//display
		setFont(new Font("Arial",Font.BOLD,20));
		frame.setSize(400,550);//set the size
		frame.setLocation(200,300);//set the location
		frame.setVisible(true);
		frame.validate();
	}


	public void actionPerformed(ActionEvent ae){
	  //get the message from the text field and put to chat area
	  String text =  inputField.getText();	
	  chatArea.append(text);
	  chatArea.append("\n");
	  //clear the textfield
	  inputField.setText("");
 	}


}
