import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block implements Objects {
    int x;
    int y;
    int width = 30;
    int height = 30;
    boolean walk;
    String imagePath1;
    BufferedImage myPicture1;
    BufferedImage myPicture2;

    public Block(int x, int y) throws IOException
    {
        this.x = x;
        this.y = y;
        walk = false;
        imagePath1 = "GameSprites/BLOCK.png";
        myPicture1 = ImageIO.read(new File(imagePath1));
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
        g2d.drawImage(myPicture1, x, y, null);
    }

    public void adjustX()
    {
        x += 0; // Adjust x by the given distance
    }

    @Override
    public void adjustY()
    {

    }

    @Override
    public int returnX()
    {
        return x;
    }

    @Override
    public int returnY()
    {
        return y;
    }

    @Override
    public boolean returnWalk
            () {
        return false;
    }

    @Override
    public void changeWalk()
    {
        if(walk)
            walk = false;

        else if(!walk)
            walk = true;
    }

    @Override
    public void changeDirection() {

    }

    @Override
    public boolean returnDirection() {
        return false;
    }


}
