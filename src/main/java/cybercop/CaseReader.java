//Enliang Wu
//enliangw
package cybercop;

import java.util.List;

/**
 * CaseReader class is used by CaseReaderFactory
 * to initialize the data file to be read.
 * Its readCases() method is implemented by
 * CSVCaseReader and TSVCaseReader classes
 */
public abstract class CaseReader {

    String filename;

    CaseReader(String filename) {
        this.filename = filename;
    }

    abstract List<Case> readCases();

    protected Case parseCase(List<String> properties) {
        if (properties.size() < 7) {
            return null;
        }
        String caseDate = properties.get(0);
        if (caseDate == null || caseDate.isBlank()) {
            return null;
        }
        String caseTitle = properties.get(1);
        if (caseTitle == null || caseTitle.isBlank()) {
            return null;
        }
        String caseType = properties.get(2);
        if (caseType == null || caseType.isBlank()) {
            return null;
        }
        String caseNumber = properties.get(3);
        if (caseNumber == null || caseNumber.isBlank()) {
            return null;
        }
        String caseLink = properties.get(4);
        String caseCategory = properties.get(5);
        String caseNotes = properties.get(6);
        return new Case(caseDate, caseTitle, caseType, caseNumber, caseLink, caseCategory, caseNotes);
    }
}
