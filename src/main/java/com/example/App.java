package com.example;

import com.example.controller.AudioController;
import com.example.controller.ScreenshotController;
import com.example.controller.LocationController;
import com.example.view.MainView;

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
