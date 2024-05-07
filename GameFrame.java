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

    public GameFrame(int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        frame = new JFrame();
        area = new JTextArea();
        canvas = new GameCanvas();
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
        if(!canvas.getObject(0).returnDirection())
        {
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                if (canvas.getObject(0).returnWalk())
                {
                    canvas.getObject(0).adjustX();
                    canvas.repaint();
                }
                else
                {
                    canvas.getObject(0).changeWalk();
                    canvas.getObject(0).adjustX();
                    canvas.repaint();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                canvas.getObject(0).changeDirection();
        }
        else
        {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                if (canvas.getObject(0).returnWalk()) {
                    canvas.getObject(0).adjustX();
                    canvas.repaint();
                } else
                {
                    canvas.getObject(0).changeWalk();
                    canvas.getObject(0).adjustX();
                    canvas.repaint();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
                canvas.getObject(0).changeDirection();
        }

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            canvas.getObject(0).changeWalk();
        }
    }
}
