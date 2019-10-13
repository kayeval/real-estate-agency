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
    private DBConnector conn;
    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        conn = new DBConnector();
        mainController = new MainController();
        mainController.setDbConnector(conn);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
        Parent root = loader.load();

        UserController userController = new UserController();
        userController.setMainController(mainController);
        mainController.setUserController(userController);

        LoginController controller = loader.getController();
        controller.setUserController(userController);

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