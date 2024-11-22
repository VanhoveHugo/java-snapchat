package com.example;

import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class App {

    public static void main(String[] args) {
        MainView view = new MainView();
        AudioController audioController = new AudioController(view);
        ScreenshotController screenshotController = new ScreenshotController(view);
        LocationController locationController = new LocationController(view);

        view.setAudioController(audioController);
        view.setScreenshotController(screenshotController);
        view.setLocationController(locationController);

        view.show();
    }
}

// Vue
class MainView {
    private JFrame frame;
    private JButton recordAudioStartButton;
    private JButton takeScreenshotButton;
    private JButton getLocationButton;
    private JTextArea outputArea;

    public MainView() {
        frame = new JFrame("Snapchat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 100);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        recordAudioStartButton = createSmallStyledButton("Enregistrer Audio");
        takeScreenshotButton = createSmallStyledButton("Capturer Image");
        getLocationButton = createSmallStyledButton("Obtenir Coordonnées");

        buttonPanel.add(recordAudioStartButton);
        buttonPanel.add(takeScreenshotButton);
        buttonPanel.add(getLocationButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

    }

    private JButton createSmallStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    public void show() {
        frame.setVisible(true);
    }

    public void displayOutput(String message) {
        outputArea.append(message + "\n");
    }

    public void setAudioController(AudioController controller) {
        recordAudioStartButton.addActionListener(e -> {
            if (recordAudioStartButton.getText().equals("Enregistrer Audio")) {
                recordAudioStartButton.setText("Arrêter Audio");
                controller.startRecording();
            } else {
                recordAudioStartButton.setText("Enregistrer Audio");
                controller.stopRecording();
            }
        });
    }

    public void setScreenshotController(ScreenshotController controller) {
        takeScreenshotButton.addActionListener(e -> controller.takeScreenshot());
    }

    public void setLocationController(LocationController controller) {
        getLocationButton.addActionListener(e -> controller.getLocation());
    }
}

// Contrôleur Audio
class AudioController {
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

    // Pour arrêter l'enregistrement
    public void stopRecording() {
        if (targetDataLine != null && targetDataLine.isOpen()) {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }
}

// Contrôleur Screenshot
class ScreenshotController {
    private MainView view;

    public ScreenshotController(MainView view) {
        this.view = view;
    }

    public void takeScreenshot() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenShot = robot.createScreenCapture(screenRect);
            File outputDir = new File("data");


            if (!outputDir.exists())
                outputDir.mkdir();

            File file = new File(outputDir, "screenshot-" + System.currentTimeMillis() + ".png");
            ImageIO.write(screenShot, "PNG", file);
            System.out.println("Enregistrement de la capture d'écran dans '" + file.getAbsolutePath() + "'.");
        } catch (AWTException | IOException e) {
            System.out.println("Erreur lors de la capture d'écran : " + e.getMessage());
        }
    }
}

// Contrôleur Géolocalisation
class LocationController {
    private MainView view;

    public LocationController(MainView view) {
        this.view = view;
    }

    public void getLocation() {
        try {
            URI uri = new URI("https://ipinfo.io/json");
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonResponse = response.toString();

            String location = jsonResponse.substring(jsonResponse.indexOf("loc") + 7, jsonResponse.indexOf("loc") + 21);
            String latitude = location.substring(0, location.indexOf(","));
            String longitude = location.substring(location.indexOf(",") + 1);

            System.out.println("Coordonnées GPS : " + latitude + ", " + longitude);

        } catch (IOException e) {
            System.out.println("Erreur lors de la récupération de la localisation (IOException) : " + e.getMessage());
        } catch (URISyntaxException e) {
            System.out.println("Erreur dans l'URL fournie : " + e.getMessage());
        }
    }

}