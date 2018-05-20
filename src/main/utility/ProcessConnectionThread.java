package main.utility;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import javafx.application.Platform;
import main.controllers.MainController;
import main.models.LancerMatch;

import javax.microedition.io.StreamConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ProcessConnectionThread implements Runnable{
    private StreamConnection mConnection;
    private byte[] byteCommand;

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
                byteCommand = readByteArrayCommand(inputStream);

                String command = new String(byteCommand);

                if (command.equals("")) {
                    System.out.println("Exit Command Received. Finished");
                    break;
                }

                processCommand(command);
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
     * @param command the command code
     */
    private void processCommand(String command) {
        switch (command) {
            case "MATCH":
                try {
                    byteCommand = readByteArrayCommand(inputStream);
                    String json = new String(byteCommand);
                    System.out.println(json);
                    Gson gson = new Gson();
                    LancerMatch lancerMatch = gson.fromJson(json, LancerMatch.class);

                    if(MainController.teamInfo.get(lancerMatch.getTeamNumber()) == null){
                        Platform.runLater(() -> MainController.teamInfo.put(lancerMatch.getTeamNumber(), new ArrayList<>(Collections.singletonList(lancerMatch))));
                    }else{
                        Platform.runLater(() -> {
                            ArrayList<LancerMatch> currentMatches = MainController.teamInfo.get(lancerMatch.getTeamNumber());
                            currentMatches.add(lancerMatch);
                            MainController.teamInfo.put(lancerMatch.getTeamNumber(), currentMatches);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Received " + command);
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
