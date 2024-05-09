import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameFrame implements KeyListener {

    private JFrame frame;
    private int width;
    private int height;
    private JLabel coin1, coin2;
    private GameCanvas canvas;
    private Player player1;
    private Player player2;

    public GameFrame(int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        frame = new JFrame();
        canvas = new GameCanvas();
        player1 = canvas.getPlayer(0);
        player2 = canvas.getPlayer(1);
        coin1 = new JLabel("Coins: " + player1.coins);
        coin2 = new JLabel("Coins: " + player2.coins);
    }

    public void setGUI() {
        frame.setSize(width, height);
        frame.setTitle("Final Project - Giron - Olegario");
        frame.setLayout(null);
        frame.add(coin1);
        frame.add(coin2);
        frame.add(canvas);
        canvas.setBounds(0, 0, 800, 600);
        coin1.setBounds(100, 400, 145, 40);
        coin2.setBounds(300, 400, 145, 40);
        canvas.setFocusable(true);
        frame.setVisible(true);
        canvas.addKeyListener(this);
        canvas.requestFocusInWindow();
    }

    private void updateCoinLabel()
    {
        coin1.setText("Coins: " + player1.coins);
        coin2.setText("Coins: " + player2.coins);
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
            updateCoinLabel(); // Update coin label after player movement
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

