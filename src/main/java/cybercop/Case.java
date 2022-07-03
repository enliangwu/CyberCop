//Enliang Wu
//enliangw
package cybercop;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case> {

    private final StringProperty caseDate;
    private final StringProperty caseTitle;
    private final StringProperty caseType;
    private final StringProperty caseNumber;
    private final StringProperty caseLink;
    private final StringProperty caseCategory;
    private final StringProperty caseNotes;

    //Constructor
    public Case(String caseDate, String caseTitle, String caseType, String caseNumber, String caseLink, String caseCategory, String caseNotes) {
        this.caseDate = new SimpleStringProperty(caseDate);
        this.caseTitle = new SimpleStringProperty(caseTitle);
        this.caseType = new SimpleStringProperty(caseType);
        this.caseNumber = new SimpleStringProperty(caseNumber);
        this.caseLink = new SimpleStringProperty(caseLink);
        this.caseCategory = new SimpleStringProperty(caseCategory);
        this.caseNotes = new SimpleStringProperty(caseNotes);
    }

    public String getCaseDate() {
        return caseDate.get();
    }

    public void setCaseDate(String caseDate) {
        this.caseDate.set(caseDate);
    }

    public StringProperty caseDateProperty() {
        return caseDate;
    }

    public String getCaseTitle() {
        return caseTitle.get();
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle.set(caseTitle);
    }

    public StringProperty caseTitleProperty() {
        return caseTitle;
    }

    public String getCaseType() {
        return caseType.get();
    }

    public void setCaseType(String caseType) {
        this.caseType.set(caseType);
    }

    public StringProperty caseTypeProperty() {
        return caseType;
    }

    public String getCaseNumber() {
        return caseNumber.get();
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber.set(caseNumber);
    }

    public StringProperty caseNumberProperty() {
        return caseNumber;
    }

    public String getCaseLink() {
        return caseLink.get();
    }

    public void setCaseLink(String caseLink) {
        this.caseLink.set(caseLink);
    }

    public StringProperty caseLinkProperty() {
        return caseLink;
    }

    public String getCaseCategory() {
        return caseCategory.get();
    }

    public void setCaseCategory(String caseCategory) {
        this.caseCategory.set(caseCategory);
    }

    public StringProperty caseCategoryProperty() {
        return caseCategory;
    }

    public String getCaseNotes() {
        return caseNotes.get();
    }

    public void setCaseNotes(String caseNotes) {
        this.caseNotes.set(caseNotes);
    }

    public StringProperty caseNotesProperty() {
        return caseNotes;
    }

    public String toDataFileString() {
        String[] properties = new String[]{
                this.getCaseDate(),
                this.getCaseTitle(),
                this.getCaseType(),
                this.getCaseNumber(),
                this.getCaseLink(),
                this.getCaseCategory(),
                this.getCaseNotes(),
        };
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].isEmpty()) {
                properties[i] = " ";
            }
        }
        return String.join("\t", properties) + "\n";
    }

    @Override
    public int compareTo(Case c) {
        return String.CASE_INSENSITIVE_ORDER.compare(c.getCaseDate(), this.getCaseDate());
    }

    @Override
    public String toString() {
        return "[Case #" + this.getCaseNumber() + "]";
    }
}