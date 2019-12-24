package music;

import javax.sound.sampled.*;
import java.io.*;

public class GameSounds {


    public static Clip getClipSound(String file_name){

        File f = new File(file_name);
        Clip clip = null;

        try {
            InputStream inputStream = new FileInputStream(f);
            inputStream = new BufferedInputStream(inputStream);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(inputStream);

            try {
                clip = AudioSystem.getClip();
                clip.open(audioIn);
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

        return clip;
    }
}
