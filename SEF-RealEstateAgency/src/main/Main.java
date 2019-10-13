package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controller.LoginController;
import main.controller.MainController;
import main.controller.UserController;
import main.model.DBConnector;

public class Main extends Application {
    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainController = new MainController();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
        Parent root = loader.load();

        UserController userController = new UserController();
        userController.setMainController(mainController);

        LoginController controller = loader.getController();
        controller.setUserController(userController);

//        userController.registerPropertyOwner("p1", "vendor@a.com", "1234", true);
//        userController.registerPropertyOwner("p2", "buyer@a.com", "1234", false);

        primaryStage.setTitle("Real Estate Agency");
        Scene scene = new Scene(root, 800, 760);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("/main/res/login.css");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}