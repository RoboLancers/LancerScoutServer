package main.utility;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import main.models.LancerMatch;

public class AlertHelper {
    public static Alert showAlert(Alert.AlertType alertType, Window owner, String title, String message){
        Alert alert = createAlert(alertType, owner, title, message);
        alert.show();
        return alert;
    }

    public static Alert createAlert(Alert.AlertType alertType, Window owner, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);

        return alert;
    }

    public static void showMatchAlert(LancerMatch match, Window owner){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Match Information");
        alert.setHeaderText("Match " + match.getMatchNumber() + " for team " + match.getTeamNumber());
        alert.setContentText(match.getMatchInfo(match));
        alert.initOwner(owner);
        alert.show();
    }

    public static Alert createDuplicateMatchAlert(LancerMatch match1, LancerMatch match2, Window owner){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Match Conflict");
        alert.setHeaderText("Overwrite Match " + match1.getMatchNumber() + " for team " + match1.getTeamNumber() + "?");

        String match1Difference = "", match2Difference = "";

        HBox dialogPaneContent = new HBox();
        Label match1Label = new Label(), match2Label = new Label();

        if(match1.getColor() != match2.getColor()){
            match1Difference += "Color: " + match1.getColor() + "\n";
            match2Difference += "Color: " + match2.getColor() + "\n";
        }

        if(match1.getStartingConfiguration() != match2.getStartingConfiguration()){
            match1Difference += "Starting Configuration: " + match1.getStartingConfiguration() + "\n";
            match2Difference += "Starting Configuration: " + match2.getStartingConfiguration() + "\n";
        }

        if(match1.getCrossedAutoLine() != match2.getCrossedAutoLine()){
            match1Difference += "Crossed Auto Line: " + match1.getCrossedAutoLine() + "\n";
            match2Difference += "Crossed Auto Line: " + match2.getCrossedAutoLine() + "\n";
        }

        if(match1.getAutonomousAttempt() != match2.getAutonomousAttempt()){
            match1Difference += "Autonomous Attempt: " + match1.getAutonomousAttempt() + "\n";
            match2Difference += "Autonomous Attempt: " + match2.getAutonomousAttempt() + "\n";
        }

        if(match1.getWrongSideAuto() != match2.getWrongSideAuto()){
            match1Difference += "Wrong side auto: " + match1.getWrongSideAuto() + "\n";
            match2Difference += "Wrong side auto: " + match2.getWrongSideAuto() + "\n";
        }

        if(match1.getAllianceSwitch() != match2.getAllianceSwitch()){
            match1Difference += "Alliance Switch: " + match1.getAllianceSwitch() + "\n";
            match2Difference += "Alliance Switch: " + match2.getAllianceSwitch() + "\n";
        }

        if(match1.getCenterScale() != match2.getCenterScale()){
            match1Difference += "Center Scale: " + match1.getCenterScale() + "\n";
            match2Difference += "Center Scale: " + match2.getCenterScale() + "\n";
        }

        if(match1.getOpponentSwitch() != match2.getOpponentSwitch()){
            match1Difference += "Opponent Switch: " + match1.getOpponentSwitch() + "\n";
            match2Difference += "Opponent Switch: " + match2.getOpponentSwitch() + "\n";
        }

        if(match1.getExchange() != match2.getExchange()){
            match1Difference += "Exchange: " + match1.getExchange() + "\n";
            match2Difference += "Exchange: " + match2.getExchange() + "\n";
        }

        if(match1.getEndGameAttempt() != match2.getEndGameAttempt()){
            match1Difference += "End Game Attempt: " + match1.getEndGameAttempt() + "\n";
            match2Difference += "End Game Attempt: " + match2.getEndGameAttempt() + "\n";
        }

        if(match1.getRobotBrokeDown() != match2.getRobotBrokeDown()){
            match1Difference += "Robot Broke Down: " + match1.getRobotBrokeDown() + "\n";
            match2Difference += "Robot Broke Down: " + match2.getRobotBrokeDown() + "\n";
        }

        if(!match1.getComment().equals(match2.getComment())){
            match1Difference += "Comments: " + match1.getComment() + "\n";
            match2Difference += "Comments: " + match2.getComment() + "\n";
        }

        if(match1Difference.isEmpty() && match2Difference.isEmpty()){
            match1Label.setText("Everything is the same");
            match1Label.setPadding(new Insets(20));

            dialogPaneContent.getChildren().addAll(match1Label);
        }else{
            match1Label.setText(match1Difference);
            match2Label.setText(match2Difference);

            match1Label.setPadding(new Insets(20));
            match2Label.setPadding(new Insets(20));

            dialogPaneContent.getChildren().addAll(match1Label, match2Label);
        }

        alert.getDialogPane().setContent(dialogPaneContent);

        alert.initOwner(owner);

        return alert;
    }
}
