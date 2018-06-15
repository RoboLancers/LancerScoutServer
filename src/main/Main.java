package main;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.controllers.MainController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            try {
                Gson gson = new Gson();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("resources/fxml/main.fxml"));
                loader.load();
                MainController mainController = loader.getController();

                BufferedWriter writer = new BufferedWriter(new FileWriter("teams.json"));
                writer.write(gson.toJson(mainController.teams));
                writer.flush();
                writer.close();

                writer = new BufferedWriter(new FileWriter("teamInfo.json"));
                writer.write(gson.toJson(MainController.teamInfo));
                writer.flush();
                writer.close();
            }catch (IOException e){
                System.out.println("Uh oh :( Something bad has happened!");
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void handleClose(){
        Platform.exit();
        System.exit(0);
    }
}
