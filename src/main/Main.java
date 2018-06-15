package main;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.controllers.MainController;
import main.utility.AlertHelper;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static Scene scene;
    private Gson gson = new Gson();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(this::handleClose);
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

        setUpSave();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void handleClose(Event windowEvent){
        Optional<ButtonType> result = AlertHelper.createAlert(Alert.AlertType.CONFIRMATION, scene.getWindow(), "Confirm Close", "Confirm close? All data will be lost!").showAndWait();

        if(result.get() == ButtonType.OK){
            Platform.exit();
            System.exit(0);
        }else{
            windowEvent.consume();
        }
    }

    private void setUpSave(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            try {
                save();

                Platform.runLater(() -> Notifications.create().text("Autosaved").showInformation());
            }catch (IOException e){
                System.out.println("Uh oh :( Something bad has happened!");
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    private void save() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("teams.json"));
        writer.write(gson.toJson(MainController.teams));
        writer.flush();
        writer.close();

        writer = new BufferedWriter(new FileWriter("teamInfo.json"));
        writer.write(gson.toJson(MainController.teamInfo));
        writer.flush();
        writer.close();
    }
}
