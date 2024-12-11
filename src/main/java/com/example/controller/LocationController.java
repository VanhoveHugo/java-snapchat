package com.example.controller;

import com.example.view.MainView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class LocationController {
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
            StringBuilder response = new StringBuilder();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Localisation obtenue : " + response);
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la localisation : " + e.getMessage());
        }
    }
}
