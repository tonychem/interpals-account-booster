package ru.yandex.tonychem.interpalsviewbooster.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label incorrectCredentialsLabel;

    public void clickAuthorizeButton(ActionEvent event) {
        String login = loginTextField.getText();
        String password = passwordField.getText();

//        System.out.println(login + ":" + password);
//        incorrectCredentialsLabel.setVisible(true);
    }
}
