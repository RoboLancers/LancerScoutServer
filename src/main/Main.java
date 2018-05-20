package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.MainController;
import main.utility.ProcessConnectionThread;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static Stage window;
    public static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                ArrayList<ProcessConnectionThread> threads = MainController.waitThread.getConnections();

                for(ProcessConnectionThread thread : threads){
                    thread.write("disconnect");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
        });

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
