import java.awt.*;

public interface Objects
{
    void draw(Graphics2D g2d);
    void adjustX(int distance);
    boolean getState();
    void changeState();
}
