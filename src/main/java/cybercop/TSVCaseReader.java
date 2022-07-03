//Enliang Wu
//enliangw
package cybercop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSVCaseReader extends CaseReader {

    TSVCaseReader(String filename) {
        super(filename);
    }

    @Override
    List<Case> readCases() {
        int rejectedCount = 0;
        List<Case> caseList = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new File(filename));
            while (scan.hasNextLine()) {
                String[] splitByTab = scan.nextLine().split("\t");
                Case c = parseCase(List.of(splitByTab));
                if (c != null) {
                    caseList.add(c);
                } else {
                    rejectedCount++;
                }
            }
            scan.close();

            if (rejectedCount > 0) {
                String errorMessage = String.format("%d case rejected.\nThe file must have cases with tab separated data, title, type and case number!", rejectedCount);
                throw new DataException(errorMessage);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DataException ignored) {
        }
        return caseList;
    }
}
