import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

public class GameCanvas extends JComponent
{
    CoinGenerator coinGenerator;
    Player player1;
    Player player2;
    ArrayList<Player> players;
    ArrayList<Block> blocks;
    ArrayList<SilverCoin> sc;
    ArrayList<Star> stars;
    ArrayList<Sleep> sleeps;
    ArrayList<Shell> shells;
    ArrayList<Enemy> enemies;
    BG bg = new BG(0, 0);


    public GameCanvas() throws IOException {
        setPreferredSize(new Dimension(800, 600));
        Timer timer = new Timer(16, new ActionListener() { // Adjust the delay according to your desired frame rate
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint(); // Trigger repaint every time the timer ticks
            }
        });
        timer.start();
        players = new ArrayList<>();
        blocks = new ArrayList<>();
        sc = new ArrayList<>();
        stars = new ArrayList<>();
        sleeps = new ArrayList<>();
        shells = new ArrayList<>();
        enemies = new ArrayList<>();
        for(int i = 1; i < 19; i++)
        {
            blocks.add(new Block(750, i * 25));
        }
        for(int i = 1; i < 19; i++)
        {
            blocks.add(new Block(0, i * 25));
        }
        for(int i = 1; i < 30; i++)
        {
            blocks.add(new Block(i * 25, 25));
        }
        for(int i = 1; i < 30; i++)
        {
            blocks.add(new Block(i * 25, 475));
        }

        players.add(player1 = new Player(150, 50, "mario"));
        players.add(player2 = new Player(300, 50, "peach"));
        sleeps.add(new Sleep(200, 200));
        shells.add(new Shell(250, 250));
        enemies.add(new Enemy(100, 300));
        stars.add(new Star(150, 300));
        coinGenerator = new CoinGenerator();
        coinGenerator.start();
        collisionChecker();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        bg.draw(g2d);
        for (Player player : players) {
            player.draw(g2d);
        }
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
            if (player.shooting) {
                for (Player.ShellProjectile shellProjectile : player.shellProjectiles)
                {
                    shellProjectile.draw(g2d);
                }
            }
        }
    }

    public void collisionChecker() {
        Timer animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkCollisions();
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
                if (player.playerCollision(play))
                {
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
                if (player.sleepCollision(sleep))
                {
                    player.doSleepCollision(sleep,
                            players);
                    iterator3.remove();
                }
            }

            Iterator<Shell> iterator4 = shells.iterator();
            while (iterator4.hasNext())
            {
                Shell shell = iterator4.next();
                if (player.shellCollision(shell)) {
                    player.doShellCollision(shell);
                    iterator4.remove();
                }
            }

            for (Enemy enemy: enemies) {
                if (player.enemyCollision(enemy))
                {
                    player.doEnemyCollision(enemy);
                }
            }

            for (Player.ShellProjectile shellProjectile : player.shellProjectiles)
            {
                player.doProjectileCollision(player.shellProjectiles, players);
            }
        }
    }


    public class CoinGenerator extends Thread
    {
        public void run() {
            Timer coinTimer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sc.clear();

                    for (int i = 0; i < 10; i++)
                    {
                        int coinX = (int) (Math.random() * (getWidth() - 50));
                        int coinY = (int) (Math.random() * (getHeight() - 50));


                        try {
                            sc.add(new SilverCoin(coinX, coinY));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    repaint();
                }
            });
            coinTimer.start();
        }
    }
}
