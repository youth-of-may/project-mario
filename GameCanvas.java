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
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class GameCanvas extends JComponent {
    ArrayList<Objects> objects;
    ArrayList<Block> blocks;
    BG bg = new BG(0, 0);


    public GameCanvas() throws IOException {
        setPreferredSize(new Dimension(800, 600));
        objects = new ArrayList<>();
        blocks = new ArrayList<>();
        blocks.add(new Block(50, 50));
        objects.add(new Char1(550, 50));
        checkCollisions();

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
    }

    public Objects getObject(int index) {
        return objects.get(index);
    }

    public void checkCollisions()
    {
        Char1 character = (Char1) objects.get(0); // Assuming the character is always the first object in the list
        for (Block block : blocks) {
            if (character.blockCollision(block))
            {
                character.doCollision(blocks);
            }
        }
    }
}
