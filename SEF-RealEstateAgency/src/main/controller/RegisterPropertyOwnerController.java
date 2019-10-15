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
import main.model.DBModel.UserDBModel;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterPropertyOwnerController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button registerBtn;

    @FXML
    private Button btnBack;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label error;

    private UserDBModel userDBModel;

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    @FXML
    void initialize() {
        cmbType.getItems().clear();
        cmbType.getItems().addAll("Selling", "Leasing");
        cmbType.getSelectionModel().selectFirst();

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
    }

    @FXML
    void registerAction(ActionEvent event) {
        boolean valid = true, canRegister = false;

        if (emailField.getText().equals("") || passwordField.getText().equals("") || usernameField.getText().equals("")) {
            error.setText("Please fill up all fields.");
            valid = false;
        }

        if (valid) {
            if (!userDBModel.isUniqueUsername(usernameField.getText()))
                error.setText("That username is already taken.");
            else if (!userDBModel.isValidEmailFormat(emailField.getText())) {
                error.setText("Invalid email format");
            } else if (!userDBModel.isValidUsernameFormat(usernameField.getText())) {
                error.setText("Invalid username format");
            } else canRegister = true;
        }

        if (canRegister) {
            //add to user db
            try {
                userDBModel.registerPropertyOwner(usernameField.getText(), emailField.getText(), passwordField.getText(),
                        cmbType.getSelectionModel().getSelectedItem().equals("Selling"));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            btnBack.fire();
        }
    }

    @FXML
    void goToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
        Parent nextPane = loader.load();
        LoginController controller = loader.getController();
        controller.setUserDBModel(userDBModel);
        Scene nextScene = new Scene(nextPane);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(nextScene);
        window.show();
    }
}
