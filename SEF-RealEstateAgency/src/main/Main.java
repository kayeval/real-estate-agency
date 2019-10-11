package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main/view/Login.fxml"));
        primaryStage.setTitle("Real Estate Agency");
        Scene scene = new Scene(root, 800, 540);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("/main/res/login.css");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}