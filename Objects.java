import java.awt.*;

public interface Objects
{
    void draw(Graphics2D g2d);
    void adjustX();
    void adjustY();
    int returnX();
    int returnY();
    String returnStatus();
    void changeStatus();
    String returnDirection();
    void changeDirection(String direction);

}
