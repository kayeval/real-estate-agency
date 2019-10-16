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
import main.model.DecimalFilter;

import java.io.IOException;
import java.util.Arrays;

public class RegisterCustomerController {

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
    private TextField suburbsField;

    @FXML
    private TextField occupationField;

    @FXML
    private TextField incomeField;

    @FXML
    private Button registerBtn;

    @FXML
    private Button btnBack;

    private UserDBModel userDBModel;
    private boolean isBuyer = true;

    @FXML
    void goToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
        Parent nextPane = loader.load();
        LoginController controller = loader.getController();
        controller.setUserDBModel(userDBModel);
        Scene nextScene = new Scene(nextPane);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Real Estate Agency");
        window.setScene(nextScene);
        window.show();
    }

    @FXML
    void registerAction(ActionEvent event) {
        boolean valid = true, canRegister = false;

        if (emailField.getText().equals("") || passwordField.getText().equals("") || usernameField.getText().equals("") ||
                suburbsField.getText().equals("") || (!isBuyer && (occupationField.getText().equals("") || incomeField.getText().equals("")))) {
            error.setText("Please fill up all fields.");
            valid = false;
        } else if (!incomeField.getText().equals("") && Double.parseDouble(incomeField.getText()) < 0) {
            error.setText("Income cannot be negative.");
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
            userDBModel.registerCustomer(usernameField.getText(), emailField.getText(), passwordField.getText(),
                    occupationField.getText(), incomeField.getText(), Arrays.asList(suburbsField.getText().split("\\s*,\\s*")));

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

        suburbsField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        occupationField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        incomeField.textProperty().addListener((observable) -> {
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

        suburbsField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                registerBtn.fire();
            }
        });

        occupationField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                registerBtn.fire();
            }
        });

        incomeField.setOnKeyPressed(event -> {
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

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    public UserDBModel getUserDBModel() {
        return userDBModel;
    }
}
