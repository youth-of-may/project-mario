import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CoinCounter implements Objects
{
    int x, y, width, height;
    String imagePath1;
    BufferedImage myPicture1;

    public CoinCounter(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        width = 35;
        height = 35;
        imagePath1 = "GameSprites/COINCOUNTER.png";
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
        g2d.drawImage(myPicture1, x, y, null);
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
}
