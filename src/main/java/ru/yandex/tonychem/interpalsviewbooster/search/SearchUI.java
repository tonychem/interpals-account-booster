package ru.yandex.tonychem.interpalsviewbooster.search;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.yandex.tonychem.interpalsviewbooster.login.LoginController;

import java.io.IOException;

public class SearchUI {
    public static void renderWindow() {
        FXMLLoader searchWindowLoader = new FXMLLoader(SearchUI.class.getResource("search-window.fxml"));

        Parent searchWindowRoot = null;

        try {
            searchWindowRoot = searchWindowLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("SearchUI has encountered error while loading nodes");
        }

        Scene searchScene = new Scene(searchWindowRoot, 887.0, 400.0);

        Stage stage = new Stage();
        stage.setTitle("Interpals booster");
        stage.setResizable(false);

        Image icon = new Image(SearchUI.class.getResource("penpals.jpg").toExternalForm());
        stage.getIcons().add(icon);

        stage.setScene(searchScene);
        stage.show();
    }
}
