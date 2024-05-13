import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Enemy implements Objects {
    int x, y, xSpeed, ySpeed, imageX, imageY, width, height;
    String imagePath1, direction;
    boolean small;
    BufferedImage myPicture1;

    public Enemy(int x, int y, String direction, boolean small) throws IOException {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.small = small;
        if(small)
        {
            width = 40;
            height = 40;
            imagePath1 = "GameSprites/SMALLENEMY.png";
            myPicture1 = ImageIO.read(new File(imagePath1));
        }
        else
        {
            width = 80;
            height = 80;
            imagePath1 = "GameSprites/LARGEENEMY.png";
            myPicture1 = ImageIO.read(new File(imagePath1));
        }
        xSpeed = 2;
        ySpeed = 2;
    }
    public void startThreads() {
        EnemyAnimation enemyAnimation = new EnemyAnimation();
        enemyAnimation.start();
        EnemyMovement enemyMovement = new EnemyMovement();
        enemyMovement.start();
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
        g2d.drawImage(myPicture1.getSubimage(imageX, imageY, width, height), x, y, null);
    }


    @Override
    public void adjustX() {
        x += xSpeed;
    }

    @Override
    public void adjustY() {
        y += ySpeed;
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


    public class EnemyMovement extends Thread {
        int origX = Enemy.this.x;
        int origY = Enemy.this.y;
        String origDirection = Enemy.this.direction;
        boolean away = true;

        @Override
        public void run() {
            while (true) {
                if (away) {
                    while (Math.abs(x - origX) <= 380 && Math.abs(y - origY) <= 360) {
                        moveInDirection(origDirection, 1); // Move one step in the specified direction
                        sleepFor(25); // Sleep for 10 milliseconds
                    }
                    away = false;
                } else if (!away) {
                    while (Math.abs(x - origX) > 0 || Math.abs(y - origY) > 0) {
                        moveInOppositeDirection(origDirection); // Move in the opposite direction
                        sleepFor(25); // Sleep for 5 milliseconds
                    }
                    away = true;
                }
            }
        }

        private void moveInDirection(String direction, int step) {
            if (direction.equals("up"))
                y -= step;
            else if (direction.equals("down"))
                y += step;
            else if (direction.equals("left"))
                x -= step;
            else if (direction.equals("right"))
                x += step;
        }

        private void moveInOppositeDirection(String direction) {
            // Move in the opposite direction based on original direction
            if (direction.equals("up"))
                y += 1;
            else if (direction.equals("down"))
                y -= 1;
            else if (direction.equals("left"))
                x += 1;
            else if (direction.equals("right"))
                x -= 1;
        }

        private void sleepFor(int milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class EnemyAnimation extends Thread {
        @Override
        public void run() {
            boolean growing = true;
            try {
                while (true) {
                    if (small) {
                        if (growing) {
                            imageX = 0;
                            Thread.sleep(50);
                            imageX = 40;
                            Thread.sleep(50);
                            imageX = 80;
                            Thread.sleep(50);
                            imageX = 120;
                            Thread.sleep(50);
                            imageX = 160;
                            Thread.sleep(50);
                            imageX = 200;
                            Thread.sleep(75);
                            imageX = 240;
                            Thread.sleep(50);
                            imageX = 280;
                            Thread.sleep(50);
                            imageX = 320;
                            Thread.sleep(50);
                            imageX = 360;
                            Thread.sleep(50);
                            imageX = 400;
                            growing = false;
                        } else {
                            imageX = 360;
                            Thread.sleep(50);
                            imageX = 320;
                            Thread.sleep(50);
                            imageX = 280;
                            Thread.sleep(50);
                            imageX = 240;
                            Thread.sleep(50);
                            imageX = 200;
                            Thread.sleep(50);
                            imageX = 160;
                            Thread.sleep(75);
                            imageX = 120;
                            Thread.sleep(50);
                            imageX = 80;
                            Thread.sleep(50);
                            imageX = 40;
                            Thread.sleep(50);
                            imageX = 0;
                            growing = true;
                        }
                    }
                    else
                    {
                        if (growing) {
                            imageX = 0;
                            Thread.sleep(50);
                            imageX = 80;
                            Thread.sleep(50);
                            imageX = 160;
                            Thread.sleep(50);
                            imageX = 240;
                            Thread.sleep(50);
                            imageX = 320;
                            Thread.sleep(50);
                            imageX = 400;
                            Thread.sleep(75);
                            imageX = 480;
                            Thread.sleep(50);
                            imageX = 560;
                            Thread.sleep(50);
                            imageX = 640;
                            Thread.sleep(50);
                            imageX = 720;
                            Thread.sleep(50);
                            imageX = 800;
                            growing = false;
                        } else {
                            imageX = 720;
                            Thread.sleep(50);
                            imageX = 640;
                            Thread.sleep(50);
                            imageX = 560;
                            Thread.sleep(50);
                            imageX = 480;
                            Thread.sleep(50);
                            imageX = 400;
                            Thread.sleep(50);
                            imageX = 320;
                            Thread.sleep(75);
                            imageX = 240;
                            Thread.sleep(50);
                            imageX = 160;
                            Thread.sleep(50);
                            imageX = 80;
                            Thread.sleep(50);
                            imageX = 0;
                            growing = true;
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
