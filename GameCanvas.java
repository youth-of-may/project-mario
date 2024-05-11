import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameCanvas extends JComponent {
    ObjectGenerator objectGenerator;
    public boolean ongoing;
    int timeLeft;
    ArrayList<Player> players;
    ArrayList<Block> blocks;
    ArrayList<SilverCoin> sc;
    ArrayList<Star> stars;
    ArrayList<Sleep> sleeps;
    ArrayList<Shell> shells;
    ArrayList<Enemy> enemies;
    ArrayList<WinnerScreen> screens;
    BG bg = new BG(0, 0);
    PlayerIcons icon1, icon2;
    CoinCounter counter1, counter2;
    Font font;
    TimerImage timer;
    JLabel count1, count2, countdown;
    SongPlayer player;



    public GameCanvas() throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException {
        setPreferredSize(new Dimension(800, 600));
        ongoing = true;
        timeLeft = 5;
        players = new ArrayList<>();
        blocks = new ArrayList<>();
        sc = new ArrayList<>();
        stars = new ArrayList<>();
        sleeps = new ArrayList<>();
        shells = new ArrayList<>();
        enemies = new ArrayList<>();
        screens = new ArrayList<>();
        icon1 = new PlayerIcons(10, 530, "mario");
        icon2 = new PlayerIcons(510, 530, "peach");
        counter1 = new CoinCounter(60, 550);
        counter2 = new CoinCounter(560, 550);
        timer = new TimerImage(355, 550);
        objectGenerator = new ObjectGenerator();
        objectGenerator.start();
        font = Font.createFont(Font.TRUETYPE_FONT, new File("Font/FOT-YurukaStd-UB.otf"));
        count1 = new JLabel("0");
        count1.setFont(font.deriveFont(Font.PLAIN, 15f));
        count1.setBounds(130, 545, 200, 50);
        count1.setForeground(Color.WHITE);
        count2 = new JLabel("0");
        count2.setFont(font.deriveFont(Font.PLAIN, 15f));
        count2.setBounds(630, 545, 200, 50);
        count2.setForeground(Color.WHITE);
        countdown = new JLabel(String.valueOf(timeLeft));
        countdown.setFont(font.deriveFont(Font.PLAIN, 20f));
        countdown.setBounds(400, 555, 200, 50);
        countdown.setForeground(Color.WHITE);
        add(count1);
        add(count2);
        add(countdown);
        countdownTimer.start();
        player = new SongPlayer();
        addBlocks();
        addEnemies();
        stars.add(new Star(200, 200));
        player.play(player.BG);
        statusMusic();
    }

    public void addPlayers(ArrayList<Player> p) {
        //add players from GameFrame
        players = p;
        //System.out.println("Successfully added.");
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        bg.draw(g2d);
        counter1.draw(g2d);
        counter2.draw(g2d);
        icon1.draw(g2d);
        icon2.draw(g2d);
        players.get(0).starTracker.draw(g2d);
        players.get(0).shellTracker.draw(g2d);
        players.get(0).sleepTracker.draw(g2d);
        players.get(1).starTracker.draw(g2d);
        players.get(1).shellTracker.draw(g2d);
        players.get(1).sleepTracker.draw(g2d);
        timer.draw(g2d);


        for (Block block : blocks) {
            block.draw(g2d);
        }
        for (SilverCoin silverCoin : sc) {
            silverCoin.draw(g2d);
        }
        for (Star star : stars) {
            star.draw(g2d);
        }
        for (Sleep sleep : sleeps) {
            sleep.draw(g2d);
        }
        for (Shell shell : shells) {
            shell.draw(g2d);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }
        for (Player player : players) {
            player.draw(g2d);
        }
        for (WinnerScreen screen : screens) {
            screen.draw(g2d);
        }
        for (Player player : players) {
            if (player.shooting) {
                for (Player.ShellProjectile shellProjectile : player.shellProjectiles) {
                    shellProjectile.draw(g2d);
                }
            }
        }
    }


    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void checkCollisions() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        for (Player player : players) {
            for (Block block : blocks) {
                if (player.blockCollision(block)) {
                    player.doBlockCollision(blocks);
                }
            }

            Iterator<SilverCoin> iterator1 = sc.iterator();
            while (iterator1.hasNext()) {
                SilverCoin coin = iterator1.next();
                if (player.scCollision(coin)) {
                    player.doSCCollision(coin);
                    iterator1.remove();
                }
            }

            for (Player play : players) {
                if (player.playerCollision(play)) {
                    player.doPlayerCollision(play);
                }
            }

            Iterator<Star> iterator2 = stars.iterator();
            while (iterator2.hasNext()) {
                Star star = iterator2.next();
                if (player.starCollision(star)) {
                    player.doStarCollision(star);
                    iterator2.remove();
                }
            }

            Iterator<Sleep> iterator3 = sleeps.iterator();
            while (iterator3.hasNext()) {
                Sleep sleep = iterator3.next();
                if (player.sleepCollision(sleep)) {
                    player.doSleepCollision(sleep,
                            players);
                    iterator3.remove();
                }
            }

            Iterator<Shell> iterator4 = shells.iterator();
            while (iterator4.hasNext()) {
                Shell shell = iterator4.next();
                if (player.shellCollision(shell)) {
                    player.doShellCollision(shell);
                    iterator4.remove();
                }
            }

            for (Enemy enemy : enemies) {
                if (player.enemyCollision(enemy)) {
                    player.doEnemyCollision(enemy);
                }
            }

            for (Player.ShellProjectile shellProjectile : player.shellProjectiles) {
                player.doProjectileCollision(player.shellProjectiles, players);
            }
        }
    }

    public void addBlocks() throws IOException {
        for (int i = 1; i < 19; i++) {
            blocks.add(new Block(750, i * 25));
        }
        for (int i = 1; i < 19; i++) {
            blocks.add(new Block(25, i * 25));
        }
        for (int i = 1; i < 30; i++) {
            blocks.add(new Block(i * 25, 25));
        }
        for (int i = 1; i < 31; i++) {
            blocks.add(new Block(i * 25, 475));
        }
        blocks.add(new Block(100, 100));
        blocks.add(new Block(125, 100));
        blocks.add(new Block(150, 100));
        blocks.add(new Block(175, 100));
        blocks.add(new Block(250, 100));
        blocks.add(new Block(275, 100));
        blocks.add(new Block(300, 100));
        blocks.add(new Block(325, 100));
        blocks.add(new Block(100, 175));
        blocks.add(new Block(125, 175));
        blocks.add(new Block(150, 175));
        blocks.add(new Block(100, 200));
        blocks.add(new Block(100, 225));
        blocks.add(new Block(100, 250));
        blocks.add(new Block(100, 275));
        blocks.add(new Block(100, 300));
        blocks.add(new Block(100, 325));
        blocks.add(new Block(100, 350));
        blocks.add(new Block(175, 250));
        blocks.add(new Block(175, 275));
        blocks.add(new Block(175, 300));
        blocks.add(new Block(200, 300));
        blocks.add(new Block(225, 300));
        blocks.add(new Block(175, 400));
        blocks.add(new Block(175, 425));
        blocks.add(new Block(175, 450));
        blocks.add(new Block(200, 400));
        blocks.add(new Block(225, 400));
        blocks.add(new Block(300, 400));
        blocks.add(new Block(325, 400));
        blocks.add(new Block(350, 400));
        blocks.add(new Block(450, 400));
        blocks.add(new Block(475, 400));
        blocks.add(new Block(500, 400));
        blocks.add(new Block(600, 400));
        blocks.add(new Block(625, 400));
        blocks.add(new Block(650, 400));
        blocks.add(new Block(650, 425));
        blocks.add(new Block(650, 450));
        blocks.add(new Block(650, 300));
        blocks.add(new Block(625, 300));
        blocks.add(new Block(600, 300));
        blocks.add(new Block(650, 275));
        blocks.add(new Block(650, 250));
        blocks.add(new Block(650, 225));
        blocks.add(new Block(650, 200));
        blocks.add(new Block(650, 175));
        blocks.add(new Block(650, 150));
        blocks.add(new Block(650, 125));
        blocks.add(new Block(650, 100));
        blocks.add(new Block(550, 100));
        blocks.add(new Block(550, 75));
        blocks.add(new Block(550, 50));
        blocks.add(new Block(525, 100));
        blocks.add(new Block(575, 100));
    }

    public void addEnemies() throws IOException {
        enemies.add(new Enemy(53, 425, "up", true));
        enemies.add(new Enemy(692, 53, "down", true));
        enemies.add(new Enemy(210, 430, "right", true));
        enemies.add(new Enemy(570, 170, "left", false));
    }

    public void updateCoins() {
        count1.setText(String.valueOf(players.get(0).coins));
        count2.setText(String.valueOf(players.get(1).coins));
    }

    public void printWinner(String name) throws IOException {
        String winner = name;
        if (winner.equals("mario")) {
            screens.add(new WinnerScreen("mario"));
        } else if (winner.equals("peach")) {
            screens.add(new WinnerScreen("peach"));
        } else if (winner.equals("draw")) {
            screens.add(new WinnerScreen("draw"));
        }
    }

    public void restartGame() throws IOException {
        screens.clear();
        addBlocks();
        enemies.add(new Enemy(100, 300, "up", true));
        objectGenerator.stopTimer();
        objectGenerator = new ObjectGenerator();
        objectGenerator.restartTimer();
        ongoing = true;
    }

    public class ObjectGenerator extends Thread {
        private Timer powerUpTimer;
        private Timer coinTimer;
        int minX = 51;
        int maxX = 699;
        int minY = 51;
        int maxY = 449;

        public void run() {
            Timer powerUpTimer = new Timer(30000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stars.clear();
                    shells.clear();
                    sleeps.clear();
                    generatePower();
                    repaint();
                }
            });
            Timer coinTimer = new Timer(15000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sc.clear();
                    generateCoins();
                    repaint();
                }
            });
            powerUpTimer.start();
            coinTimer.start();
        }

        public void generatePower() {
            for (int i = 0; i < 3; i++) {
                int objectX = (int) (Math.random() * (maxX - minX)) + minX;
                int objectY = (int) (Math.random() * (maxY - minY)) + minY;
                int x = (int) Math.floor((Math.random() * 10) + 1);
                if (x <= 5) {
                    try {
                        Shell shell = new Shell(objectX, objectY);
                        if (!shell.blockCollision(blocks) && !shell.shellCollision(shells) && !shell.starCollision(stars) && !shell.sleepCollision(sleeps))
                            shells.add(shell);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if ((x > 5) && (x <= 8)) {
                    try {
                        Sleep sleep = new Sleep(objectX, objectY);
                        if (!sleep.blockCollision(blocks) && !sleep.shellCollision(shells) && !sleep.starCollision(stars) && !sleep.sleepCollision(sleeps))
                            sleeps.add(sleep);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if ((x > 8) && (x <= 10)) {
                    try {
                        Star star = new Star(objectX, objectY);
                        if (!star.blockCollision(blocks) && !star.shellCollision(shells) && !star.starCollision(stars) && !star.sleepCollision(sleeps))
                            stars.add(star);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        }

        public void generateCoins() {
            for (int i = 0; i < 10; i++) {
                int objectX = (int) (Math.random() * (maxX - minX)) + minX;
                int objectY = (int) (Math.random() * (maxY - minY)) + minY;
                try {
                    SilverCoin coin = new SilverCoin(objectX, objectY);
                    if (!coin.blockCollision(blocks) && !coin.scCollision(sc) && !coin.sleepCollision(sleeps) && !coin.starCollision(stars) && !coin.shellCollision(shells))
                        sc.add(coin);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        public void stopTimer() {
            if (powerUpTimer != null) {
                powerUpTimer.stop();
            }
            if (coinTimer != null) {
                coinTimer.stop();
            }
        }

        public void restartTimer() {
            if (powerUpTimer != null) {
                powerUpTimer.restart();
            }
            if (coinTimer != null) {
                coinTimer.restart();
            }
        }
    }

    private class TimerImage implements Objects
    {
        int x, y, width, height;
        String imagePath1;
        BufferedImage myPicture1;

        public TimerImage(int x, int y) throws IOException
        {
            this.x = x;
            this.y = y;
            this.width = 25;
            this.height = 25;
            imagePath1 = "GameSprites/TIMER.png";
            myPicture1 = ImageIO.read(new File(imagePath1));
        }

        @Override
        public void draw(Graphics2D g2d)
        {
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);
            // Enable antialiasing for smoother rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Use better interpolation for image scaling
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(myPicture1, x, y, null);
        }

        @Override
        public void adjustX() {

        }

        @Override
        public void adjustY() {

        }

        @Override
        public int returnX() {
            return 0;
        }

        @Override
        public int returnY() {
            return 0;
        }

        @Override
        public String returnStatus() {
            return null;
        }

        @Override
        public void changeStatus() {

        }

        @Override
        public String returnDirection() {
            return null;
        }

        @Override
        public void changeDirection(String direction) {

        }

        @Override
        public int returnCoins() {
            return 0;
        }
    }

    public String winnerCheck()
    {
        int coin1 = players.get(0).coins;
        int coin2 = players.get(1).coins;

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

    Timer countdownTimer = new Timer(1000, new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            timeLeft--;
            countdown.setText(String.valueOf(timeLeft));
            if(timeLeft <= 0)
            {
                countdownTimer.stop();
                player.stop();
                try {
                    player.play(player.CLEAR);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                }
                ongoing=false;
                try {
                    printWinner(winnerCheck());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    });

    public void statusMusic()
    {
        Timer statusTimer = new Timer(20, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Player players : players)
                {
                    if (players.returnStatus().equals("star"))
                    {
                        try {
                            playStarMusic();
                        } catch (UnsupportedAudioFileException ex) {
                            throw new RuntimeException(ex);
                        } catch (LineUnavailableException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    else if (players.returnStatus().equals("hurt"))
                    {
                        break;
                    }

                    else if (players.returnStatus().equals("normal"))
                    {
                        break;
                    }
                }
            }
        });
        statusTimer.start();
    }
    public void playStarMusic() throws UnsupportedAudioFileException, LineUnavailableException, IOException
    {
        player.stop();
        player.play(player.STAR);
    }
}
