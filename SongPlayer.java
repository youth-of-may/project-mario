import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SongPlayer
{
    String status;
    Song currentSong;

    Song STAR = new Song("Music/STAR.wav", "Star");
    Song BG = new Song("Music/BG.wav", "BG");
    Song CLEAR = new Song("Music/CLEAR.wav", "BG");

    /**
     Constructor sets everything into default mode and initializes the arraylist.
     */
    public SongPlayer() throws UnsupportedAudioFileException, LineUnavailableException, IOException
    {
        status = "STOP";
    }

    /**
     fillSong adds the songs to the arraylist
     */


    /**
     Play starts to play the current song that is selected randomly
     */
    public void play(Song song) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        if(status.equals("STOP"))
        {
            currentSong = song;
            song.play();
            status = "START";

        }
    }

    /**
     Stop halts the playing of the audio file
     */
    public void stop()
    {
        currentSong.stop();
        status = "STOP";
    }

    /**
     getStatus gets the status of the player whether it is playing audio or not
     @return the status of playing
     */
    public String getStatus()
    {
        return status;
    }

}

