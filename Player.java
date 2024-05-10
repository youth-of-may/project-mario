import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player implements Objects {
    int x, y, xSpeed, ySpeed, width, height, imageX, imageY, coins;
    String name, direction, imagePath;
    boolean starUp, hurt, shellUp, sleep, shooting;
    ArrayList<ShellProjectile> shellProjectiles;
    BufferedImage spriteSheet;

    public Player(int x, int y, String name) throws IOException {
        this.x = x;
        this.y = y;
        this.name = name;
        xSpeed = 2;
        ySpeed = 2;
        width = 40;
        height = 40;
        imageX = 0;
        imageY = 0;
        coins = 100;
        sleep = false;
        starUp = false;
        hurt = false;
        shooting = false;
        direction = "up";
        shellProjectiles = new ArrayList<>();
        if(name.equals("mario"))
            imagePath = "GameSprites/PLAYER1_SPRITESHEET.png";
        else if(name.equals("peach"))
            imagePath = "GameSprites/PLAYER2_SPRITESHEET.png";
        spriteSheet = ImageIO.read(new File(imagePath));
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
        g2d.drawImage(spriteSheet.getSubimage(imageX, imageY, width, height), x, y, null);
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
    public int returnCoins()
    {
        return coins;
    }

    @Override
    public String returnDirection() {
        return direction;
    }




    public boolean playerCollision(Player other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doPlayerCollision(Player other)
    {
        if (other != this && playerCollision(other))
        {
            if(other.starUp && coins > 0 && !hurt)
            {
                hurt = true;
                coins -= 3;
                if (coins < 0)
                {
                    coins = 0;
                }
                other.coins += 3;
                HurtTimer hurtTimer = new HurtTimer(this);
                hurtTimer.start();
                HurtAnimation hurtAnimation = new HurtAnimation(this);
                hurtAnimation.start();

            }

            if(starUp && other.coins > 0 && !other.hurt)

            {
                other.hurt = true;
                coins += 3;
                other.coins -= 3;
                if (other.coins < 0)
                {
                    other.coins = 0;
                }
                HurtTimer hurtTimer = new HurtTimer(other);
                hurtTimer.start();
                HurtAnimation hurtAnimation = new HurtAnimation(other);
                hurtAnimation.start();
            }
        }
    }
    public boolean enemyCollision(Enemy other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doEnemyCollision(Enemy other)
    {
        if (enemyCollision(other) && !hurt && !starUp)
        {
            coins -= 1;
            hurt = true;
            HurtTimer hurtTimer = new HurtTimer(this);
            hurtTimer.start();
            HurtAnimation hurtAnimation = new HurtAnimation(this);
            hurtAnimation.start();
        }
    }

    public boolean shellCollision(Shell other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doShellCollision(Shell other) throws IOException {
        if(shellCollision(other) && !shellUp)
        {
            shellUp = true;
        }
    }

    public boolean sleepCollision(Sleep other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doSleepCollision(Sleep other, ArrayList<Player> players)
    {
        if(sleepCollision(other)) {
            for (Player player : players) {
                if (player != this) {
                    player.xSpeed = 0;
                    player.ySpeed = 0;
                    SleepTimer sleepTimer = new SleepTimer(players);
                    sleepTimer.start();
                }
            }
        }
    }
    public boolean starCollision(Star other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doStarCollision(Star other) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (starCollision(other))
        {
            starUp = true;
            StarTimer starTimer = new StarTimer();
            starTimer.start();
            StarAnimation starAnimation = new StarAnimation();
            starAnimation.start();
            StarMusic starMusic = new StarMusic();
            starMusic.start();
        }
    }

    public boolean projectileCollision(ShellProjectile other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }

    public void doProjectileCollision(ArrayList<ShellProjectile> shells, ArrayList<Player> players)
    {
        // Iterate over each player
        for (Player player : players)
        {
            // Check collision with each shell projectile
            for (ShellProjectile shell : shells)
            {
                if (player.projectileCollision(shell))
                {
                    // Perform collision handling
                    System.out.println("Collision detected with player: " + player.name);
                    player.coins -= 3; // Deduct coins from the player
                    // Additional handling if needed
                }
            }
        }
    }


    public void shootShell() throws IOException {
        if(shellUp) {
            shellProjectiles.add(new ShellProjectile(x, y));
            shooting = true;
            shellProjectiles.get(0).new ShellAnimation().start();
            shellUp = false;
        }
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

    public void doSCCollision(SilverCoin other)
    {
        if (scCollision(other)) {
            coins += 1;
        }
    }

    private class StarTimer extends Thread
    {
        @Override
        public void run() {
            try {
                Thread.sleep(20000);
                starUp = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class HurtTimer extends Thread {
        private Player player;

        public HurtTimer(Player player)
        {
            this.player = player;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                player.hurt = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class SleepTimer extends Thread {
        private ArrayList<Player> players;
        public SleepTimer(ArrayList<Player> players) {
            this.players = players;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
                for (Player player : players) {
                    player.xSpeed = 2;
                    player.ySpeed = 2;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class HurtAnimation extends Thread
    {
        private Player player;

        public HurtAnimation(Player player) {
            this.player = player;
        }

        @Override
        public void run()
        {
            try {
                while (player.hurt)
                {
                    player.imageX = 40;
                    Thread.sleep(100);
                    player.imageX = 80;
                    Thread.sleep(100);
                }
                player.imageX = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class StarAnimation extends Thread
    {
        @Override
        public void run()
        {
            try {
                while (starUp)
                {
                    imageX = 200;
                    Thread.sleep(100);
                    imageX = 240;
                    Thread.sleep(100);
                    imageX = 280;
                    Thread.sleep(100);
                    imageX = 320;
                    Thread.sleep(100);
                }
                imageX = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class StarMusic extends Thread
    {
        File file;
        AudioInputStream audioStream;
        Clip clip;
        private StarMusic() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
            file = new File("Music/STAR.wav");
            audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        }
        @Override
        public void run()
        {
            clip.start();
        }
    }

    public class ShellProjectile implements Objects
    {
        int x, y, width, height;
        String imagePath1;
        BufferedImage myPicture1;

        public ShellProjectile(int x, int y) throws IOException {
            this.x = x;
            this.y = y;
            width = 35;
            height = 35;
            imagePath1 = "GameSprites/SHELL.png";
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

        public class ShellAnimation extends Thread
        {
            int origX = Player.this.x;
            int origY = Player.this.y;
            String origDirection = Player.this.direction;
            @Override
            public void run()
            {
                while(Math.abs(x - origX) <= 200 && Math.abs(y - origY) <= 200)
                {
                    if(origDirection.equals("up"))
                        y -= 1;
                    else if(origDirection.equals("down"))
                        y += 1;
                    else if(origDirection.equals("left"))
                        x -= 1;
                    else if(origDirection.equals("right"))
                        x += 1;
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(!Player.this.shellProjectiles.isEmpty())
                    shellProjectiles.remove(ShellProjectile.this);
            }
        }
    }



}


