package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controller.LoginController;
import main.controller.UserController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        UserController userController = new UserController();
        controller.setUserController(userController);

//        userController.registerEmployee("admin", "a@a.com", "1234", 55000, LocalDate.now(ZoneId.systemDefault()), "admin");
//        userController.registerEmployee("manager", "a@a.com", "1234", 55000, LocalDate.now(ZoneId.systemDefault()), "manager");
//        userController.registerEmployee("sales", "a@a.com", "1234", 55000, LocalDate.now(ZoneId.systemDefault()), "propertymanager");
//        userController.registerEmployee("sales-r", "a@a.com", "1234", 55000, LocalDate.now(ZoneId.systemDefault()), "salesconsultant");
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