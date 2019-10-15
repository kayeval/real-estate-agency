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
import main.model.UserDBModel;

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

    private UserDBModel userDBModel;
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
            id = userDBModel.canLogin(username.getText(), password.getText());
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
            switch (userDBModel.loginType(id)) {
                case "buyer":
                    mainController.setRegisteredUserType("buyer");
                    break;
                case "renter":
                    mainController.setRegisteredUserType("renter");
                    break;
                case "admin":
                    mainController.setRegisteredUserType("admin");
                    break;
                case "manager":
                    mainController.setRegisteredUserType("manager");
                    break;
                case "parttime":
                    mainController.setRegisteredUserType("parttime");
                    break;
                case "propertymanager":
                    mainController.setRegisteredUserType("propertymanager");
                    break;
                case "salesconsultant":
                    mainController.setRegisteredUserType("salesconsultant");
                    break;
                case "vendor":
                    mainController.setRegisteredUserType("vendor");
                    break;
                case "landlord":
                    mainController.setRegisteredUserType("landlord");
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
        mainController.setRegisteredUserType("guest");
        Scene nextScene = new Scene(nextPane);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(nextScene);
        window.show();
    }

    @FXML
    void registerAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/main/view/RegisterCustomer.fxml"));
        Parent nextPane = loader.load();
        Scene nextScene = new Scene(nextPane);
        nextScene.getStylesheets().add("/main/res/combobox.css");
        RegisterController controller = loader.getController();
        controller.setUserDBModel(userDBModel);

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

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }
}
