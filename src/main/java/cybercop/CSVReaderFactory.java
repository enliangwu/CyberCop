//Enliang Wu
//enliangw
package cybercop;

public class CSVReaderFactory {

    public static CaseReader createReader(String filename) {

        String lowerCaseFilename = filename.toLowerCase();
        //Check the type of the file
        if (lowerCaseFilename.endsWith(".csv")) {
            return new CSVCaseReader(filename);
        } else if (lowerCaseFilename.endsWith(".tsv")) {
            return new TSVCaseReader(filename);
        }

        return null;
    }
}
