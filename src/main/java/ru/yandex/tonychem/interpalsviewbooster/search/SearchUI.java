package ru.yandex.tonychem.interpalsviewbooster.search;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.yandex.tonychem.interpalsviewbooster.InterpalsBoosterApplication;
import ru.yandex.tonychem.interpalsviewbooster.beans.BeansHolder;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

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
        stage.setTitle("Interpals View Booster");
        stage.setResizable(false);

        Image icon = new Image(InterpalsBoosterApplication.class.getResourceAsStream("logo.png"));
        stage.getIcons().add(icon);

        stage.setOnCloseRequest((event) -> {
            exit(stage);
            event.consume();
        });

        stage.setScene(searchScene);
        stage.show();
    }

    public static void exit(Stage stage) {
        Alert alert = AppUtils.alert();
        if (alert.showAndWait().get() == ButtonType.OK) {
            BeansHolder.sessionCacheManager.flush();
            stage.close();
            System.exit(0);
        }
    }
}
