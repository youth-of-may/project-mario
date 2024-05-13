import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SongPlayer
{
    String status;
    boolean playStar, playHurt;
    Song currentSong;

    Song STAR = new Song("Music/STAR.wav", "Star");
    Song BG = new Song("Music/BG.wav", "BG");
    Song CLEAR = new Song("Music/CLEAR.wav", "BG");
    Song HURT = new Song("Music/HURT.wav", "HURT");

    /**
     Constructor sets everything into default mode and initializes the arraylist.
     */
    public SongPlayer() throws UnsupportedAudioFileException, LineUnavailableException, IOException
    {
        status = "STOP";
        playStar = false;
        playHurt = false;
    }

    /**
     fillSong adds the songs to the arraylist
     */


    /**
     Play starts to play the current song that is selected randomly
     */
    public void play(Song song) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        if(status.equals("STOP"))
        {
            currentSong = song;
            if (song.equals(STAR))
                playStar = true;
            song.play();
            status = "START";
        }

    }

    public void sneakyPlay(Song song) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (song.equals(HURT))
            playHurt = true;
        song.play();
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

