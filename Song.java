import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class Song
{
    String songName;
    String title;
    File file;
    AudioInputStream audioStream;
    Clip clip;
    Long currentFrame;


    /**
     Constructor that initializes all the needed instance fields.
     */
    public Song(String a, String b) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        songName = a;
        title = b;
        file = new File(songName);
    }

    /**
     Plays the audio
     */
    public void play() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        currentFrame = 0L;
        audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    /**
     Stops the audio
     */
    public void stop()
    {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    /**
     Returns the title of the song
     @return the title of the song
     */
    public String getTitle()
    {
        return title;
    }
}

