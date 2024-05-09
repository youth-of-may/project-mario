/**
 This is the class that contains all of the Graphics2D objects that have been drawn. In here, arraylists, the draw method, and other methods can be found that are important for the whole program to work.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     .
 @author Princess May B. Giron (233826) & Hubert Ellyson T. Olegario (234550)
 @version March 4, 2024
 **/
/*
I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.

I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.

If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
*/

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
    ArrayList<Enemy> enemies;
    BG bg = new BG(0, 0);


    public GameCanvas() throws IOException {
        setPreferredSize(new Dimension(800, 600));
        players = new ArrayList<>();
        blocks = new ArrayList<>();
        sc = new ArrayList<>();
        stars = new ArrayList<>();
        sleeps = new ArrayList<>();
        enemies = new ArrayList<>();
        blocks.add(new Block(50, 50));
        players.add(player1 = new Player(150, 50, "mario"));
        players.add(player2 = new Player(300, 50, "peach"));
        sleeps.add(new Sleep(200, 200));
        enemies.add(new Enemy(100, 300));
        stars.add(new Star(150, 300));
        coinGenerator = new CoinGenerator();
        //coinGenerator.start();
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
        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }
    }

    public void collisionChecker() {
        Timer animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCollisions();
            }
        });
        animationTimer.start();
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void checkCollisions() {
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

            for (Enemy enemy: enemies) {
                if (player.enemyCollision(enemy))
                {
                    player.doEnemyCollision(enemy);
                }
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

                    for (int i = 0; i < 10; i++) {
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
