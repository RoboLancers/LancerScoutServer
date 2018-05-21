package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;
import main.models.LancerMatch;
import main.models.LancerTeam;
import main.utility.AlertHelper;
import main.utility.WaitThread;

import java.util.*;

public class MainController {

    @FXML
    private Button addTeamButton;

    @FXML
    private TextField teamNumberField;

    @FXML
    private ListView<LancerTeam> teamListView;

    @FXML
    private Label detailTeamNumber;

    @FXML
    private TextField searchTeamField;

    @FXML
    private ListView<LancerMatch> matchListView;

    private ObservableList<LancerTeam> teams = FXCollections.observableArrayList();
    private FilteredList<LancerTeam> teamFilteredList = new FilteredList<>(teams);

    public static volatile ObservableMap<Integer, ObservableList<LancerMatch>> teamInfo = FXCollections.observableHashMap();

    public static WaitThread waitThread;

    public void initialize(){
        teamListView.getItems().addAll(teamFilteredList);
        teamListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        teamListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                detailTeamNumber.setText(String.valueOf(newValue.getTeamNumber()));
                matchListView.setItems(teamInfo.get(newValue.getTeamNumber()));
            }else{
                detailTeamNumber.setText("");
            }
        });

        teamListView.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.DELETE)){
                    final int selectedIdx = teamListView.getSelectionModel().getSelectedIndex();

                    if(selectedIdx != -1) {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you sure you want to delete");
                        Optional<ButtonType> result = alert.showAndWait();

                        if(result.get() == ButtonType.OK){
                            ObservableList<LancerTeam> selectedTeams = teamListView.getSelectionModel().getSelectedItems();
                            for(LancerTeam team : selectedTeams){
                                teamInfo.remove(team.getTeamNumber());
                            }
                            teamListView.getItems().removeAll(selectedTeams);
                            teamListView.getSelectionModel().select(selectedIdx == teamListView.getItems().size() - 1 ? selectedIdx - 1 : selectedIdx);
                        }else{
                            alert.close();
                        }
                    }
                }
        });

        teamNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                teamNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        searchTeamField.textProperty().addListener((observable, oldValue, newValue) -> {
            teamFilteredList.setPredicate(team -> {
                if(newValue != null && !newValue.isEmpty()){
                    return team.getTeamName().toLowerCase().contains(newValue.toLowerCase());
                }else{
                    return true;
                }
            });

            teamListView.setItems(teamFilteredList);
        });

        matchListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                LancerMatch match = matchListView.getSelectionModel().getSelectedItem();

                if(match != null){
                    Window owner = matchListView.getScene().getWindow();
                    AlertHelper.showMatchAlert(match, owner);
                }
            }
        });

        teamInfo.addListener((MapChangeListener<Integer, ObservableList<LancerMatch>>) change -> {
            if(change.wasAdded()){
                if(!teamFilteredList.contains(change.getKey())){
                    teams.add(new LancerTeam(change.getKey()));
                    teamListView.setItems(teams);
                }
            }
        });

        addTeamButton.setOnMouseClicked(event -> {
            Window owner = addTeamButton.getScene().getWindow();
            String teamNumberFieldText = teamNumberField.getText();

            if(teamNumberFieldText.isEmpty()){
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter team number");
                return;
            }

            int teamNumber = Integer.parseInt(teamNumberFieldText);
            if(teamInfo.containsKey(teamNumber)){
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner,"Form Error", "Team already exists!");
                return;
            }

            teamInfo.put(teamNumber, FXCollections.observableArrayList());

            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Team Successfully Added", teamNumberField.getText() + " successfully added");
            teamNumberField.clear();
        });

        waitThread = new WaitThread();
        Thread thread = new Thread(waitThread);
        thread.start();
    }
}