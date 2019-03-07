package bg.sofia.uni.fmi.mjt.battleships.utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundPlayer {
    public static void playSound(String soundPath) {
        try {
            Clip clip = AudioSystem.getClip();
            File splash = new File(soundPath);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(splash);
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) {
            // If there was an error then we just don't play the sound
        }
    }
}
