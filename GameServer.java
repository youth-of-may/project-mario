import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.random.RandomGenerator;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;
import java.awt.event.*;
public class GameServer {
    /**
     * This class contains the main method. It manages the functionality of the server.
     */
    
  private ServerSocket ss;
  private int numPlayers;
  private int maxPlayers;
  private int timeLeft, counter;
  private int p1X, p2X, p1Y, p2Y, p1Coins, p2Coins;
  private Socket p1Socket, p2Socket;
  private ReadFromClient p1ReadRunnable, p2ReadRunnable; //these are inner classes; we did this so it would not mess up the code/processes
  private WriteToClient p1WriteRunnable, p2WriteRunnable;
  private String coinLocation0, coinLocation1, coinLocation2, coinLocation3,coinLocation4, coinLocation5, coinLocation6, coinLocation7, coinLocation8, coinLocation9, coinLocation10, coinLocation11, coinLocation12;
  private String[] coinCollection;
  private ObjectGenerator obj; 
  private boolean ongoing;
  
  public GameServer() {
    System.out.println("=== GAME SERVER ===");
        numPlayers = 0;
        maxPlayers = 2;
        p1X = 150;
        p1Y = 50;
        p2X = 300;
        p2Y = 50;

        //set intial value of coins 0
        p1Coins = 0;
        p2Coins = 0;

        timeLeft = 200;
        counter = -1;
        ongoing = timeLeft !=0;
        

        coinLocation0 = "575,194,267,396,283,362,86,428,200,142,86,149,52,122,271,60,188,69,79,201";
        coinLocation1 = "268,256,572,395,64,246,402,93,635,376,332,358,389,261,604,204,517,337,521,76";
        coinLocation2 = "664,59,536,332,566,243,253,347,615,434,127,438,428,129,233,139,59,320,129,73";
        coinLocation3 = "108,154,393,79,421,242,120,69,141,211,68,116,219,101,482,259,617,245,289,250";
        coinLocation4 = "451,200,544,211,356,236,601,192,577,247,60,149,537,313,591,360,134,320,620,369";
        coinLocation5 = "380,99,563,276,382,158,242,364,522,130,347,215,617,240,266,432,165,218,311,143";
        coinLocation6 = "417,115,539,378,545,132,569,314,262,351,131,66,463,307,57,219,257,395,575,194";
        coinLocation7 = "300,187,354,90,383,134,275,323,322,292,249,68,488,280,392,178,537,228,680,190";
        coinLocation8 = "173,327,455,325,628,276,331,132,389,301,523,157,620,201,376,396,201,340,78,279";
        coinLocation9 = "591,80,446,267,565,180,422,111,256,54,273,431,430,234,568,375,441,337,597,429";
        coinLocation10 = "437,158,240,198,82,434,437,82,355,147,144,203,557,401,516,197,695,131,132,434";
        coinLocation11 = "385,110,198,349,451,321,680,447,211,140,275,254,111,401,331,221,509,306,536,194";
        coinLocation12 = "59,120,536,194,424,404,444,441,405,295,498,130,358,310,75,87,434,322,321,159";

        coinCollection = new String[13];
        coinCollection[0] = coinLocation0;
        coinCollection[1] = coinLocation1;
        coinCollection[2] = coinLocation2;
        coinCollection[3] = coinLocation3;
        coinCollection[4] = coinLocation4;
        coinCollection[5] = coinLocation5;
        coinCollection[6] = coinLocation6;
        coinCollection[7] = coinLocation7;
        coinCollection[8] = coinLocation8;
        coinCollection[9] = coinLocation9;
        coinCollection[10] = coinLocation10;
        coinCollection[11] = coinLocation11;
        coinCollection[12] = coinLocation12;

        obj = new ObjectGenerator();
        //obj.start();

        //random generator
        RandomGenerator rand = new RandomGenerator();
        rand.start();
        
        
        // CopyOnWriteArrayList
        // manual fields, 10 coin fields

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

public class ObjectGenerator extends Thread {
    //private Timer powerUpTimer;
    private Timer coinTimer;
    
    public void run() {
        Timer coinTimer = new Timer(15000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                System.out.println(counter);
                
            }
        });
        
        coinTimer.start();
    }

}
public class RandomGenerator extends Thread {
        //private Timer powerUpTimer;
        private Timer randomTimer;

        public void run() {
            Timer randomTimer = new Timer(14000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Random rn = new Random() ;
                    counter = rn.nextInt(12);
                }
            });
            randomTimer.start();
        }

    }
Timer countdownTimer = new Timer(1000, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        timeLeft--;
        //System.out.println(timeLeft);
        if (timeLeft<= 0) {
            countdownTimer.stop();
        }
        
    }
});
public void checkCollisions() {
    
    if (counter !=-1){

    //System.out.println(coinCollection[counter]);
    String[] temp = coinCollection[counter].split(",");
    for (int i = 0; i<19; i+=2) {
        boolean horizontalCollisionPlayer1 = p1X < Integer.parseInt(temp[i]) + 20 && p1X + 40 > Integer.parseInt(temp[i]);
        boolean verticalCollisionPlayer1 = p1Y < Integer.parseInt(temp[i+1]) + 20 && p1Y + 40 > Integer.parseInt(temp[i+1]);
        boolean horizontalCollisionPlayer2 = p2X < Integer.parseInt(temp[i]) + 20 && p2X + 40 > Integer.parseInt(temp[i]);
        boolean verticalCollisionPlayer2 = p2Y < Integer.parseInt(temp[i+1]) + 20 && p2Y + 40 > Integer.parseInt(temp[i+1]);

        if (horizontalCollisionPlayer1 && verticalCollisionPlayer1) {
            temp[i] = "-1";
            temp[i + 1] = "-1";
            p1Coins++;
            System.out.println("Player 1 colliding");
        
        }
        else if (horizontalCollisionPlayer2 && verticalCollisionPlayer2) {
            temp[i] = "-1";
            temp[i + 1] = "-1";
            p2Coins++;
            System.out.println("Player 2 colliding");
        }

    }
    coinCollection[counter] = String.join(",", temp);
    /* 
    for (int i = 0; i < 19; i+=2) {
        if (Integer.parseInt(temp[i])==p1X && Integer.parseInt(temp[i+1])==p1Y) {
            System.out.println("Colliding");
        }
    }*/
    //use loop from GameCanvas check if x coordinate matches with p1x or p2x if yes then check y if match pa rin then set both equal to -1
    /* 
    for (int i = 0; i < 19; i+=2) {
        if (Integer.parseInt(temp[i]) == p1X || Integer.parseInt(temp[i] )== p2X) {
            if (Integer.parseInt(temp[i+1]) == p1Y || Integer.parseInt(temp[i+1] )== p2Y) {
                temp[i] = "-1";
                temp[i + 1] = "-1";
                System.out.println(temp[i]);
                if (Integer.parseInt(temp[i+1]) == p1Y) {
                    p1Coins++;
                }
                else {
                    p2Coins++;
                }
                
            }
        }
    }*/
    
    //System.out.println(coinCollection[counter]);
    
}
  }
Timer collisionTimer = new Timer(10, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        checkCollisions();
        
    }
});


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
              p1Coins = dataIn.readInt();
              //System.out.println("P1's coins are " + p1Coins);
      }  else {
          p2X = dataIn.readInt();
          p2Y = dataIn.readInt();
          p2Coins = dataIn.readInt();
          //System.out.println("P2's coins are " + p2Coins);
          
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
            if (ongoing) {
                
                /*
              dataOut.writeInt(timeLeft);
              
             */
            //check if two players are connected
            /* */
            //System.out.println("updated coin count. Mario: " +p1Coins + " Peach: " + p2Coins);

            dataOut.writeBoolean(numPlayers == maxPlayers);
            if (numPlayers ==maxPlayers) {
                dataOut.writeInt(timeLeft);
            }
           
              if (playerID == 1) {
                dataOut.writeBoolean(counter !=-1);
                if (counter !=-1) {
                    dataOut.writeUTF(coinCollection[counter]);
                }
                
                  dataOut.writeInt(p2X);
                  dataOut.writeInt(p2Y);
                  dataOut.writeInt(p1Coins);
                  dataOut.writeInt(p2Coins);
                  
                  //dataOut.writeInt(p2Coins);
                  // build string to represent coins
                  // "x1,y1,x2,y2,x3,y3"
                  // "10,50,-1,-1,20"
                  // dataOut.writeInt(c1x);
                  // dataOut.writeInt(p1score);
                  // dataOut.writeInt(winner); // starts at 0, changes eventually to either 1 or 2
                  dataOut.flush();
              }
              else {
                //Sending boolean?
                dataOut.writeBoolean(counter !=-1);
                if (counter !=-1) {
                    dataOut.writeUTF(coinCollection[counter]);
                }
                  dataOut.writeInt(p1X);
                  dataOut.writeInt(p1Y);
                  dataOut.writeInt(p2Coins);
                  dataOut.writeInt(p1Coins);
                  //dataOut.writeInt(p1Coins);
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
      }
      catch(IOException e) {
          System.out.println("IOException from WTC run");
      } }
      public void sendStartMsg() {
          try {
              dataOut.writeUTF("We now have two players");
              countdownTimer.start();
              collisionTimer.start();
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
