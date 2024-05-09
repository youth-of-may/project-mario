import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player implements Objects {
    int x, y, xSpeed, ySpeed, width, height, coins, numFrames, currentFrame;
    String direction;
    String imagePathL, imagePathR;
    BufferedImage spriteSheetL, spriteSheetR;

    public Player(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        xSpeed = 2;
        ySpeed = 2;
        width = 50;
        height = 75;
        coins = 0;
        direction = "up";
        imagePathL = "GameSprites/Sprite_SheetL.png";
        spriteSheetL = ImageIO.read(new File(imagePathL));
        imagePathR = "GameSprites/Sprite_SheetR.png";
        spriteSheetR = ImageIO.read(new File(imagePathR));
        numFrames = 8;
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
        // Draw standing frame
        g2d.drawImage(spriteSheetR.getSubimage(0, 0, width, height), x, y, null);
    }

    public void adjustX() {
        if (direction.equals("right"))
            x += xSpeed;
        else if (direction.equals("left"))
            x -= xSpeed;
    }

    @Override
    public void adjustY() {
        if (direction.equals("down"))
            y += ySpeed;
        else if (direction.equals("up"))
            y -= ySpeed;
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
    public String returnStatus() {
        return null;
    }

    @Override
    public void changeStatus() {

    }

    @Override
    public void changeDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String returnDirection() {
        return direction;
    }


    public boolean blockCollision(Block other) {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doBlockCollision(ArrayList<Block> blocks) {
        boolean collided = false;
        for (Block other : blocks) {
            boolean horizontalCollision = false;
            boolean verticalCollision = false;

            if (blockCollision(other)) {
                collided = true;
                int dx = x + width / 2 - (other.x + other.width / 2);
                int dy = y + height / 2 - (other.y + other.height / 2);

                if (Math.abs(dx) > Math.abs(dy)) {
                    horizontalCollision = true;
                } else {
                    verticalCollision = true;
                }

                if (horizontalCollision) {
                    if (x < other.x) {
                        x = other.x - width;
                    } else {
                        x = other.x + other.width;
                    }
                    xSpeed = 0;
                } else {
                    xSpeed = 2;
                }

                if (verticalCollision) {
                    if (y < other.y) {
                        y = other.y - height;
                    } else {
                        y = other.y + other.height;
                    }
                    ySpeed = 0;
                } else {
                    ySpeed = 2;
                }

                collided = false;

                break;
            }
        }

        if (!collided) {
            xSpeed = 2;
            ySpeed = 2;
        }
    }

    public boolean scCollision(SilverCoin other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doSCCollision(ArrayList<SilverCoin> sc) {
        for (SilverCoin other: sc) {
            if (scCollision(other)) {
                coins += 1;
            }
        }
    }


}
