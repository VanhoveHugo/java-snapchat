package com.example.controller;

import com.example.view.MainView;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioController {
    private MainView view;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;

    public AudioController(MainView view) {
        this.view = view;
    }

    public void startRecording() {
        audioFormat = new AudioFormat(8000, 8, 1, true, false);

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            new Thread(() -> {
                try {
                    File outputDir = new File("data");

                    if (!outputDir.exists())
                        outputDir.mkdir();

                    File file = new File(outputDir, "audio-" + System.currentTimeMillis() + ".wav");
                    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
                    AudioSystem.write(new AudioInputStream(targetDataLine), fileType, file);
                    System.out.println("Enregistrement de l'audio dans '" + file.getAbsolutePath() + "'.");
                } catch (IOException e) {
                    System.out.println("Erreur lors de l'enregistrement audio : " + e.getMessage());
                }
            }).start();

        } catch (LineUnavailableException e) {
            System.out.println("Erreur : Ligne audio non disponible pour l'enregistrement.");
        }
    }

    public void stopRecording() {
        if (targetDataLine != null && targetDataLine.isOpen()) {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }
}
