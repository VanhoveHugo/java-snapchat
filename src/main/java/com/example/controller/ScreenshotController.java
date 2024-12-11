package com.example.controller;

import com.example.view.MainView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotController {
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
