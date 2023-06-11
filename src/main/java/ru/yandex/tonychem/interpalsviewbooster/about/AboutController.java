package ru.yandex.tonychem.interpalsviewbooster.about;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

import java.io.IOException;


public class AboutController {

    private final String githubURL = "https://github.com/tonychem";

    @FXML
    private Hyperlink githubLink;

    public void openGitHub(ActionEvent event) {
        Runtime runtime = Runtime.getRuntime();
        String userOS = System.getProperty("os.name").toLowerCase();

        try {
            if (userOS.contains("win")) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + githubURL);
            } else if (userOS.contains("mac")) {
                runtime.exec("open " + githubURL);
            } else if (userOS.indexOf("nix") >= 0 || userOS.indexOf("nux") >= 0) {
                runtime.exec("xdg-open " + githubURL);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
