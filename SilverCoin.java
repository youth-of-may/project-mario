import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SilverCoin implements Objects
{

    int x, y, width, height;
    String imagePath1;
    BufferedImage myPicture1;

    public SilverCoin(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        width = 20;
        height = 20;
        imagePath1 = "GameSprites/SILVERCOIN.png";
        myPicture1 = ImageIO.read(new File(imagePath1));
    }

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
        return x;
    }

    @Override
    public int returnY() {
        return y;
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

    public boolean starCollision(ArrayList<Star> stars)
    {
        for(Star other:stars)
        {
            boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
            boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
            if (horizontalCollision && verticalCollision) {
                return true;
            }
        }
        return false;
    }

    public boolean shellCollision(ArrayList<Shell> shells)
    {
        for(Shell other:shells)
        {
            boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
            boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
            if (horizontalCollision && verticalCollision) {
                return true;
            }
        }
        return false;
    }

    public boolean sleepCollision(ArrayList<Sleep> sleeps)
    {
        for(Sleep other: sleeps)
        {
            boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
            boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
            if (horizontalCollision && verticalCollision) {
                return true;
            }
        }
        return false;
    }
    
    public boolean scCollision(ArrayList<SilverCoin> sc)
    {
        for(SilverCoin other:sc)
        {
            boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
            boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
            if (horizontalCollision && verticalCollision) {
                return true;
            }
        }
        return false;
    }
    public boolean blockCollision(ArrayList<Block> blocks)
    {
        for(Block other:blocks)
        {
            boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
            boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
            if (horizontalCollision && verticalCollision) {
                return true;
            }
        }
        return false;
    }

}
