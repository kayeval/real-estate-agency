package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label error;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private Text registerTxt;

    private UserController userController;
    private MainController mainController;

    @FXML
    void loginAction(ActionEvent event) throws IOException {
        boolean valid = true, found = false;

        if (username.getText().equals("") || password.getText().equals("")) {
            error.setText("Please fill up all fields.");
            valid = false;
        }

        if (valid) {
            if (!userController.canLogin(username.getText(), password.getText()))
                error.setText("Invalid username or password.");
            else found = true;
        }

        if (found) {
            //go to main panel
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/main/view/Main.fxml"));
            Parent nextPane = loader.load();
            Scene nextScene = new Scene(nextPane);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(nextScene);
            window.show();
        }
    }

    @FXML
    void registerAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/main/view/Register.fxml"));
        Parent nextPane = loader.load();
        Scene nextScene = new Scene(nextPane);
        nextScene.getStylesheets().add("/main/res/combobox.css");
        RegisterController controller = loader.getController();
        controller.setUserController(userController);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(nextScene);
        window.show();
    }

    @FXML
    void initialize() {
        username.textProperty().addListener((observable) -> {
            error.setText("");
        });

        password.textProperty().addListener((observable) -> {
            error.setText("");
        });

        username.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginBtn.fire();
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginBtn.fire();
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
