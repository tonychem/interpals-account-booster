package ru.yandex.tonychem.interpalsviewbooster.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {
    @FXML
    private Label incorrectCredentialsLabel;
    public void clickAuthorizeButton(ActionEvent event) {
        incorrectCredentialsLabel.setVisible(true);
    }
}
