package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private Label error;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<?> cmbType;

    @FXML
    private Button registerBtn;

    private UserController userController = new UserController();

    @FXML
    void registerAction(ActionEvent event) throws IOException {
        boolean valid = true, canRegister = false;

        if (emailField.getText().equals("") || passwordField.getText().equals("") || usernameField.getText().equals("")
                || cmbType.getSelectionModel().getSelectedItem().toString().equals("Buying/Renting")) {
            error.setText("Please fill up all fields.");
            valid = false;
        }

        if (valid) {
            if (!userController.isUnique(usernameField.getText()))
                error.setText("That username is already taken.");
            else if (!userController.isValidEmailFormat(emailField.getText())) {
                error.setText("Invalid username format");
            } else if (!userController.isValidUsernameFormat(usernameField.getText())) {
                error.setText("Invalid email format");
            } else canRegister = true;
        }

        if (canRegister) {
            //add to user db
            userController.registerUser(usernameField.getText(), emailField.getText(), passwordField.getText());

            //add to customer db


            //go back to login
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
            Parent nextPane = loader.load();
            Scene nextScene = new Scene(nextPane);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(nextScene);
            window.show();
        }
    }

    @FXML
    void initialize() {
        usernameField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        emailField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        passwordField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                registerBtn.fire();
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                registerBtn.fire();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                registerBtn.fire();
            }
        });

        cmbType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            //get occupation and income from Renters
            if (newValue.equals("Renting")) {
                System.out.println("RENTING!!!");

            } else {
                System.out.println("HIDE?");
            }
        });
    }

}
