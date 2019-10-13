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
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class EditUserController {

    @FXML
    private Label error;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField suburbsField;

    @FXML
    private TextField occupationField;

    @FXML
    private TextField incomeField;

    @FXML
    private Button saveBtn;

    private UserController userController;
    private String username;
    private String email;
    private String suburbs;
    private String occupation;
    private double income;
    private boolean isBuyer;

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
                saveBtn.fire();
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        suburbsField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        incomeField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        occupationField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        TextFormatter formatter = new TextFormatter(new DecimalFilter());
        incomeField.setTextFormatter(formatter);
    }

    public void start() {
        usernameField.setText(username);
        emailField.setText(email);
        suburbsField.setText(suburbs);

        occupationField.setDisable(false);
        incomeField.setDisable(false);

        if (isBuyer) {
            occupationField.setDisable(true);
            incomeField.setDisable(true);
        } else {
            occupationField.setText(occupation);
            incomeField.setText(income + "");
        }
    }

    @FXML
    void saveAction(ActionEvent event) throws IOException {
        boolean valid = true, canSave = false;

        if (emailField.getText().equals("") || usernameField.getText().equals("") || suburbsField.getText().equals("") ||
                (!isBuyer && (occupationField.getText().equals("") || incomeField.getText().equals("")))) {
            error.setText("Please fill up all fields.");
            valid = false;
        } else if (!incomeField.getText().equals("") && Double.parseDouble(incomeField.getText()) < 0) {
            error.setText("Income cannot be negative.");
            valid = false;
        }

        if (valid) {
            if (!usernameField.getText().equals(username) && !userController.isUniqueUsername(usernameField.getText()))
                error.setText("That username is already taken.");
            else if (!userController.isValidEmailFormat(emailField.getText())) {
                error.setText("Invalid email format");
            } else if (!userController.isValidUsernameFormat(usernameField.getText())) {
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
            passwordPromptController.setUserController(userController);
            passwordPromptController.hasConfirmedProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                if (isConfirmed) {
                    confirmChangeStage.hide();
                    setSaved(true);

                    try {
                        userController.updateCustomerDetails(usernameField.getText(), emailField.getText(),
                                occupationField.getText(), incomeField.getText(), Arrays.asList(suburbsField.getText().split("\\s*,\\s*")), username);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            confirmChangeStage.setScene(scene);
            confirmChangeStage.show();
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setSuburbs(String suburbs) {
        this.suburbs = suburbs;
    }

    public void setBuyer(boolean buyer) {
        isBuyer = buyer;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
