import java.io.*;
import java.net.*;
public class GameServer {
    /**
     * This class contains the main method. It manages the functionality of the server.
     */
    
  private ServerSocket ss;
  private int numPlayers;
  private int maxPlayers;
  private int p1X, p2X, p1Y, p2Y;
  private Socket p1Socket, p2Socket;
  private ReadFromClient p1ReadRunnable, p2ReadRunnable; //these are inner classes; we did this so it would not mess up the code/processes
  private WriteToClient p1WriteRunnable, p2WriteRunnable;
  public GameServer() {
    System.out.println("=== GAME SERVER ===");
        numPlayers = 0;
        maxPlayers = 2;
        p1X = 150;
        p1Y = 50;
        p2X = 300;
        p2Y = 50;

        try {
            ss = new ServerSocket(55555);

        }
        catch (IOException e) {
            System.out.println("IOException in constructor.");
        }
}
public void acceptConnections() {
  try {
      System.out.println("Waiting for connections...");
      while (numPlayers< maxPlayers) {
          Socket s = ss.accept();
          DataInputStream in = new DataInputStream(s.getInputStream());
          DataOutputStream out = new DataOutputStream(s.getOutputStream());
          numPlayers++;
          out.writeInt(numPlayers); //for sending the playerID
          System.out.println("Player #" + numPlayers +  " has connected.");
          ReadFromClient rfc= new ReadFromClient(numPlayers, in);
          WriteToClient wtc = new WriteToClient(numPlayers, out);

          if (numPlayers == 1) {
              p1Socket =s ;
              p1ReadRunnable = rfc;
              p1WriteRunnable = wtc;
          }
          else {
              p2Socket =s ;
              p2ReadRunnable = rfc;
              p2WriteRunnable = wtc;
              p1WriteRunnable.sendStartMsg();
              p2WriteRunnable.sendStartMsg();

              //starting the threads
              Thread readThread1 = new Thread(p1ReadRunnable);
              Thread readThread2 = new Thread(p2ReadRunnable);
              readThread1.start();
              readThread2.start();

              Thread writeThread1 = new Thread(p1WriteRunnable);
              Thread writeThread2 = new Thread(p2WriteRunnable);
              writeThread1.start();
              writeThread2.start();
          }
      }
      System.out.println("No longer accepting connections");
  }
  catch(IOException e) {
      System.out.println("IOexception from acceptConnections");
  }
}
private class ReadFromClient implements Runnable{
  private int playerID;
  private DataInputStream dataIn;

  public ReadFromClient(int p, DataInputStream in) {
      playerID = p;
      dataIn = in;
      System.out.println("RFC " + playerID + " Runnable created.");


  }
  public void run() {
      try {
          while (true) {
            if (playerID == 1) {
              p1X = dataIn.readInt();
              p1Y = dataIn.readInt();
      }  else {
          p2X = dataIn.readInt();
          p2Y = dataIn.readInt();
      }
          }
          
      
      }
      catch(IOException e) {
          System.out.println("IOException from RFC run()");
      }
      
  }
}
private class WriteToClient implements Runnable {
  private int playerID;
  private DataOutputStream dataOut;
  public WriteToClient(int p, DataOutputStream out) {
      playerID = p;
      dataOut = out;
      System.out.println("WTC " + playerID + " Runnable created.");

  }
  public void run() {
      try {
          while (true) {
              if (playerID == 1) {
                  dataOut.writeInt(p2X);
                  dataOut.writeInt(p2Y);
                  dataOut.flush();
              }
              else {
                  dataOut.writeInt(p1X);
                  dataOut.writeInt(p1Y);
                  dataOut.flush();
              }
              try {
                  Thread.sleep(25);
              }
              catch(InterruptedException e) {
                  System.out.println("Interrupted exception from WTC run");
              }
          }
      }
      catch(IOException e) {
          System.out.println("IOException from WTC run");
      } }
      public void sendStartMsg() {
          try {
              dataOut.writeUTF("We now have two players");
          }
          catch(IOException e) {
              System.out.println("IOexception from sendStartMSg");

          }
     
  
}
}
public static void main(String[] args) {
  GameServer gs = new GameServer();
  gs.acceptConnections();
}

}
