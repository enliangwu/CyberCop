//Enliang Wu
//enliangw
package cybercop;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddCaseView extends CaseView {

    public AddCaseView(String header) {
        super(header);
    }

    @Override
    Stage buildView() {
        // Set up stageView
        stage.setScene(new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT));
        //Close Button
        closeButton.setOnAction(event -> stage.close());
        //Clear Button
        clearButton.setOnAction(event -> {
            titleTextField.clear();
            caseDatePicker.setValue(LocalDate.now());
            caseTypeTextField.clear();
            caseNumberTextField.clear();
            categoryTextField.clear();
            caseLinkTextField.clear();
            caseNotesTextArea.clear();
        });

        updateButton.setText("Add case");
        caseDatePicker.setValue(LocalDate.now());
        return stage;
    }


}

