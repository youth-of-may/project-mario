import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Char1 implements Objects
{
    int x;
    int y;
    int width = 50;
    int height = 75;
    boolean walking;
    String imagePath;
    BufferedImage spriteSheet;
    int numFrames;
    int currentFrame;


    public Char1(int x, int y) throws IOException
    {
        this.x = x;
        this.y = y;
        walking = false;
        imagePath = "GameSprites/Sprite_Sheet.png";
        spriteSheet = ImageIO.read(new File(imagePath));
        numFrames = 9;
        currentFrame = 0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        // Enable antialiasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Use better interpolation for image scaling
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (walking)
        {
            BufferedImage currentFrameImage = spriteSheet.getSubimage(currentFrame * width, 0, width, height);
            g2d.drawImage(currentFrameImage, x, y, null);
            currentFrame = (currentFrame + 1) % numFrames;
        }
        else {
            // Draw standing frame
            g2d.drawImage(spriteSheet.getSubimage(0, 0, width, height), x, y, null);
        }
    }

    public void adjustX(int distance)
    {
        x += distance; // Adjust x by the given distance
    }

    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void changeState()
    {
        if(walking)
            walking = false;

        else if(!walking)
            walking = true;
    }
}
