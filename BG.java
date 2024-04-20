import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BG implements Objects
{
    int x;
    int y;
    String imagePath1;
    String imagePath2;
    BufferedImage myPicture1;
    BufferedImage myPicture2;

    public BG(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        imagePath1 = "GameSprites/BG.png";
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

    public void adjustX(int distance)
    {
        x += distance;
    }

    @Override
    public boolean getState()
    {
        return false;
    }

    @Override
    public void changeState() {

    }


}
