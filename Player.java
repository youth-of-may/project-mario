import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player implements Objects {
    int x, y, xSpeed, ySpeed, width, height, numFrames, currentFrame;
    boolean walking, jumping, direction, falling;
    String imagePathL, imagePathR;
    BufferedImage spriteSheetL, spriteSheetR;
    SpriteThread spriteThread;

    public Player(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        xSpeed = 2;
        width = 50;
        height = 75;
        walking = true;
        jumping = false;
        falling = true;
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

        if (walking) {
            BufferedImage currentFrameImage = spriteSheetR.getSubimage(currentFrame * width, 0, width, height);
            g2d.drawImage(currentFrameImage, x, y, null);
        } else {
            // Draw standing frame
            g2d.drawImage(spriteSheetR.getSubimage(0, 0, width, height), x, y, null);
        }
    }

    public void adjustX() {
        if (direction)
            x += xSpeed;
        else
            x -= xSpeed;
    }

    @Override
    public void adjustY()
    {

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
    public boolean returnWalk() {
        return walking;
    }


    @Override
    public void changeWalk() {
        walking = !walking;
    }

    @Override
    public void changeDirection() {
        if (direction)
            direction = false;
        else
            direction = true;
    }

    @Override
    public boolean returnDirection() {
        return direction;
    }

    @Override
    public boolean returnJump() {
        return jumping;
    }

    @Override
    public void changeJump() {
        if (jumping)
            jumping = false;
        if (!jumping)
            jumping = true;
    }

    public boolean blockCollision(Block other) {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doBlockCollision(ArrayList<Block> blocks) {
        for (Block other : blocks) {
            if (blockCollision(other)) {
                // Collision detected, adjust character's position and speed
                xSpeed = 0;

                // Adjust character's position to prevent overlap
                if (x < other.x) {
                    x = other.x - width; // Move character to the left of the block
                } else {
                    x = other.x + other.width; // Move character to the right of the block
                }
            }
        }
        xSpeed = 2; // Adjust this value to match the original xSpeed
    }




    class SpriteThread extends Thread
    {
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
