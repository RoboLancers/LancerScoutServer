package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage window;
    public static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("fxmls/main.fxml"));
        mainScene = new Scene(root);

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Lancer Scout Server");

        primaryStage.setWidth(1155);
        primaryStage.setHeight(820);
        primaryStage.centerOnScreen();

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
