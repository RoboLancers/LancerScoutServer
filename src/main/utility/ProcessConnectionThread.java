package main.utility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import main.Main;
import main.controllers.MainController;
import main.models.match.LancerMatch;
import main.models.pit.LancerPit;

import javax.microedition.io.StreamConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class ProcessConnectionThread implements Runnable{
    private StreamConnection mConnection;

    private InputStream inputStream;
    private OutputStream outputStream;

    ProcessConnectionThread(StreamConnection connection) {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
            inputStream = mConnection.openInputStream();
            outputStream = mConnection.openOutputStream();
            System.out.println("Waiting for input");

            while (true) {
                String data = new String(readByteArrayCommand(inputStream));

                if(data.equals("")){
                    System.out.println("Exit");
                    break;
                }

                processCommand(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the command from client
     * @param command the command code
     */
    private void processCommand(int command) {
        switch (command) {
            default:
                System.out.println("Received " + command);
        }
    }

    /**
     * Process the command from client
     * @param data the data
     */
    private void processCommand(String data) {

        String command = "";
        Gson gson = new Gson();

        if(data.length() > 5){
            if(data.substring(0, 5).equals("MATCH")){
                command = data.substring(0, 5);
            }else if(data.substring(0, 3).equals("PIT")){
                command = data.substring(0, 3);
            }
        }

        switch (command) {
            case "MATCH":
                try {
                    String json = data.substring(5);
                    LancerMatch lancerMatch = gson.fromJson(json, LancerMatch.class);

                    if(!MainController.teamInfo.containsKey(lancerMatch.getTeamNumber())){
                        //Platform.runLater(() -> MainController.teamInfo.put(lancerMatch.getTeamNumber(), FXCollections.observableArrayList(lancerMatch)));
                        Platform.runLater(() -> MainController.teamInfo.put(lancerMatch.getTeamNumber(), new ArrayList<>(Collections.singletonList(lancerMatch))));
                    }else{
                        Platform.runLater(() -> {
                            //ObservableList<LancerMatch> currentMatches = MainController.teamInfo.remove(lancerMatch.getTeamNumber());
                            ArrayList<LancerMatch> currentMatches = MainController.teamInfo.remove(lancerMatch.getTeamNumber());

                            if(currentMatches.contains(lancerMatch)){
                                LancerMatch duplicate = null;

                                for (LancerMatch match : currentMatches) {
                                    if(match.equals(lancerMatch)){
                                        duplicate = match;
                                    }
                                }

                                if(duplicate != null) {
                                    Optional<ButtonType> result = AlertHelper.createDuplicateMatchAlert(duplicate, lancerMatch, Main.scene.getWindow()).showAndWait();
                                    if (result.isPresent() && result.get() == ButtonType.OK) {
                                        int index = currentMatches.indexOf(lancerMatch);
                                        currentMatches.remove(lancerMatch);
                                        currentMatches.add(index, lancerMatch);
                                    }

                                    MainController.teamInfo.put(lancerMatch.getTeamNumber(), currentMatches);
                                }
                            }else {
                                currentMatches.add(lancerMatch);
                                MainController.teamInfo.put(lancerMatch.getTeamNumber(), currentMatches);
                            }
                        });
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case "PIT":
                try {
                    String json = data.substring(3);
                    LancerPit lancerPit = gson.fromJson(json, LancerPit.class);

                    if (!MainController.pitInfo.containsKey(lancerPit.getTeamNumber())) {
                        Platform.runLater(() -> MainController.pitInfo.put(lancerPit.getTeamNumber(), lancerPit));
                    }else {
                        Platform.runLater(() -> {
                            MainController.pitInfo.remove(lancerPit.getTeamNumber());
                            MainController.pitInfo.put(lancerPit.getTeamNumber(), lancerPit);
                        });
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Received " + data);
        }
    }

    private byte[] readByteArrayCommand(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read = inputStream.read();

        while(read != -1 && read != 0){
            byteArrayOutputStream.write(read);
            read = inputStream.read();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public void write(int command) throws IOException {
        if(outputStream != null) {
            outputStream.write(command);
        }
    }

    public void write(String command) throws IOException {
        if(outputStream != null) {
            if (command != null && !command.isEmpty()) {
                outputStream.write(command.getBytes());
                outputStream.write(0);
            }
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
