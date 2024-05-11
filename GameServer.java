import java.io.*;
import java.net.*;
public class GameServer {
    /**
     * This class contains the main method. It manages the functionality of the server.
     */
    
  private ServerSocket ss;
  private int numPlayers;
  private int maxPlayers;
  private int p1x, p2x, p1y, p2y;
  private Socket p1Socket, p2Socket;
  private ReadFromClient p1ReadRunnable, p2ReadRunnable; //these are inner classes; we did this so it would not mess up the code/processes
  private WriteToClient p1WriteRunnable, p2WriteRunnable;
  public GameServer() {
    System.out.println("====GAME SERVER====");
    numPlayers = 0;
    maxPlayers = 2;

    /* put here the initial values of the position of the sprite */
    p1x = 150;
    p2x = 300;
    p1y = 50;
    p2y = 50;
    try {
      ss = new ServerSocket(55555);

    }
    catch (IOException e) {
      System.out.println("IO exception from GameServer constructor.");

    }
}
  public void acceptConnections() {
    try {
      System.out.println("Waiting for connecctions");
            while (numPlayers < maxPlayers) {
                Socket s = ss.accept(); //begin accepting connections 
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                numPlayers++;
                
                out.writeInt(numPlayers); // send out the number of players connected
                System.out.println("Player #" + numPlayers + "has connected.");
                
                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wts = new WriteToClient(numPlayers, out);

                if (numPlayers == 1) {
                  p1Socket =s;
                  p1ReadRunnable = rfc;
                  p1WriteRunnable = wts;
                }
                else {
                  p2Socket = s;
                  p2ReadRunnable = rfc;
                  p2WriteRunnable = wts;
                  p1WriteRunnable.sendStartMsg();
                  p2WriteRunnable.sendStartMsg();

                  //start Threads
                  Thread readThread1 = new Thread(p1ReadRunnable);
                  Thread readThread2 = new Thread(p2ReadRunnable);
                  Thread writeThread1 = new Thread(p1WriteRunnable);
                  Thread writeThread2 = new Thread(p2WriteRunnable);
                  
                  writeThread1.start();
                  writeThread2.start();
                  readThread1.start();
                  readThread2.start();
                }
            }
            System.out.println("No longer accepting connections");
    }
    catch(IOException e) {
      System.out.println("IOexception in acceptConnections");
    }
  }
  private class ReadFromClient implements Runnable {
    private int playerID;
    private DataInputStream dataIn;
    public ReadFromClient(int p, DataInputStream d) {
      playerID = p;
      dataIn = d;
      System.out.println("RFC for " + playerID + " created.");
    }
    public void run() {
      try {

        
        while (true) {
          if (playerID == 1) {
            p1x = dataIn.readInt();
            p1y = dataIn.readInt();
            //System.out.println("P1's position is " + p1x);
          }
          else {
            p2x = dataIn.readInt();
            p2y = dataIn.readInt();
          }
        } 
      }
      catch(IOException e) {
        System.out.println("IOException in RFC");
      }
    }
  }
  private class WriteToClient implements Runnable {
    private int playerID;
    private DataOutputStream dataOut;
    public WriteToClient(int p, DataOutputStream d) {
      playerID = p;
      dataOut = d;
      System.out.println("WTC for " + playerID + " created.");
    }
    public void run() {
      
      try {
        while (true) {
          if (playerID == 1) {
            //System.out.println("Player 2's position is " + p2x);
            dataOut.writeInt(p2x);
            dataOut.writeInt(p2y);
            //System.out.println("Player 2's position is : " + p2x);
            dataOut.flush();
            
        } 
        else {
            dataOut.writeInt(p1x);
            dataOut.writeInt(p1y);
            //System.out.println("Player 1's position is : " + p1x);
            dataOut.flush();
        }
       
        
        try {
            Thread.sleep(25);
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted Exception at WTC");
        }
        }
        
      }
      catch(IOException e) {
        System.out.println("IOException in WriteToClient");
      } 
    }
    public void sendStartMsg() {
      try {
        dataOut.writeUTF("Ready. Set. Go!");
    }
    catch (IOException e) {
        System.out.println("IOexception from sendStartMsg");
    }
    }

  }
public static void main(String[] args) {
  GameServer gs = new GameServer();
  gs.acceptConnections();
}

}
