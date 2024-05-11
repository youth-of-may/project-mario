import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WinnerScreen implements Objects
{
    String winner;
    String imagePath;
    BufferedImage screen;

    public WinnerScreen(String name) throws IOException {
        winner = name;
        if(winner.equals("mario"))
            imagePath = "GameSprites/WINNER1.png";
        else if(winner.equals("peach"))
            imagePath = "GameSprites/WINNER2.png";
        else if(winner.equals("draw"))
            imagePath = "GameSprites/WINNER3.png";
        screen = ImageIO.read(new File(imagePath));
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
        g2d.drawImage(screen, 0, 0, null);
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
