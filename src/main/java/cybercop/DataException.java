//Enliang Wu
//enliangw
package cybercop;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DataException extends RuntimeException {

    public DataException(String message) {
        super(message);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Data Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
