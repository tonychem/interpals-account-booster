package ru.yandex.tonychem.interpalsviewbooster.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.yandex.tonychem.interpalsviewbooster.beans.BeansHolder;
import ru.yandex.tonychem.interpalsviewbooster.engine.CacheManager;
import ru.yandex.tonychem.interpalsviewbooster.engine.CrawlEngine;
import ru.yandex.tonychem.interpalsviewbooster.engine.exception.IncorrectCredentialsException;
import ru.yandex.tonychem.interpalsviewbooster.search.SearchUI;
import ru.yandex.tonychem.interpalsviewbooster.util.AppUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private CrawlEngine engine = BeansHolder.engine();

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label incorrectCredentialsLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Label fieldsEmptyLabel;

    @FXML
    private Label connectionErrorLabel;

    public void clickAuthorizeButton(ActionEvent event) {
        resetInvisibleNodes();

        String login = loginTextField.getText();
        String password = passwordField.getText();

        if (login.isBlank() || password.isBlank()) {
            fieldsEmptyLabel.setVisible(true);
            return;
        }

        try {
            engine.authorize(login, password);
        } catch (IncorrectCredentialsException e) {
            incorrectCredentialsLabel.setVisible(true);
            return;
        } catch (IOException e) {
            connectionErrorLabel.setVisible(true);
            return;
        }

        BeansHolder.sessionCacheManager = new CacheManager(login);
        AppUtils.closeWindow(event);
        Platform.runLater(SearchUI::renderWindow);
    }

    private void resetInvisibleNodes() {
        incorrectCredentialsLabel.setVisible(false);
        fieldsEmptyLabel.setVisible(false);
        connectionErrorLabel.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setDefaultButton(true);
    }
}
