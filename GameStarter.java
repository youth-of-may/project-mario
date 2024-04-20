/**
 SceneStarter is the main method that will allow us to see the GUI                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    .
 @author Chris Julian R. Macaspac (233826) & Hubert Ellyson T. Olegario (234550)
 @version March 4, 2024
 **/
/*
I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.

I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.

If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
*/
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class GameStarter
{
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException
    {
        GameFrame gd = new GameFrame(800, 600);
        gd.setGUI();
    }
}
