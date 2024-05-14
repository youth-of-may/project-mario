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
    boolean starUp, hurt, shellUp, sleepUp, sleep, shooting;
    ArrayList<ShellProjectile> shellProjectiles;
    BufferedImage spriteSheet;
    StarTracker starTracker;
    ShellTracker shellTracker;
    SleepTracker sleepTracker;




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
        coins = 0;
        sleep = false;
        sleepUp = false;
        starUp = false;
        hurt = false;
        shooting = false;
        direction = "up";
        shellProjectiles = new ArrayList<>();
        if(name.equals("mario"))
        {
            imagePath = "GameSprites/PLAYER1_SPRITESHEET.png";
            starTracker = new StarTracker(190, 560);
            shellTracker = new ShellTracker(225, 560);
            sleepTracker = new SleepTracker(260, 560);
        }
        else if(name.equals("peach")) {
            imagePath = "GameSprites/PLAYER2_SPRITESHEET.png";
            starTracker = new StarTracker(690, 560);
            shellTracker = new ShellTracker(725, 560);
            sleepTracker = new SleepTracker(760, 560);
        }
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
    public void setX(int posX) {
        x = posX;
    }
    public void setY(int posY) {
        y = posY;
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
    public void returnStatus() {


    }


    public String checkStatus() {
        if(starUp)
            return "star";
        else if(hurt)
            return "hurt";
        else
            return "normal";
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
    public void updateCoins(int c) {
        coins = c;
    }
    public void addCoins() {
        coins++;
    }


    @Override
    public String returnDirection() {
        return direction;
    }


    public void sleep()
    {
        xSpeed = 0;
        ySpeed = 0;
        sleep = true;
    }


    public void wakeUp()
    {
        xSpeed = 2;
        ySpeed = 2;
        sleep = false;
    }
    public void updatePowerUps(boolean shell, boolean star, boolean sleep) {
        shellUp = shell;
        starUp = star;
        sleepUp = sleep;
    }
    public boolean shellBoolean (){
        return shellUp;
    }
    public boolean starBoolean (){
        return starUp;
    }
    public boolean sleepBoolean (){
        return sleepUp;
    }


    public void changeIcons()
    {
        if(shellUp)
            shellTracker.imageX = 30;
        else
            shellTracker.imageX = 0;




        if(starUp)
            starTracker.imageX = 30;
        else
            starTracker.imageX = 0;




        if(sleepUp)
            sleepTracker.imageX = 30;
        else
            sleepTracker.imageX = 0;
    }


    public boolean playerCollision(Player other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }


    public void doPlayerCollision(Player other) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
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
                HurtMusic hurtMusic = new HurtMusic();
                hurtMusic.start();




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
                HurtMusic hurtMusic = new HurtMusic();
                hurtMusic.start();
            }
        }
    }
    public boolean enemyCollision(Enemy other)
    {
        boolean horizontalCollision = this.x < other.x + other.width && this.x + this.width > other.x;
        boolean verticalCollision = this.y < other.y + other.height && this.y + this.height > other.y;
        return horizontalCollision && verticalCollision;
    }


    public void doEnemyCollision(Enemy other) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (enemyCollision(other) && !hurt && !starUp)
        {
            if (coins>0)
                coins -= 1;
            hurt = true;
            HurtTimer hurtTimer = new HurtTimer(this);
            hurtTimer.start();
            HurtAnimation hurtAnimation = new HurtAnimation(this);
            hurtAnimation.start();
            HurtMusic hurtMusic = new HurtMusic();
            hurtMusic.start();
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
            ShellAnimation shellAnimation = new ShellAnimation();
            shellAnimation.start();
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
        if(sleepCollision(other))
        {
            sleepUp = true;
            for (Player player : players) {
                if (player != this && !player.starUp) {
                    player.sleep();
                    System.out.println("Stopped " + player.name);
                    SleepAnimation sleepAnimation = new SleepAnimation(player);
                    sleepAnimation.start();
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


    public void doProjectileCollision(ArrayList<ShellProjectile> shells, ArrayList<Player> players) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        for (Player player : players)
        {
            for (ShellProjectile shell : shells)
            {
                if (player != this && player.projectileCollision(shell) && !player.hurt)
                {
                    player.hurt = true;
                    player.coins -= 3;
                    HurtTimer hurtTimer = new HurtTimer(player);
                    hurtTimer.start();
                    HurtAnimation hurtAnimation = new HurtAnimation(player);
                    hurtAnimation.start();
                    HurtMusic hurtMusic = new HurtMusic();
                    hurtMusic.start();
                }
            }
        }
    }




    public void shootShell() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if(shellUp)
        {
            shellProjectiles.add(new ShellProjectile(x, y));
            shellUp = true;
            shooting = true;
            shellProjectiles.get(0).new ShellAnimation().start();
            ShellMusic shellMusic = new ShellMusic();
            shellMusic.start();
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
            for (Player player : players)
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                player.wakeUp();
                Player.this.sleepUp = false;
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


    private class SleepAnimation extends Thread
    {
        private Player player;


        public SleepAnimation(Player player)
        {
            this.player = player;
        }
        @Override
        public void run()
        {
            while(player.sleep)
            {
                player.imageX = 120;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            player.imageX = 0;
        }
    }


    private class ShellAnimation extends Thread
    {
        @Override
        public void run()
        {
            while(shellUp)
            {
                if(!starUp && !hurt)
                {
                    imageX = 160;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            imageX = 0;
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
        public void returnStatus()
        {
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


    private class HurtMusic extends Thread
    {
        File file;
        AudioInputStream audioStream;
        Clip clip;
        private HurtMusic() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
            file = new File("Music/HURT.wav");
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


    private class ShellMusic extends Thread
    {
        File file;
        AudioInputStream audioStream;
        Clip clip;
        private ShellMusic() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
            file = new File("Music/KICK.wav");
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


    public class StarTracker implements Objects
    {
        int x, y, imageX, imageY, width, height;
        String imagePath1;
        BufferedImage myPicture1;


        public StarTracker(int x, int y) throws IOException
        {
            this.x = x;
            this.y = y;
            imageX = 0;
            imageY = 0;
            width = 30;
            height = 30;
            imagePath1 = "GameSprites/STARTRACKER.png";
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
            g2d.drawImage(myPicture1.getSubimage(imageX, imageY, width, height), x, y, null);
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


    public class ShellTracker implements Objects
    {
        int x, y, imageX, imageY, width, height;
        String imagePath1;
        BufferedImage myPicture1;


        public ShellTracker(int x, int y) throws IOException
        {
            this.x = x;
            this.y = y;
            imageX = 0;
            imageY = 0;
            width = 30;
            height = 30;
            imagePath1 = "GameSprites/SHELLTRACKER.png";
            myPicture1 = ImageIO.read(new File(imagePath1));
        }


        @Override
        public void draw(Graphics2D g2d)
        {
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(myPicture1.getSubimage(imageX, imageY, width, height), x, y, null);
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


    public class SleepTracker implements Objects
    {
        int x, y, imageX, imageY, width, height;
        String imagePath1;
        BufferedImage myPicture1;


        public SleepTracker(int x, int y) throws IOException
        {
            this.x = x;
            this.y = y;
            imageX = 0;
            imageY = 0;
            width = 30;
            height = 30;
            imagePath1 = "GameSprites/SLEEPTRACKER.png";
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
            g2d.drawImage(myPicture1.getSubimage(imageX, imageY, width, height), x, y, null);
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




}








