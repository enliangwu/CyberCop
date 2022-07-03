//Enliang Wu
//enliangw
package cybercop;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVCaseReader extends CaseReader {

    CSVCaseReader(String filename) {
        super(filename);
    }

    /**
     * readCases uses CSVParser library to parse data file
     */
    @Override
    List<Case> readCases() {
        List<Case> caseList = new ArrayList<>();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();  //data file has head row
        try {
            CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
            for (CSVRecord csvRecord : csvParser) {
                Case c = parseCase(new ArrayList<>(csvRecord.toMap().values()));
                caseList.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caseList;
    }
}
