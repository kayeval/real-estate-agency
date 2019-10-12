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
import main.model.User.User;

import java.io.IOException;
import java.sql.SQLException;

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
    private TextField occupationField;

    @FXML
    private TextField incomeField;

    @FXML
    private Button registerBtn;

    @FXML
    private Button btnBack;

    private UserController userController;
    private boolean isBuyer = true;

    @FXML
    void goBack(ActionEvent event) throws IOException {
        //go back to login
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
        Parent nextPane = loader.load();
        Scene nextScene = new Scene(nextPane);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(nextScene);
        window.show();
    }

    @FXML
    void registerAction(ActionEvent event) {
        boolean valid = true, canRegister = false;

        if (emailField.getText().equals("") || passwordField.getText().equals("") || usernameField.getText().equals("") ||
                (!isBuyer && (occupationField.getText().equals("") || incomeField.getText().equals("")))) {
            error.setText("Please fill up all fields.");
            valid = false;
        }

        if (valid) {
            if (!userController.isUnique(usernameField.getText()))
                error.setText("That username is already taken.");
            else if (!userController.isValidEmailFormat(emailField.getText())) {
                error.setText("Invalid email format");
            } else if (!userController.isValidUsernameFormat(usernameField.getText())) {
                error.setText("Invalid username format");
            } else canRegister = true;
        }

        if (canRegister) {
            //add to user db
            try {
                userController.registerUser(usernameField.getText(), emailField.getText(), passwordField.getText(), occupationField.getText(), incomeField.getText());

                System.out.println("REGISTER SUCCESS, USERS=");
                for (User u : userController.getCustomers().values())
                    System.out.println(u.getUsername());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            btnBack.fire();
        }
    }

    @FXML
    void initialize() {
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
        TextFormatter formatter = new TextFormatter(new DecimalFilter());
        incomeField.setTextFormatter(formatter);

        cmbType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue.equals("Renting")) {
                isBuyer = false;

                //enable occupation and income fields
                occupationField.setDisable(false);
                incomeField.setDisable(false);
            } else {
                isBuyer = true;

                //disable occupation and income fields
                occupationField.setText("");
                occupationField.setDisable(true);

                incomeField.setText("");
                incomeField.setDisable(true);
            }
        });
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public UserController getUserController() {
        return userController;
    }
}
