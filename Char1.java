import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Char1 implements Objects
{
    int x, y, xSpeed, ySpeed, width, height, numFrames, currentFrame;
    boolean walking, jumping, falling;
    String imagePathL, imagePathR;
    BufferedImage spriteSheetL, spriteSheetR;
    SpriteThread spriteThread;

    public Char1(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        width = 50;
        height = 75;
        walking = true;
        jumping = false;
        direction = true;
        imagePathL = "GameSprites/Sprite_SheetL.png";
        spriteSheetL = ImageIO.read(new File(imagePathL));
        imagePathR = "GameSprites/Sprite_SheetR.png";
        spriteSheetR = ImageIO.read(new File(imagePathR));
        numFrames = 8;
        currentFrame = 0;
        spriteThread = new SpriteThread();
        spriteThread.start();
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

        if(!direction)
        {
            if (walking) {
                BufferedImage currentFrameImage = spriteSheetL.getSubimage(currentFrame * width, 0, width, height);
                g2d.drawImage(currentFrameImage, x, y, null);
            } else {
                // Draw standing frame
                g2d.drawImage(spriteSheetL.getSubimage(0, 0, width, height), x, y, null);
            }
        }
        else
        {
            if (walking)
            {
                BufferedImage currentFrameImage = spriteSheetR.getSubimage(currentFrame * width, 0, width, height);
                g2d.drawImage(currentFrameImage, x, y, null);
            } else {
                // Draw standing frame
                g2d.drawImage(spriteSheetR.getSubimage(0, 0, width, height), x, y, null);
            }
        }
    }

    public void adjustX(int distance)
    {
        x += distance;
    }

    @Override
    public void adjustY()
    {
        y += 2;
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
    public boolean returnWalk()
    {
        return walking;
    }

    @Override
    public boolean returnJump()
    {
        return false;
    }

    @Override
    public void changeWalk() {
        walking = !walking;
    }

    @Override
    public void changeJump() {

    }

    @Override
    public void changeDirection()
    {
        if(direction)
            direction = false;
        else
            direction = true;
    }

    @Override
    public boolean returnDirection()
    {
        return direction;
    }

    public boolean blockCollision(Block other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public boolean sleepCollision(Sleep other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public boolean starCollision(Star other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public boolean shellCollision(Shell other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    class SpriteThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    if (walking)
                    {
                        currentFrame = (currentFrame + 1) % numFrames;
                    }
                    else
                        currentFrame = 0;
                    Thread.sleep(50);
                } catch (InterruptedException f) {
                    f.printStackTrace();
                }
            }
        }
    }
}
