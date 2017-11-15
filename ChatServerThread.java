import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread
{  private ChatServer       server    = null;
   private Socket           socket    = null;
   private int              ID        = -1;
   private String           ipAddress = null;
   private DataInputStream  streamIn  =  null;
   private DataOutputStream streamOut = null;

   public ChatServerThread(ChatServer _server, Socket _socket)
   {  super();
      server = _server;
      socket = _socket;
      ID     = socket.getPort();
      ipAddress = socket.getInetAddress().getHostAddress();
   }
   public void send(String msg)
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
       }
       catch(IOException ioe)
       {  System.out.println(ID + " ERROR sending: " + ioe.getMessage());
          server.remove(ID);
          stop();
       }
   }
   public int getID()
   {  return ID;
   }

   public String getIPAddress(){
      return ipAddress;

   }
   public void run()
   {  System.out.println("Server Thread " + ID + " running.");
      while (true)
      {  try
         {  server.handle(ipAddress,ID, streamIn.readUTF());
         }
         catch(IOException ioe)
         {  System.out.println(ID + " ERROR reading: " + ioe.getMessage());
            server.remove(ID);
            stop();
         }
      }
   }
   public void open() throws IOException
   {  streamIn = new DataInputStream(new 
                        BufferedInputStream(socket.getInputStream()));
      streamOut = new DataOutputStream(new
                        BufferedOutputStream(socket.getOutputStream()));
   }
   public void close() throws IOException
   {  if (socket != null)    socket.close();
      if (streamIn != null)  streamIn.close();
      if (streamOut != null) streamOut.close();
   }
}