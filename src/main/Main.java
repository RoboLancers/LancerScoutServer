package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.controllers.MainController;
import main.utility.ProcessConnectionThread;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(windowEvent -> handleClose());
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/drawable/Logo.jpg")));

        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/main.fxml"));

        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("resources/stylesheet/stylesheet.css").toExternalForm());

        primaryStage.setScene(scene);
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

    private void handleClose(){
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
    }
}
