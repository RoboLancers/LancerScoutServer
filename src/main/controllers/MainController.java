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

    @FXML
    private Label teamSummaryTotalMatches;

    @FXML
    private Label allianceSwitchAverage;

    @FXML
    private Label centerScaleAverage;

    @FXML
    private Label opponentSwitchAverage;

    @FXML
    private Label exchangeAverage;

    @FXML
    private Label crossAutoLinePercent;

    @FXML
    private Label robotBrokeDownPercent;

    @FXML
    private Label wrongSideAutoPercent;

    public volatile ObservableList<LancerTeam> teams = FXCollections.observableArrayList();
    public volatile FilteredList<LancerTeam> teamFilteredList = new FilteredList<>(teams);

    public static volatile ObservableMap<Integer, ObservableList<LancerMatch>> teamInfo = FXCollections.observableHashMap();

    private static boolean ran = false;

    public void initialize(){
        teamListView.getItems().addAll(teamFilteredList);
        teamListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        teamListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                updateTeamSummary(newValue);
            }else{
                detailTeamNumber.setText("");
                teamSummaryTotalMatches.setText("");

                allianceSwitchAverage.setText("");
                centerScaleAverage.setText("");
                opponentSwitchAverage.setText("");
                exchangeAverage.setText("");

                crossAutoLinePercent.setText("");
                robotBrokeDownPercent.setText("");
                wrongSideAutoPercent.setText("");
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

                        if(result.isPresent() && result.get() == ButtonType.OK){
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
                    return String.valueOf(team.getTeamNumber()).contains(newValue);
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
            LancerTeam team = null;

            for(LancerTeam currentTeam : teams){
                if(currentTeam.getTeamNumber() == change.getKey()){
                    team = currentTeam;
                }
            }

            if (change.wasAdded()) {
                if (!teamFilteredList.contains(team)) {
                    teams.add(new LancerTeam(change.getKey()));
                    teamListView.setItems(teams);
                }
            } else if (change.wasRemoved()) {
                if(team != null) {
                    if (teamFilteredList.contains(team)) {
                        teams.remove(team);
                        teamListView.setItems(teams);
                    }
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

        if(!ran) {
            WaitThread waitThread = new WaitThread();
            Thread thread = new Thread(waitThread);
            thread.start();

            ran = true;
        }
    }

    private void updateTeamSummary(LancerTeam newValue){
        int matchSize = teamInfo.get(newValue.getTeamNumber()).size();

        detailTeamNumber.setText(String.valueOf(newValue.getTeamNumber()));
        teamSummaryTotalMatches.setText(String.valueOf(matchSize));
        matchListView.setItems(teamInfo.get(newValue.getTeamNumber()));

        double allianceSwitch = 0, centerScale = 0, opponentSwitch = 0, exchange = 0, crossAutoLine = 0, robotBrokeDown = 0, wrongSideAuto = 0;

        if(matchSize > 0) {
            for(LancerMatch match : teamInfo.get(newValue.getTeamNumber())){
                allianceSwitch += match.getAllianceSwitch();
                centerScale += match.getCenterScale();
                opponentSwitch += match.getOpponentSwitch();
                exchange += match.getExchange();

                crossAutoLine += match.getCrossedAutoLine() ? 1 : 0;
                robotBrokeDown += match.getRobotBrokeDown() ? 1 : 0;
                wrongSideAuto += match.getWrongSideAuto() ? 1 : 0;
            }

            allianceSwitch /= matchSize;
            centerScale /= matchSize;
            opponentSwitch /= matchSize;
            exchange /= matchSize;

            crossAutoLine /= matchSize;
            robotBrokeDown /= matchSize;
            wrongSideAuto /= matchSize;

            crossAutoLine *= 100;
            robotBrokeDown *= 100;
            wrongSideAuto *= 100;
        }

        allianceSwitchAverage.setText(String.format("%.2f", allianceSwitch));
        centerScaleAverage.setText(String.format("%.2f", centerScale));
        opponentSwitchAverage.setText(String.format("%.2f", opponentSwitch));
        exchangeAverage.setText(String.format("%.2f", exchange));

        crossAutoLinePercent.setText(String.format("%.2f%s", crossAutoLine, "%"));
        robotBrokeDownPercent.setText(String.format("%.2f%s", robotBrokeDown, "%"));
        wrongSideAutoPercent.setText(String.format("%.2f%s", wrongSideAuto, "%"));
    }
}