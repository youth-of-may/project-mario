/**
 This is the class that contains all of the Graphics2D objects that have been drawn. In here, arraylists, the draw method, and other methods can be found that are important for the whole program to work.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     .
 @author Chris Julian R. Macaspac (233826) & Hubert Ellyson T. Olegario (234550)
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

public class GameCanvas extends JComponent {
    ArrayList<Objects> objects;
    ArrayList<Block> blocks;
    ArrayList<SilverCoin> sc;
    BG bg = new BG(0, 0);


    public GameCanvas() throws IOException {
        setPreferredSize(new Dimension(800, 600));
        objects = new ArrayList<>();
        blocks = new ArrayList<>();
        sc = new ArrayList<>();
        blocks.add(new Block(50, 50));
        objects.add(new Player(150, 50));
        objects.add(new Sleep(300, 300));
        collisionChecker();
        coinGenerator();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        bg.draw(g2d);
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).draw(g2d);
        }
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).draw(g2d);
        }
        for (int i = 0; i < sc.size(); i++) {
            sc.get(i).draw(g2d);
        }
    }

    public void collisionChecker()
    {
        Timer animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCollisions();
            }
        });
        animationTimer.start();
    }
    public Objects getObject(int index)
    {
        return objects.get(index);
    }

    public void checkCollisions()
    {
        Player character = (Player) objects.get(0);
        for (Block block : blocks)
        {
            if (character.blockCollision(block))
            {
                character.doBlockCollision(blocks);
            }
        }

        Iterator<SilverCoin> iterator = sc.iterator();
        while (iterator.hasNext()) {
            SilverCoin coin = iterator.next();
            if (character.scCollision(coin)) {
                // Remove the coin from the list
                iterator.remove();
            }
        }
    }

    public void coinGenerator()
    {
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
