package ru.yandex.tonychem.interpalsviewbooster.about;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.yandex.tonychem.interpalsviewbooster.InterpalsBoosterApplication;
import ru.yandex.tonychem.interpalsviewbooster.search.SearchUI;

import java.io.IOException;

public class AboutUI {

    public static void renderWindow() {
        FXMLLoader aboutWindowLoader = new FXMLLoader(AboutUI.class.getResource("about-window.fxml"));

        Parent aboutWindowRoot = null;

        try {
            aboutWindowRoot = aboutWindowLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("AboutUI has encountered error while loading nodes");
        }

        Scene aboutScene = new Scene(aboutWindowRoot, 372.0, 235.0);

        Stage stage = new Stage();
        stage.setTitle("About Interpals View Booster");
        stage.setResizable(false);

        Image icon = new Image(InterpalsBoosterApplication.class.getResourceAsStream("logo.png"));
        stage.getIcons().add(icon);

        stage.setScene(aboutScene);
        stage.show();
    }
}
