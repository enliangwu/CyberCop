//Enliang Wu
//enliangw
package cybercop;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModifyCaseView extends CaseView {

    ModifyCaseView(String header) {
        super(header);
    }

    @Override
    Stage buildView() {
        updateButton.setText("Update case");
        updateCaseData(CyberCop.currentCase);
        return stage;
    }

    public void updateCaseData(Case c) {
        //Set up the stage
        stage.setScene(new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT));
        closeButton.setOnAction(event -> stage.close());
        clearButton.setOnAction(event -> {
            titleTextField.clear();
            caseDatePicker.setValue(LocalDate.now());
            caseTypeTextField.clear();
            caseNumberTextField.clear();
            categoryTextField.clear();
            caseLinkTextField.clear();
            caseNotesTextArea.clear();
        });


        //Set up the text
        titleTextField.setText(c.getCaseTitle());
        caseDatePicker.setValue(LocalDate.parse(c.getCaseDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        caseTypeTextField.setText(c.getCaseType());
        caseNumberTextField.setText(c.getCaseNumber());
        categoryTextField.setText(c.getCaseCategory());
        caseLinkTextField.setText(c.getCaseLink());
        caseNotesTextArea.setText(c.getCaseNotes());
    }
}
