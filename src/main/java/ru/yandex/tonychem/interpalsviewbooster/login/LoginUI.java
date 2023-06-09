package ru.yandex.tonychem.interpalsviewbooster.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.yandex.tonychem.interpalsviewbooster.InterpalsBoosterApplication;

import java.io.IOException;

public class LoginUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loginWindowLoader = new FXMLLoader(getClass().getResource("login-window.fxml"));
        Parent loginWindowRoot = loginWindowLoader.load();
        Scene loginScene = new Scene(loginWindowRoot, 343.0, 326.0);

        stage.setTitle("Interpals booster");
        stage.setResizable(false);

        Image icon = new Image(InterpalsBoosterApplication.class.getResource("logo.jpg").toExternalForm());
        stage.getIcons().add(icon);

        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}