import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ChatClient extends JFrame implements Runnable, ActionListener{
   
   JButton clearButton, toRedButton, toBlackButton, smallButton, mediumButton, largeButton;
   DrawingArea drawingArea;
   ActionListener actionListener = new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
         if(e.getSource() == clearButton){
            drawingArea.clear();
         }else if(e.getSource() == toBlackButton){
            drawingArea.setBrushToBlack();
         }else if(e.getSource() == toRedButton){
            drawingArea.setBrushToRed();
         }else if(e.getSource() == smallButton){
            drawingArea.setBrushSmall();
         }else if(e.getSource() == mediumButton){
            drawingArea.setBrushMedium();
         }else if(e.getSource() == largeButton){
            drawingArea.setBrushLarge();
         }
         
      }
   };
   //declarations
   private static Button sendButton;
   private static TextArea chatArea;
   private static TextField inputField;
   private static String msg;
   private Integer lineCnt = 0;

   private Socket socket              = null;
   private Thread thread              = null;
   private static DataInputStream  console   = null;
   private DataOutputStream streamOut = null;
   private ChatClientThread client    = null;

   public ChatClient(String serverName, int serverPort)
   {  System.out.println("Establishing connection. Please wait ...");
      try
      {  socket = new Socket(serverName, serverPort);
         System.out.println("Connected: " + socket);
         start();
      }
      catch(UnknownHostException uhe)
      {  System.out.println("Host unknown: " + uhe.getMessage()); }
      catch(IOException ioe)
      {  System.out.println("Unexpected exception: " + ioe.getMessage()); }
   }
   public void run()
   {  while (thread != null)
      {  try
         {  
            if(lineCnt == 0){
              JFrame drawingFrame = new JFrame("DrawMeDaddy");
              Container content = drawingFrame.getContentPane();
              content.setLayout(new BorderLayout());
              
              drawingArea = new DrawingArea();
              content.add(drawingArea, BorderLayout.CENTER);
              
              JPanel controlPanel = new JPanel();
              JPanel wordPanel = new JPanel();
              JLabel wordLabel = new JLabel("GUESS ME");
              wordPanel.add(wordLabel);
              //Area Functions
              clearButton = new JButton("Clear");

              //Brush Color
              toRedButton = new JButton("Red");
              toBlackButton = new JButton("Black");
              
              //Brush Sizes
              smallButton = new JButton("S");
              mediumButton = new JButton("M");
              largeButton = new JButton("L");
              
              clearButton.addActionListener(actionListener);
              toRedButton.addActionListener(actionListener);
              toBlackButton.addActionListener(actionListener);
              smallButton.addActionListener(actionListener);
              mediumButton.addActionListener(actionListener);
              largeButton.addActionListener(actionListener);
              
              controlPanel.add(clearButton);
              controlPanel.add(toRedButton);
              controlPanel.add(toBlackButton);
              controlPanel.add(smallButton);
              controlPanel.add(mediumButton);
              controlPanel.add(largeButton);
              
              content.add(controlPanel,BorderLayout.NORTH);
              
              content.add(wordPanel,BorderLayout.SOUTH);
              JPanel chatBox = new JPanel();
              chatBox.setLayout(new BorderLayout());
              sendButton = new Button("Send");
              sendButton.addActionListener(this);
         
              inputField = new TextField(35);
              inputField.addKeyListener(new KeyListener(){
                  @Override
                  public void keyTyped(KeyEvent e){

                  }

                  @Override
                  public void keyPressed(KeyEvent e){
                     if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        try{
                           streamOut.writeUTF(inputField.getText());
                           streamOut.flush();
                           inputField.setText("");
                            }catch(Exception IO){
                              System.out.println("");
                            }
                     }
                  }

                  @Override
                  public void keyReleased(KeyEvent e){
                     
                  } 
               });
              chatArea = new TextArea(30,40);
              chatArea.setEditable(false);
      
              chatBox.add(chatArea, BorderLayout.NORTH);
              chatBox.add(inputField, BorderLayout.CENTER);
              chatBox.add(sendButton, BorderLayout.EAST);
              content.add(chatBox,BorderLayout.EAST);
              drawingFrame.setSize(800,800);
              drawingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              drawingFrame.setVisible(true);
              lineCnt++;
            }
         }
         catch(Exception ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
      }
   }
   public void handle(String msg)
   {  if (msg.equals(".bye"))
      {  System.out.println("Good bye. Press RETURN to exit ...");
         stop();
      }
      else
        System.out.println(msg);
        chatArea.append(msg);
        chatArea.append("\n");
   }
   public void start() throws IOException
   {  console   = new DataInputStream(System.in);
      streamOut = new DataOutputStream(socket.getOutputStream());
      if (thread == null)
      {  client = new ChatClientThread(this, socket);
         thread = new Thread(this);                   
         thread.start();


      }
   }
   public void stop()
   {  if (thread != null)
      {  thread.stop();  
         thread = null;
      }
      try
      {  if (console   != null)  console.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing ..."); }
      client.close();  
      client.stop();
   }
   public static void main(String args[])
   {  ChatClient client = null;
      if (args.length != 2)
         System.out.println("Usage: java ChatClient host port");
      else
         client = new ChatClient(args[0], Integer.parseInt(args[1]));
   }

   //UI PART
   
   public void actionPerformed(ActionEvent ae){
     //get the message from the text field and put to chat area
     try{

      streamOut.writeUTF(inputField.getText());
      streamOut.flush();
      inputField.setText("");
       }catch(Exception IO){
         System.out.println("");
       }
   }

   public void keyTyped(KeyEvent e){

   }
   public void keyReleased(KeyEvent e){

   }

}