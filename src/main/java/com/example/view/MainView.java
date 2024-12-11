package com.example.view;

import com.example.controller.AudioController;
import com.example.controller.ScreenshotController;
import com.example.controller.LocationController;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private JFrame frame;
    private JButton recordAudioStartButton;
    private JButton takeScreenshotButton;
    private JButton getLocationButton;

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
