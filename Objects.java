import java.awt.*;

public interface Objects
{
    void draw(Graphics2D g2d);
    void adjustX(int distance);
    void adjustY();
    int returnX();
    int returnY();
    boolean returnWalk();
    boolean returnJump();
    void changeWalk();
    void changeJump();
    void changeDirection();
    boolean returnDirection();

}
