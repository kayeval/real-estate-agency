package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

public class PasswordPromptController {

    @FXML
    private Label error;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button confirmBtn;

    private final BooleanProperty hasConfirmed = new SimpleBooleanProperty();
    private UserController userController;
    private String username;

    @FXML
    void initialize() {
        passwordField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                confirmBtn.fire();
            }
        });
    }

    public BooleanProperty hasConfirmedProperty() {
        return hasConfirmed;
    }

    public final boolean isConfirmed() {
        return hasConfirmedProperty().get();
    }

    public final void setHasConfirmed(boolean saved) {
        hasConfirmedProperty().set(saved);
    }

    @FXML
    void confirmAction(ActionEvent event) {
        boolean valid = true;
        if (passwordField.getText().equals("")) {
            error.setText("Please enter your password.");
            valid = false;
        }

        if (valid) {
            int id = userController.canLogin(username, passwordField.getText());
            if (id != 0) setHasConfirmed(true);
            else error.setText("Can't save changes.");
        }

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
