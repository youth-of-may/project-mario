import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;

public class GameFrame implements KeyListener {

    private JFrame frame;
    private int p2x, p2y;
    private int playerID;
    private int width;
    private int height;
    private String winner;
    private JLabel coin1, coin2;
    private JButton button;
    private GameCanvas canvas;
    private ArrayList<Player> players;
    private Player player1;
    private Player player2;
    private Socket socket;
    private ReadFromServer rfs;
    private WriteToServer wts;
    

    public GameFrame(int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        frame = new JFrame();
        winner = "";
        button = new JButton("RETRY?");
        canvas = new GameCanvas();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    restartGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //for networking stuff
        players = new ArrayList<>();
        playerID = 0;

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
            player1 = new Player(150, 50, "mario");
            player2 = new Player(300, 50, "peach");
            p2x = 300;
            p2y = 50;

        }
        else {
            player1 = new Player(300, 50, "peach");
            player2 = new Player(150, 50, "mario");
            p2x = 150;
            p2y = 50;
        }
        players.add(player1);
        players.add(player2);
        System.out.println("Players created");
    }
    private void setUpCoins() {
        /*
         * This is for setting up the coins 
         */
        coin1 = new JLabel("Coins: " + player1.coins);
        coin2 = new JLabel("Coins: " + player2.coins);
    }

    private void connectToServer() {
        /*
         * Connecting to server
         */
        try {
            socket = new Socket("localhost", 55555);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt(); //tells you if you're the first one to connect or what
            System.out.println("You are player#" + playerID);
            rfs = new ReadFromServer(in);
            wts = new WriteToServer(out);
            rfs.waitForStartMsg();
        }
        catch(IOException e) {
            System.out.println("IOException in connectToServer");

        }
    }

    public void setGUI() throws IOException{
        connectToServer();
        createSprites();
        setUpCoins();
        updateChecker();
        canvas.addPlayers(players);
        frame.setTitle("Final Project - Giron - Olegario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout());
        canvas.setPreferredSize(new Dimension(800, 600));
        canvas.addPlayers(players);
        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        frame.add(canvas, BorderLayout.CENTER);
        //frame.add(button, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        canvas.requestFocusInWindow();
    }

    private void updateCoinLabel()
    {
        coin1.setText("Coins: " + player1.coins);
        coin2.setText("Coins: " + player2.coins);
    }

    public void updateChecker() {
        Timer animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    canvas.checkCollisions();
                    canvas.repaint();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        animationTimer.start();
        /*Timer gameEndTimer = new Timer(3000, new ActionListener() { // 5000 milliseconds = 5 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                animationTimer.stop();
                canvas.ongoing = false;
                canvas.stars.clear();
                canvas.sc.clear();
                canvas.shells.clear();
                canvas.sleeps.clear();
                canvas.blocks.clear();
                canvas.enemies.clear();
                try {
                    canvas.printWinner(winnerCheck());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                canvas.repaint(); // Repaint the canvas to clear it
            }
        });
        gameEndTimer.setRepeats(false); // Set the timer to run only once
        gameEndTimer.start();

         */

    }

    public String winnerCheck()
    {
        int coin1 = player1.coins;
        int coin2 = player2.coins;

        if(coin1 > coin2)
        {
            return "mario";
        }

        else if(coin2 > coin1)
        {
            return "peach";
        }

        else if(coin2 == coin1)
        {
            return "draw";
        }

        return null;
    }

    public void restartGame() throws IOException {
        canvas.restartGame();
        canvas.repaint();
        updateChecker();
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
            updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player1.changeDirection("down");
            player1.adjustY();
            canvas.repaint();
            updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player1.changeDirection("right");
            player1.adjustX();
            canvas.repaint();
            updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player1.changeDirection("left");
            player1.adjustX();
            canvas.repaint();
            updateCoinLabel();
        }
        else if (e.getKeyCode() == KeyEvent.VK_A) {
            player2.changeDirection("left");
            player2.adjustX();
            canvas.repaint();
            updateCoinLabel();
        }
        else if (e.getKeyCode() == KeyEvent.VK_S) {
            player2.changeDirection("down");
            player2.adjustY();
            canvas.repaint();
            updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            player2.changeDirection("right");
            player2.adjustX();
            canvas.repaint();
            updateCoinLabel();
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            player2.changeDirection("up");
            player2.adjustY();
            canvas.repaint();
            updateCoinLabel();
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            try {
                player1.shootShell();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            canvas.repaint();
            updateCoinLabel();
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
                p2x = dataIn.readInt();
                p2y = dataIn.readInt();
                if (player2 != null) {
                    player2.setX(p2x);
                    player2.setY(p2y);
                }
            }
        }
        catch(IOException e) {
            System.out.println("IOexception in readFromServer");
        }
        }
        
        public void waitForStartMsg() {
           // try {}
                //String startMsg = dataIn.readUTF();
                    //System.out.println("Message from server: " + startMsg);
                    Thread read = new Thread(rfs);
                    read.start();
                    Thread write = new Thread(wts);
                    write.start();

            //catch(IOException e) {}
    
            
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
                    //System.out.println(player1.getName());
                    dataOut.writeInt(player1.returnX());
                    dataOut.writeInt(player1.returnY());
                    dataOut.flush();
                    //System.out.println(p2x);
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



