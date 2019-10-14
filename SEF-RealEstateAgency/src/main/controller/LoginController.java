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

    @FXML
    private Text guestLoginTxt;

    private UserController userController;
    private MainController mainController;

    @FXML
    void loginAction(ActionEvent event) throws IOException {
        boolean valid = true, found = false;
        int id = 0;

        if (username.getText().equals("") || password.getText().equals("")) {
            error.setText("Please fill up all fields.");
            valid = false;
        }

        if (valid) {
            id = userController.canLogin(username.getText(), password.getText());
            if (id == 0)
                error.setText("Invalid username or password.");
            else found = true;
        }

        if (found) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/view/Main.fxml"));
            Parent nextPane = loader.load();

            MainController mainController = loader.getController();
            mainController.setUserID(id);
            switch (userController.loginType(id)) {
                case "buyer":
                    mainController.setType("buyer");
                    break;
                case "renter":
                    mainController.setType("renter");
                    break;
                case "admin":
                    mainController.setType("admin");
                    break;
                case "manager":
                    mainController.setType("manager");
                    break;
                case "parttime":
                    mainController.setType("parttime");
                    break;
                case "propertymanager":
                    mainController.setType("propertymanager");
                    break;
                case "salesconsultant":
                    mainController.setType("salesconsultant");
                case "vendor":
                    mainController.setType("vendor");
                    break;
                case "landlord":
                    mainController.setType("landlord");
                    break;
                default:
            }

            Scene nextScene = new Scene(nextPane);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(nextScene);
            window.show();
        }
    }

    @FXML
    void guestLoginAction(MouseEvent event) throws IOException {
        //todo
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Main.fxml"));
        Parent nextPane = loader.load();

        MainController mainController = loader.getController();
//        mainController.setUserID(id);
        mainController.setType("guest");
        Scene nextScene = new Scene(nextPane);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(nextScene);
        window.show();
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
}
