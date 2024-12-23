package Audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioController {

    private Clip currentClip;
    private Clip clip1;
    private Clip clip2;
    private Clip clip3;

    public AudioController(String filePath1, String filePath2, String filePath3) {
        try {
            clip1 = loadAudio(filePath1);
            clip2 = loadAudio(filePath2);
            clip3 = loadAudio(filePath3);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Error initializing audio clips.");
        }
    }

    private Clip loadAudio(String filePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            throw new IOException("Audio file not found: " + filePath);
        }
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }


    public void play(int trackNumber) {
        stop(); // Stop any currently playing audio

        switch (trackNumber) {
            case 1:
                currentClip = clip1;
                break;
            case 2:
                currentClip = clip2;
                break;
            case 3:
                currentClip = clip3; //INTENSE HMM
                break;
            default:
                System.out.println("Invalid track number.");
                return;
        }

        if (currentClip != null) {
            currentClip.setFramePosition(0); // Start from the beginning
            currentClip.start();
        }
    }

    public void pause() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
    }

    public void stop() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.setFramePosition(0); // Reset to the beginning
        }
    }

    public void resume() {
        if (currentClip != null && !currentClip.isRunning()) {
            currentClip.start();
        }
    }

    public boolean isPlaying() {
        return currentClip != null && currentClip.isRunning();
    }

    public void close() {
        if (clip1 != null) clip1.close();
        if (clip2 != null) clip2.close();
        if (clip3 != null) clip3.close();
    }
}
