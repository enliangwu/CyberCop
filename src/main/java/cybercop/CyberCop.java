//Enliang Wu
//enliangw
package cybercop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class CyberCop extends Application {

    public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
    public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
    public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

    CCView ccView = new CCView();
    CCModel ccModel = new CCModel();

    CaseView caseView; //UI for Add/Modify/Delete menu option

    GridPane cyberCopRoot;
    Stage stage;

    static Case currentCase; //points to the case selected in TableView.

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * start the application and show the opening scene
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle(APP_TITLE);
        cyberCopRoot = ccView.setupScreen();
        setupBindings();
        Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
        primaryStage.show();
    }

    /**
     * setupBindings() binds all GUI components to their handlers.
     * It also binds disableProperty of menu items and text-fields
     * with ccView.isFileOpen so that they are enabled as needed
     */
    void setupBindings() {
        //write your code here
        ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
        ccView.saveFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
        ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
        ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
        ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
        ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());

        ccView.caseTableView.setItems(ccModel.caseList);
        ccView.yearComboBox.setItems(ccModel.yearList);

        //Add Listener
        ccView.caseTableView.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<Case>) change -> {
                    while (change.next()) {
                        //Check if the list is null
                        if (!change.getList().isEmpty()) {
                            currentCase = change.getList().get(0);
                            ccView.titleTextField.setText(currentCase.getCaseTitle());
                            ccView.caseTypeTextField.setText(currentCase.getCaseType());
                            ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
                            ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
                            ccView.yearComboBox.getSelectionModel().select(currentCase.getCaseDate().substring(0, 4));

                            if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
                                URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
                                if (url != null) ccView.webEngine.load(url.toExternalForm());
                            } else if (currentCase.getCaseLink().toLowerCase().startsWith("http")) {  //if external link
                                ccView.webEngine.load(currentCase.getCaseLink());
                            } else {
                                URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
                                if (url != null) ccView.webEngine.load(url.toExternalForm());
                            }
                        } else {
                            ccView.titleTextField.clear();
                            ccView.caseTypeTextField.clear();
                            ccView.yearComboBox.getSelectionModel().clearSelection();
                            ccView.caseNumberTextField.clear();
                            ccView.caseNotesTextArea.clear();
                            ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
                        }
                    }
                });

        //OpenFile handler
        ccView.openFileMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                ccModel.readCases(file.getPath());
                ccModel.buildYearMapAndList();
                ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
                stage.setTitle(APP_TITLE + ": " + file.getName());
                ccView.isFileOpen.set(true);

                if (!ccModel.caseList.isEmpty()) {
                    ccView.caseTableView.getSelectionModel().selectFirst();
                }
            }
        });

        //Close File handler
        ccView.closeFileMenuItem.setOnAction(event -> {
            ccView.caseTableView.setItems(ccModel.caseList);
            ccModel.caseList.clear();
            ccModel.caseMap.clear();
            ccModel.yearList.clear();
            ccModel.yearMap.clear();
            ccView.isFileOpen.set(false);
            ccView.titleTextField.clear();
            ccView.caseTypeTextField.clear();
            ccView.yearComboBox.getSelectionModel().clearSelection();
            ccView.caseNumberTextField.clear();
            currentCase = null;
        });

        //Save file button handler
        ccView.saveFileMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null && ccModel.writeCases(file.getPath())) {
                ccView.messageLabel.setText(file.getName() + " saved");
            }
        });

        //Exit app handler
        ccView.exitMenuItem.setOnAction(event -> Platform.exit());

        //Search Button Handler
        ccView.searchButton.setOnAction(event -> {
            String title = ccView.titleTextField.getText();
            String caseType = ccView.caseTypeTextField.getText();
            String year = ccView.yearComboBox.getSelectionModel().getSelectedItem();
            String caseNumber = ccView.caseNumberTextField.getText();
            List<Case> cases = ccModel.searchCases(title, caseType, year, caseNumber);
            ccView.caseTableView.setItems(FXCollections.observableArrayList(cases));
            ccView.messageLabel.setText(cases.size() + " cases.");
        });

        //Clear button handler
        ccView.clearButton.setOnAction(event -> {
            ccView.titleTextField.clear();
            ccView.caseTypeTextField.clear();
            ccView.yearComboBox.getSelectionModel().clearSelection();
            ccView.caseNumberTextField.clear();
        });

        // Add case handler
        ccView.addCaseMenuItem.setOnAction(event -> {
            caseView = new AddCaseView("Add case");
            caseView.updateButton.setOnAction(addCaseEvent -> {
                String caseDate = caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String caseTitle = caseView.titleTextField.getText();
                String caseType = caseView.caseTypeTextField.getText();
                String caseNumber = caseView.caseNumberTextField.getText();
                String caseLink = caseView.caseLinkTextField.getText();
                String caseCategory = caseView.categoryTextField.getText();
                String caseNotes = caseView.caseNotesTextArea.getText();

                if (!validate(caseDate, caseTitle, caseType, caseNumber)) return;
                if (!isDuplicated(caseNumber, null)) return;

                Case newCase = new Case(caseDate, caseTitle, caseType, caseNumber, caseLink, caseCategory, caseNotes);
                ccModel.caseList.add(newCase);
                ccModel.caseMap.put(newCase.getCaseNumber(), newCase);
                ccModel.buildYearMapAndList();
                ccView.caseTableView.getSelectionModel().select(newCase);
                caseView.stage.close();
            });
            caseView.buildView().show();
        });

        //Modify case handler
        ccView.modifyCaseMenuItem.setOnAction(event -> {
            caseView = new ModifyCaseView("Modify case");
            caseView.updateButton.setOnAction(modifyCaseEvent -> {
                String caseDate = caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String caseTitle = caseView.titleTextField.getText();
                String caseType = caseView.caseTypeTextField.getText();
                String caseNumber = caseView.caseNumberTextField.getText();
                if (!validate(caseDate, caseTitle, caseType, caseNumber)) return;
                if (!isDuplicated(caseNumber, currentCase)) return;

                currentCase.setCaseDate(caseDate);
                currentCase.setCaseTitle(caseTitle);
                currentCase.setCaseType(caseType);
                currentCase.setCaseNumber(caseNumber);
                currentCase.setCaseLink(caseView.caseLinkTextField.getText());
                currentCase.setCaseCategory(caseView.categoryTextField.getText());
                currentCase.setCaseNotes(caseView.caseNotesTextArea.getText());
                ccModel.buildYearMapAndList();
                ccView.titleTextField.setText(currentCase.getCaseTitle());
                ccView.caseTypeTextField.setText(currentCase.getCaseType());
                ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
                ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
                ccView.yearComboBox.getSelectionModel().select(currentCase.getCaseDate().substring(0, 4));
                caseView.stage.close();
            });
            caseView.buildView().show();
        });

        //Delete case handler
        ccView.deleteCaseMenuItem.setOnAction(event -> {
            caseView = new DeleteCaseView("Delete case");
            caseView.updateButton.setOnAction(deleteCaseEvent -> {
                ccModel.caseList.remove(currentCase);
                ccModel.caseMap.remove(currentCase.getCaseNumber(), currentCase);
                ccModel.buildYearMapAndList();
                ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
                if (!ccModel.caseList.isEmpty()) {
                    ccView.caseTableView.getSelectionModel().selectFirst();
                } else {
                    ccView.caseTableView.getSelectionModel().clearSelection();
                }
                caseView.stage.close();
            });
            caseView.buildView().show();
        });

        //Case count chart handler
        ccView.caseCountChartMenuItem.setOnAction(event -> ccView.showChartView(ccModel.yearMap));
    }

    private boolean isDuplicated(String caseNumber, Case exceptFor) {
        try {
            for (Case c : ccModel.caseList) {
                if (c != exceptFor && c.getCaseNumber().equals(caseNumber)) {
                    throw new DataException("Duplicate case number");
                }
            }
        } catch (DataException e) {
            return false;
        }
        return true;
    }

    private static boolean validate(String caseDate, String caseTitle, String caseType, String caseNumber) {
        try {
            String errorMessage = "Case must have date, title, type and number";
            if (caseDate == null || caseDate.isBlank()) {
                throw new DataException(errorMessage);
            }
            if (caseTitle == null || caseTitle.isBlank()) {
                throw new DataException(errorMessage);
            }
            if (caseType == null || caseType.isBlank()) {
                throw new DataException(errorMessage);
            }
            if (caseNumber == null || caseNumber.isBlank()) {
                throw new DataException(errorMessage);
            }
        } catch (DataException e) {
            return false;
        }
        return true;
    }
}
