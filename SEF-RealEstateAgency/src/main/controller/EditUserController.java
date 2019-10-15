package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.model.DBModel.UserDBModel;

import java.io.IOException;

public class EditUserController {

    @FXML
    private Label error;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button saveBtn;

    private UserDBModel userDBModel;
    private String username;
    private String email;

    private final BooleanProperty hasSaved = new SimpleBooleanProperty();

    public BooleanProperty hasSavedProperty() {
        return hasSaved;
    }

    public final boolean isSaved() {
        return hasSavedProperty().get();
    }

    public final void setSaved(boolean saved) {
        hasSavedProperty().set(saved);
    }

    @FXML
    void initialize() {
        usernameField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        emailField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });
    }

    @FXML
    void saveAction(ActionEvent event) throws IOException {
        boolean valid = true, canSave = false;

        if (emailField.getText().equals("") || usernameField.getText().equals("")) {
            error.setText("Please fill up all fields.");
            valid = false;
        }

        if (valid) {
            if (!usernameField.getText().equals(username) && !userDBModel.isUniqueUsername(usernameField.getText()))
                error.setText("That username is already taken.");
            else if (!userDBModel.isValidEmailFormat(emailField.getText())) {
                error.setText("Invalid email format");
            } else if (!userDBModel.isValidUsernameFormat(usernameField.getText())) {
                error.setText("Invalid username format");
            } else canSave = true;
        }

        if (canSave) {
            Stage confirmChangeStage = new Stage();
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/view/PasswordPrompt.fxml"));
            AnchorPane rootLayout = loader.load();

            PasswordPromptController passwordPromptController = loader.getController();
            passwordPromptController.setUsername(username);
            passwordPromptController.setUserDBModel(userDBModel);
            passwordPromptController.hasConfirmedProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                if (isConfirmed) {
                    confirmChangeStage.hide();
                    setSaved(true);

                    userDBModel.updateUserDetails(usernameField.getText(), emailField.getText(), username);
                }
            });

            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            confirmChangeStage.setScene(scene);
            confirmChangeStage.show();
        }
    }

    public void start() {
        usernameField.setText(username);
        emailField.setText(email);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }
}
