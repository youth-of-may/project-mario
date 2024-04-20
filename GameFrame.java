import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameFrame implements KeyListener {

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

    /**
     * setGUI adds all the components in the JFrame and sets the appropriate settings
     * in order for the JFrame to be seen in the GUI.
     */
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
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            canvas.getObject(2).adjustX(-1);
            canvas.getObject(2).changeState();
            canvas.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed
    }
}
