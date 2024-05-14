import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.*;
import javax.swing.*;

public class GameFrame implements KeyListener {

    private JFrame frame;
    private int playerID;
    private int width;
    private int height;
    private Timer updateTimer;
    private Timer countdownTimer;
    private JLabel coin1, coin2;
    private JButton button;
    private GameCanvas canvas;
    private ArrayList<Player> players;
    private ArrayList<Enemy> enemies;
    private ArrayList<SilverCoin> sc; 
    private Player player1;
    private Player player2;
    private Socket socket;
    private ReadFromServer rfs;
    private WriteToServer wts;
    private String coinLocation;
    private int timeLeft;
    String[] coinCoordinates;
    private JLabel count1, count2;
    Font font;
    
    


    public GameFrame(int width, int height) throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException {
        this.width = width;
        this.height = height;
        frame = new JFrame();
        button = new JButton("RETRY?");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                restartGame();
            }
        });
        canvas = new GameCanvas();

        //for networking stuff
        players = new ArrayList<>();
        playerID = 1;

        enemies = new ArrayList<>();
        sc = new ArrayList<>();
        coinLocation = "";
        timeLeft = 1000;
        coinCoordinates = new String[20];
        font = Font.createFont(Font.TRUETYPE_FONT, new File("Font/FOT-YurukaStd-UB.otf"));

        count1 = new JLabel("0");
        count1.setFont(font.deriveFont(Font.PLAIN, 15f));
        count1.setBounds(130, 545, 200, 50);
        count1.setForeground(Color.WHITE);
        count2 = new JLabel("0");
        count2.setFont(font.deriveFont(Font.PLAIN, 15f));
        count2.setBounds(630, 545, 200, 50);
        count2.setForeground(Color.WHITE);
        
        
        /*
        player1 = canvas.getPlayer(0);
        player2 = canvas.getPlayer(1);
        coin1 = new JLabel("Coins: " + player1.coins);
        coin2 = new JLabel("Coins: " + player2.coins); */
    }

    private void createSprites() throws IOException{
        /*
         * This is for creating players based on the player id/player number
         */
        if (playerID == 1) {
            player1 = new Player(75, 50, "mario");
            player2 = new Player(685, 450, "peach");

        }
        else {
            player1 = new Player(685, 450, "peach");
            player2 = new Player(75, 50, "mario");

        }
        players.add(player1);
        players.add(player2);        
        System.out.println("Players created");

        enemies = canvas.returnEnemy();
        
        //sc = canvas.returnSC();
        

        //setUpCoins();
        
    }
    /*
    private void setUpCoins() {
        /*
         * This is for setting up the coins
         
        coin1 = new JLabel("Coins: " + String.valueOf(height)player1.returnCoins());
        coin2 = new JLabel("Coins: " + player2.coins);
    }
 */
private void updateCoins() {
    if (playerID == 1) {
    count1.setText(String.valueOf(player1.returnCoins()));
    count2.setText(String.valueOf(player2.returnCoins()));
    }
    else {
    count1.setText(String.valueOf(player2.returnCoins()));
    count2.setText(String.valueOf(player1.returnCoins()));
    }

    
}
    private void connectToServer() throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException{
        /*
         * Connecting to server
         */
        try {
            socket = new Socket("localhost", 55555);
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream in = new DataInputStream(bis);
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream out = new DataOutputStream(bos);

            //DataInputStream in = new DataInputStream(socket.getInputStream());
            //DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt(); //tells you if you're the first one to connect or what
            
            System.out.println("You are player#" + playerID);

            createSprites();
            Timer t = updateChecker();

            rfs = new ReadFromServer(in);
            wts = new WriteToServer(out);
            rfs.waitForStartMsg();
            t.start();
            
        }
        catch(IOException e) {
            System.out.println("IOException in connectToServer");

        }
    }
    public void generateCoins() {
        try {
           
            if (coinCoordinates.length ==20) { 
                coinCoordinates = coinLocation.split(",");
               for (int i = 0; i < 19; i+=2) {
            if (Integer.parseInt(coinCoordinates[i]) != -1) 
            {
            SilverCoin coin = new SilverCoin(Integer.parseInt(coinCoordinates[i]), Integer.parseInt(coinCoordinates[i+1]));
            sc.add(coin);  
            }
            
        }
        
        }
        canvas.modifySC(sc);
        }
        catch(IOException e) {
            System.out.println("IOException in generateCoins()");
        }
    }
    
    Timer coinTimer = new Timer(11000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            generateCoins();
            
        }
    });
    Timer coinTimerClear = new Timer(21000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            sc.clear();
            //generateCoins();
            canvas.repaint();
        }
    });
    public void checkCollisions() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        for (Player player : players) {
             Iterator<SilverCoin> iterator1 = sc.iterator();
            while (iterator1.hasNext()) {
                SilverCoin coin = iterator1.next();
                if (player.scCollision(coin)) {
                    player.doSCCollision(coin);
                    player.addCoins();
                    System.out.println(player.returnCoins());
                    iterator1.remove();
                }
            }
        }
       

    }

    public void setGUI() throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException{
        
        connectToServer();
        /*if (playerID == 1) {
            canvas.updateDelay(10000);
        }
        else {
            canvas.updateDelay(7000);
        }*/
        coinTimer.start();
        coinTimerClear.start();
        
        canvas.addPlayers(players);
        frame.setTitle("Final Project - Giron - Olegario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout());
        canvas.setPreferredSize(new Dimension(800, 600));
        canvas.addPlayers(players);
        frame.add(count1);
        frame.add(count2);
        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        frame.add(canvas, BorderLayout.CENTER);
        //frame.add(button, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        canvas.requestFocusInWindow();
    }
/*
    private void updateCoinLabel()
    {
        coin1.setText("Coins: " + player1.returnCoins());
        coin2.setText("Coins: " + player2.returnCoins());
    } */
    /* 
    public void resetPowerUps() {
        Timer starTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

            }
        });
        for (Player p: players) {
            if (p.starBoolean()) {
                starTimer.start();
            }
        }
        
    }*/
    public Timer updateChecker() {
        updateTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(!canvas.ongoing)
                    {
                        updateTimer.stop();
                        canvas.count1.setText("");
                        canvas.count2.setText("");
                        canvas.countdown.setText("");
                        canvas.repaint();
                    }
                    else
                    {
                        checkCollisions();
                        updateCoins();
                        canvas.checkCollisions();
                        canvas.repaint();
                        //updatePowerups then change icons
                        canvas.players.get(0).changeIcons();
                        canvas.players.get(1).changeIcons();
                        //canvas.updateCoins(playerID);
                        //updateCoinLabel();
                    }
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return updateTimer;
        //updateTimer.start();
    }

    public void restartGame()
    {
        canvas.timeLeft = 200;
        canvas.ongoing = true;
        canvas.addPlayers(players);
        updateTimer.start();
    }
    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player1.changeDirection("up");
            player1.adjustY();
            canvas.repaint();
            //updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player1.changeDirection("down");
            player1.adjustY();
            canvas.repaint();
            //updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player1.changeDirection("right");
            player1.adjustX();
            canvas.repaint();
            //updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player1.changeDirection("left");
            player1.adjustX();
            canvas.repaint();
            //updateCoinLabel();
        }
        else if (e.getKeyCode() == KeyEvent.VK_A) {
            player2.changeDirection("left");
            player2.adjustX();
            canvas.repaint();
            //updateCoinLabel();
        }
        else if (e.getKeyCode() == KeyEvent.VK_S) {
            player2.changeDirection("down");
            player2.adjustY();
            canvas.repaint();
            //updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            player2.changeDirection("right");
            player2.adjustX();
            canvas.repaint();
            //updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            player2.changeDirection("up");
            player2.adjustY();
            canvas.repaint();
            //updateCoinLabel();
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            try {
                player1.shootShell();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            canvas.repaint();
            //updateCoinLabel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    private class ReadFromServer implements Runnable {
        private DataInputStream dataIn;
        public ReadFromServer(DataInputStream d) {
            dataIn = d;
            System.out.println("RFS Runnable Created");

        }
        public void run() {
            try {
            while (true) {
                
                if (player2 != null) {
                    int p1coins, p2coins;
                        p1coins = 0;
                        p2coins = 0;
                    if (dataIn.readBoolean()) {
                        
                        timeLeft = dataIn.readInt();
                        canvas.updateTimeLeft(timeLeft);
                        //System.out.println(timeLeft);
                    }
                    
                    
                        coinLocation = dataIn.readUTF();
                       
                        //System.out.println(coinLocation);
                        //canvas.passCoinLocation(coinLocation);
                        //passCoinLocation(coinLocation);
                    //if (dataIn.readBoolean()) {}
                    int p2x = dataIn.readInt();
                    int p2y = dataIn.readInt();
                    //System.out.println(p2x);
                    player2.setX(p2x);
                    player2.setY(p2y);
                    //update coins 
                    
                    p1coins = dataIn.readInt();
                   p2coins =dataIn.readInt();
                    
                    //canvas.updateCoins(player1.returnCoins(), player2.returnCoins());
                    
                    
                    
                    
                    
                    //System.out.println(player1.returnCoins());

                    //player2.updateCoins(dataIn.readInt());
                    
                }
            }
        }
        catch(IOException e) {
            System.out.println("IOexception in readFromServer");
        }
        }
        
        public void waitForStartMsg() throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException{
            try {
                String startMsg = dataIn.readUTF();
                    System.out.println("Message from server: " + startMsg);
                    Thread read = new Thread(rfs);
                    read.start();
                    Thread write = new Thread(wts);
                    write.start();
                    canvas.updateTimeLeft(timeLeft);
                    //canvas.startCountdown();
                    canvas.playMusic();
                    for (Enemy e : enemies) {
                        //e.startThreads();
                    }
                    
            }
            catch(IOException e) {
                System.out.println("IOexception in waitForStartMsg");
            }
        } 
    }
    private class WriteToServer implements Runnable {
        private DataOutputStream dataOut;
        public WriteToServer(DataOutputStream d) {
            dataOut = d;
            System.out.println("WTS Runnable Created");

        }
        public void run() {
            try {
            while (true) {
                if (player1!= null) {
                    for (SilverCoin s : sc) {
                        //to get coords
                        //System.out.print(s.returnX() + "," + s.returnY()+ ",");
                        
                    }
                    
                    dataOut.writeInt(player1.returnX());
                    dataOut.writeInt(player1.returnY());
                    dataOut.writeInt(player1.returnCoins());
                    dataOut.flush();
                }
                try {
                    Thread.sleep(25);
                }
                catch(InterruptedException e) {
                    System.out.println("Interrupted Exception from WTS run");
                }
            }
        }
        catch(IOException e) {
            System.out.println("IOexception in WriteToserver");
        }
        }
    }
}



