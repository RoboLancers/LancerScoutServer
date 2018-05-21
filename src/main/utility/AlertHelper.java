package main.utility;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Window;
import main.models.LancerMatch;

public class AlertHelper {
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
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
}
