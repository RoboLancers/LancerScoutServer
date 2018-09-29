package main.controllers;
// vincent sucks ass
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import main.Main;
import main.models.match.LancerMatch;
import main.models.match.LancerTeam;
import main.models.pit.LancerPit;
import main.utility.AlertHelper;
import main.utility.WaitThread;
import org.controlsfx.control.Notifications;
// wsp bchen
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MainController {

    public static volatile ObservableList<LancerTeam> teams = FXCollections.observableArrayList();
    private static volatile FilteredList<LancerTeam> teamFilteredList = new FilteredList<>(teams);
    public static volatile ObservableMap<Integer, ArrayList<LancerMatch>> teamInfo = FXCollections.observableHashMap();
    public static volatile ObservableMap<Integer, LancerPit> pitInfo = FXCollections.observableHashMap();
    private static volatile ObservableMap<Integer, Image> robotImages = FXCollections.observableHashMap();
    private static WaitThread waitThread;

    @FXML
    private MenuItem menuOpen;
    @FXML
    private MenuItem menuSave;
    @FXML
    private MenuItem menuClose;
    @FXML
    private Button uploadPicturesButton;
    @FXML
    private Button imageChooserButton;
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
    @FXML
    private Label drivetrainLabel;
    @FXML
    private Label cubeIntakeLabel;
    @FXML
    private Label climbLabel;
    @FXML
    private Label robotWeightLabel;
    @FXML
    private Label programmingLanguageLabel;
    @FXML
    private ImageView robotImage;
    @FXML
    private Button sortByButton;

    private Image image;

    public void initialize() {
        teamListView.getItems().addAll(teamFilteredList);
        teamListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        teamListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateTeamSummary(newValue);
            } else {
                detailTeamNumber.setText("");
                teamSummaryTotalMatches.setText("");

                allianceSwitchAverage.setText("");
                centerScaleAverage.setText("");
                opponentSwitchAverage.setText("");
                exchangeAverage.setText("");

                crossAutoLinePercent.setText("");
                robotBrokeDownPercent.setText("");
                wrongSideAutoPercent.setText("");

                drivetrainLabel.setText("");
                cubeIntakeLabel.setText("");
                climbLabel.setText("");
                robotWeightLabel.setText("");
                programmingLanguageLabel.setText("");

                drivetrainLabel.setText("");
                cubeIntakeLabel.setText("");
                climbLabel.setText("");
                robotWeightLabel.setText("");
                programmingLanguageLabel.setText("");

                robotImage.setImage(null);
            }
        });

        teamListView.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                final int selectedIdx = teamListView.getSelectionModel().getSelectedIndex();

                if (selectedIdx != -1) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        LancerTeam selectedTeams = teamListView.getSelectionModel().getSelectedItem();

                        teamInfo.remove(selectedTeams.getTeamNumber());
                        pitInfo.remove(selectedTeams.getTeamNumber());
                        robotImages.remove(selectedTeams.getTeamNumber());
                        teamFilteredList.remove(selectedTeams);

                        teamListView.getSelectionModel().clearSelection();
                    } else {
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
                if (newValue != null && !newValue.isEmpty()) {
                    return String.valueOf(team.getTeamNumber()).contains(newValue);
                } else {
                    return true;
                }
            });

            teamListView.setItems(teamFilteredList);
        });

        matchListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                LancerMatch match = matchListView.getSelectionModel().getSelectedItem();

                if (match != null) {
                    Window owner = matchListView.getScene().getWindow();
                    AlertHelper.showMatchAlert(match, owner);
                }
            }
        });

        teamInfo.addListener((MapChangeListener<Integer, ArrayList<LancerMatch>>) change -> {
            LancerTeam team = null;

            for (LancerTeam currentTeam : teams) {
                if (currentTeam.getTeamNumber() == change.getKey()) {
                    team = currentTeam;
                }
            }

            if (change.wasAdded()) {
                if (!teams.contains(team)) {
                    teams.add(new LancerTeam(change.getKey()));
                    teamListView.setItems(teams);
                }
            } else if (change.wasRemoved()) {
                if (team != null) {
                    if (teamFilteredList.contains(team)) {
                        teams.remove(team);
                        teamListView.setItems(teams);
                    }
                }
            }
        });

        pitInfo.addListener((MapChangeListener<Integer, LancerPit>) change -> {
            LancerTeam team = null;

            for (LancerTeam currentTeam : teams) {
                if (currentTeam.getTeamNumber() == change.getKey()) {
                    team = currentTeam;
                }
            }

            if(change.wasAdded()){
                if(!teams.contains(team)){
                    teams.add(new LancerTeam(change.getKey()));
                    teamListView.setItems(teams);
                    teamListView.getSelectionModel().clearSelection();
                }
            }else if (change.wasRemoved()) {
                System.out.println("Attempting Removal");
                if (team != null) {
                    if (teamFilteredList.contains(team)) {
                        teams.remove(team);
                        teamListView.setItems(teams);
                    }
                }
            }
        });

        imageChooserButton.setOnAction(event -> {
            Node source = (Node) event.getSource();

            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(source.getScene().getWindow());
            if(file != null){
                image = new Image(file.toURI().toString());
            }
        });

        uploadPicturesButton.setOnAction(event -> {
            if(image == null){
                Node source = (Node) event.getSource();
                AlertHelper.showAlert(Alert.AlertType.ERROR, source.getScene().getWindow(), "Add Image Error!", "Please select an image to upload!");
                return;
            }

            robotImages.put(Integer.parseInt(teamNumberField.getText()), image);
            teamNumberField.clear();
            image = null;
        });

        sortByButton.setOnAction(event -> {

        });

        menuSave.setOnAction(event -> {
            try {
                Main.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuClose.setOnAction(Main::handleClose);

        menuOpen.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Team Info save files (*.json)", "teamInfo.json");
            fileChooser.getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(null);
            if(file != null){
                try {
                    Scanner scanner = new Scanner(file);

                    StringBuilder jsonBuilder = new StringBuilder();

                    while(scanner.hasNextLine()){
                        jsonBuilder.append(scanner.nextLine());
                    }

                    HashMap<Integer, ArrayList<LancerMatch>> teamMatches = new Gson().fromJson(jsonBuilder.toString(), new TypeToken<HashMap<Integer, ObservableList<LancerMatch>>>(){}.getType());
                    teamInfo = FXCollections.observableMap(teamMatches);
                } catch (FileNotFoundException e) {
                    Platform.runLater(() -> Notifications.create().text("File not found").showInformation());
                }
            }
        });

        if (waitThread == null) {
            waitThread = new WaitThread();
            Thread thread = new Thread(waitThread);
            thread.start();
        }
    }

    private void updateTeamSummary(LancerTeam newValue) {
        if(teamInfo.containsKey(newValue.getTeamNumber())) {
            int matchSize = teamInfo.get(newValue.getTeamNumber()).size();

            detailTeamNumber.setText(String.valueOf(newValue.getTeamNumber()));
            teamSummaryTotalMatches.setText(String.valueOf(matchSize));
            //matchListView.setItems(teamInfo.get(newValue.getTeamNumber()));
            matchListView.setItems(FXCollections.observableArrayList(teamInfo.get(newValue.getTeamNumber())));

            double allianceSwitch = 0, centerScale = 0, opponentSwitch = 0, exchange = 0, crossAutoLine = 0, robotBrokeDown = 0, wrongSideAuto = 0;

            if (matchSize > 0) {
                for (LancerMatch match : teamInfo.get(newValue.getTeamNumber())) {
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
        }else{
            detailTeamNumber.setText(String.valueOf(newValue.getTeamNumber()));
            teamSummaryTotalMatches.setText(String.valueOf(0));
            matchListView.getItems().clear();

            allianceSwitchAverage.setText(String.valueOf(0));
            centerScaleAverage.setText(String.valueOf(0));
            opponentSwitchAverage.setText(String.valueOf(0));
            exchangeAverage.setText(String.valueOf(0));

            crossAutoLinePercent.setText(String.format("%.2f%s", 0.00, "%"));
            robotBrokeDownPercent.setText(String.format("%.2f%s", 0.00, "%"));
            wrongSideAutoPercent.setText(String.format("%.2f%s", 0.00, "%"));
        }

        LancerPit lancerPit = pitInfo.get(newValue.getTeamNumber());

        if (lancerPit != null) {
            if (lancerPit.getDrivetrain() != null) {
                drivetrainLabel.setText(lancerPit.getDrivetrain().getName());
            } else {
                drivetrainLabel.setText("No value");
            }

            if (lancerPit.getCubeIntake() != null) {
                cubeIntakeLabel.setText(lancerPit.getCubeIntake().getName());
            } else {
                cubeIntakeLabel.setText("No value");
            }

            if (lancerPit.getClimb() != null) {
                climbLabel.setText(lancerPit.getClimb().getName());
            } else {
                climbLabel.setText("No value");
            }

            if (lancerPit.getRobotWeight() > 0) {
                robotWeightLabel.setText(String.valueOf(lancerPit.getRobotWeight()));
            } else {
                robotWeightLabel.setText("No value");
            }

            if (lancerPit.getProgrammingLanguage() != null) {
                programmingLanguageLabel.setText(lancerPit.getProgrammingLanguage().getName());
            } else {
                programmingLanguageLabel.setText("No value");
            }
        }

        Image robot = robotImages.get(newValue.getTeamNumber());

        if(robot != null){
            robotImage.setImage(robot);
        }
    }
}