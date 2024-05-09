import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameFrame implements KeyListener
{

    private JFrame frame;
    private int width;
    private int height;
    private JTextArea area;
    private GameCanvas canvas;
    private Player character;

    public GameFrame(int width, int height) throws IOException
    {
        this.width = width;
        this.height = height;
        frame = new JFrame();
        area = new JTextArea();
        canvas = new GameCanvas();
        character = (Player) canvas.getObject(0);
    }

    public void setGUI() {
        frame.setSize(width, height);
        frame.setTitle("Final Project - Giron - Olegario");
        frame.setLayout(null);
        frame.add(area);
        frame.add(canvas);
        area.setLineWrap(true);
        area.setOpaque(false);
        area.setForeground(Color.BLACK);
        canvas.setBounds(0, 0, 800, 600);
        area.setBounds(37, 435, 145, 40);
        canvas.setFocusable(true);
        frame.setVisible(true);
        canvas.addKeyListener(this);
        canvas.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            character.changeDirection("up");
            character.adjustY();
            canvas.repaint();
        }

        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            character.changeDirection("down");
            character.adjustY();
            canvas.repaint();
        }

        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            character.changeDirection("right");
            character.adjustX();
            canvas.repaint();
        }

        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            character.changeDirection("left");
            character.adjustX();
            canvas.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
