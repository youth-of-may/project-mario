import java.awt.*;

public interface Objects
{
    void draw(Graphics2D g2d);
    void adjustX();
    void adjustY();
    int returnX();
    int returnY();
    boolean returnWalk();
    void changeWalk();
    void changeDirection();
    boolean returnDirection();

}
