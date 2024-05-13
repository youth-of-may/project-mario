import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShellTracker implements Objects
{
    int x, y, imageX, imageY, width, height;
    String imagePath1;
    Player player;
    BufferedImage myPicture1;
    ShellTrackAnimation shellTrackAnimation;

    public ShellTracker(int x, int y, Player player) throws IOException
    {
        this.x = x;
        this.y = y;
        this.player = player;
        imageX = 0;
        imageY = 0;
        width = 30;
        height = 30;
        shellTrackAnimation = new ShellTrackAnimation();
        shellTrackAnimation.start();
        imagePath1 = "GameSprites/SHELLTRACKER.png";
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
        g2d.drawImage(myPicture1.getSubimage(imageX, imageY, width, height), x, y, null);
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
    public void returnStatus() {

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

    public class ShellTrackAnimation extends Thread
    {
        @Override
        public void run() {
            while (player.shellUp)
            {
                imageX = 30;
                try {
                    Thread.sleep(100); // Adjust the sleep duration as needed
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("SHELL");
            }
            imageX = 0;
            System.out.println("NO");
        }
    }
}

