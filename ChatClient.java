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

public class ChatClient extends JFrame implements Runnable, ActionListener{

   //declarations
   private Button sendButton;
   private TextArea chatArea;
   private TextField inputField;

   private Socket socket              = null;
   private Thread thread              = null;
   private DataInputStream  console   = null;
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
         {  streamOut.writeUTF(console.readLine());
            streamOut.flush();
         }
         catch(IOException ioe)
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
   public void DrawMeDaddyGUI(){
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
      //frame.setSize(400,550);//set the size
      frame.setSize(400,550);
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