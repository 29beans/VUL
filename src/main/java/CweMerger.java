import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;
import java.io.*;
import java.util.*;

public class CweMerger {

    private File cweCsvDir= new File("C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/CWE/files/");
    private File outputCSV= new File("C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/CWE/merged/merged.csv");
    private ArrayList<String> header= new ArrayList<String>();
    private Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();

    public void mergeCWE(File cweDir)
    {
        for(File cweFile: cweDir.listFiles())
        {
            try ( Reader reader = new FileReader(cweFile);CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT))
            {
                List<CSVRecord> csvRecords = csvParser.getRecords();
                //System.out.println("File reading: "+cweFile.getName());
                //header name copy just one time only!
                if(header.size() == 0) {
                    CSVRecord headerRec = csvRecords.get(0);
                    for (int i = 0; i < headerRec.size(); i++) {
                        header.add(headerRec.get(i));
                    }
                    //System.out.println(header);
                }

                //Delete header record from csvRecords list
                csvRecords.remove(0);

                for(CSVRecord csvRecord: csvRecords)
                {
                   ArrayList<String> arr = new ArrayList<String>();
                    int intKey=Integer.parseInt(csvRecord.get(0));
                    if(!map.containsKey(intKey)) {
                        for (int i = 0; i < csvRecord.size(); i++)
                            arr.add(csvRecord.get(i));
                        map.put(intKey, arr);
                    }
                }
            }catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void sortPrint()
    {
        SortedSet<Integer> sortedKeys = new TreeSet<>(map.keySet());
        try( BufferedWriter writer =  new BufferedWriter(new FileWriter(outputCSV)); CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT))
        {
            csvPrinter.printRecord(header);
            //System.out.println(sortedKeys);

            for(int key: sortedKeys)
            {
                csvPrinter.printRecord(map.get(key));
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public static void main(String[] args) {
        CweMerger merger = new CweMerger();
        merger.mergeCWE(cweCsvDir);
        System.out.println("merged size: "+map.size());
        merger.sortPrint();
    }*/
}
