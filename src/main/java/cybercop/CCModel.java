//Enliang Wu
//enliangw
package cybercop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CCModel {

    ObservableList<Case> caseList = FXCollections.observableArrayList();            //a list of case objects
    ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();        //map with caseNumber as key and Case as value
    ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();    //map with each year as a key and a list of all cases dated in that year as value.
    ObservableList<String> yearList = FXCollections.observableArrayList();            //list of years to populate the yearComboBox in ccView

    /**
     * readCases() performs the following functions:
     * It creates an instance of CaseReaderFactory,
     * invokes its createReader() method by passing the filename to it,
     * and invokes the caseReader's readCases() method.
     * The caseList returned by readCases() is sorted
     * in the order of caseDate for initial display in caseTableView.
     * Finally, it loads caseMap with cases in caseList.
     * This caseMap will be used to make sure that no duplicate cases are added to data
     *
     * @param filename
     */
    void readCases(String filename) {
        //write your code here
        CaseReader reader = CSVReaderFactory.createReader(filename);
        if (reader != null) {
            List<Case> cases = reader.readCases();
            //Sort the cases from latest to oldest
            cases.sort((o2, o1) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getCaseDate(), o2.getCaseDate()));
            caseList.addAll(cases);
            for (Case c : cases) {
                caseMap.put(c.getCaseNumber(), c);
            }
        }
    }

    /**
     * buildYearMapAndList() performs the following functions:
     * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
     * 2. It creates yearList which will be used to populate yearComboBox in ccView
     * Note that yearList can be created simply by using the keySet of yearMap.
     */
    void buildYearMapAndList() {
        //write your code here
        //Clear Map and List first
        yearMap.clear();
        yearList.clear();
        for (Case c : caseList) {
            String year = c.getCaseDate().substring(0, 4);
            if (!yearMap.containsKey(year)) {
                yearMap.put(year, new ArrayList<>());
                yearList.add(year);
            }
            yearMap.get(year).add(c);
        }
    }

    /**
     * searchCases() takes search criteria and
     * iterates through the caseList to find the matching cases.
     * It returns a list of matching cases.
     */
    List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
        //write your code here
        List<Case> matches = new ArrayList<>();
        // Check the null condition
        for (Case c : caseList) {
            if (title != null && !c.getCaseTitle().toLowerCase().contains(title)) {
                continue;
            }
            if (caseType != null && !c.getCaseType().toLowerCase().contains(caseType)) {
                continue;
            }
            if (year != null && !c.getCaseDate().substring(0, 4).contains(year)) {
                continue;
            }
            if (caseNumber != null && !c.getCaseNumber().contains(caseNumber)) {
                continue;
            }
            matches.add(c);
        }
        return matches;
    }

    boolean writeCases(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Case c : caseList) {
                writer.write(c.toDataFileString());
            }
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
