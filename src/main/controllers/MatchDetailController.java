package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import main.Main;

public class MatchDetailController {

    @FXML
    private Label detailMatchNumber;

    @FXML
    private MenuItem detailMenuBack;

    public void initialize(){
        detailMenuBack.setOnAction(event -> Main.window.setScene(Main.mainScene));
    }

    void setMatchNumber(int matchNumber){
        detailMatchNumber.setText(String.valueOf(matchNumber));
    }
}
