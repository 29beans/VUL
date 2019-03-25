import java.io.*;
import com.google.gson.*;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class DataParser {

    static private File dataDir;
    static private File outputDir;
    static private File cweCSV;
    static private File cweToCve;
    static private ArrayList<String> parseDirList;
    static private List<Map<String, Object>> dataList;
    static private Data tempData;

    static private Map<String, Object> cweInfo;


    public DataParser(File jsonDataDir, File parsedOutputDir, File cweCsvFile, File cweToCveCsv)
    {
        dataDir =jsonDataDir;
        outputDir=parsedOutputDir;
        cweCSV=cweCsvFile;
        cweToCve=cweToCveCsv;
        cweInfo=new HashMap<String, Object>();
        parseDirList = new ArrayList<>();
        for(String dir: dataDir.list())
            parseDirList.add(dir);
    }

    public void parse()
    {
        parsingDirCheck();
        parseCweInfoFromCSV(cweCSV);
        Gson gson = new Gson();

        for(String dir: parseDirList)
        {
            File parseDir = new File(dataDir, dir);

            for (File jsonFile : parseDir.listFiles())
            {
                if (jsonFile.getName().startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                    continue;

                JsonArray rootArr = null;

                try{ rootArr = gson.fromJson(new FileReader(jsonFile), JsonObject.class).get("CVE_Items").getAsJsonArray();}
                catch(FileNotFoundException e) { e.printStackTrace(); }

                dataList = new ArrayList<Map<String, Object>>();

                for (JsonElement je : rootArr) {
                    JsonObject cveObj = je.getAsJsonObject().get("cve").getAsJsonObject().get("CVE_data_meta").getAsJsonObject();
                    JsonArray vendorArr = je.getAsJsonObject().get("cve").getAsJsonObject().get("affects").getAsJsonObject().get("vendor").getAsJsonObject().get("vendor_data").getAsJsonArray();
                    JsonArray cweArr = je.getAsJsonObject().get("cve").getAsJsonObject().get("problemtype").getAsJsonObject().getAsJsonObject().get("problemtype_data").getAsJsonArray();
                    JsonArray dsctArr = je.getAsJsonObject().get("cve").getAsJsonObject().get("description").getAsJsonObject().get("description_data").getAsJsonArray();
                    JsonArray nodesArr = je.getAsJsonObject().get("configurations").getAsJsonObject().get("nodes").getAsJsonArray();

                    tempData = new Data();

                    parseCVE(tempData, cveObj);
                    parseVendorData(tempData, vendorArr);
                    parseCWE(tempData, cweArr, cweInfo);
                    parseDescription(tempData, dsctArr);
                    parseCpe(tempData, nodesArr);

                    dataList.add(tempData.createMap());
                }

                try{ writeJsonResult(new File(outputDir.getAbsolutePath() + "/" + jsonFile.getParentFile().getName() + "/" + jsonFile.getName()));}
                catch(IOException e) { e.printStackTrace(); }
            }
        }
    }

    public void recordCweToCve(File cweToCve)
    {

    }

    public void parseCweInfoFromCSV(File csv)
    {
        try {
            Reader reader = new FileReader(csv);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for(CSVRecord csvRecord: csvParser)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("Name", csvRecord.get("Name"));
                ArrayList<String> arr=new ArrayList<String>();
                splitWeaknesses(csvRecord.get("Related Weaknesses"),arr);
                map.put("Related Weaknesses", arr);+
                map.put("CVE-ID", new HashSet<String>());
                cweInfo.put(csvRecord.get("CWE-ID"), map);
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

    private void splitWeaknesses(String weaknesses, ArrayList<String> arr)
    {
        int start_idx=weaknesses.indexOf("CWE ID:");
        if(start_idx == -1)
            return;
        else
        {
            int end_idx=weaknesses.indexOf(":",start_idx+7);

            if(end_idx == -1)
                System.out.println("Error in extracting CWE-ID.... end_idx error!");

            String id= weaknesses.substring(start_idx+7, end_idx);
            arr.add(id);
            splitWeaknesses(weaknesses.substring(end_idx), arr);
        }
    }

    public void parsingDirCheck()
    {
        for(Iterator<String> it = parseDirList.iterator() ; it.hasNext() ; )
        {
            String dir = it.next();
            if(Arrays.asList(outputDir.list()).contains(dir))
                it.remove();
        }
    }

    public List<Map<String, Object>> getDataList()
    {
        return dataList;
    }

    private void parseCVE(Data tempData, JsonObject jObject)
    {
        tempData.setCVE_ID(jObject.get("ID").getAsString());
    }

    private void parseVendorData(Data tempData, JsonArray jArray)
    {
        tempData.addVendorData(jArray);
    }

    private void parseCWE(Data tempData, JsonArray jArray, Map<String, Object> cweInfo)
    {
        tempData.addCWE(jArray, cweInfo);
    }

    private void parseDescription(Data tempData, JsonArray jArray)
    {
        tempData.addDescription(jArray);
    }

    private void parseCpe(Data tempData, JsonArray jArray)
    {
        tempData.addCpe23Uri(jArray);
    }

    public void writeJsonResult(File jsonFile) throws IOException
    {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        FileWriter fw=null;

        if(!jsonFile.getParentFile().exists())
            jsonFile.getParentFile().mkdirs();

        try
        {
            fw=new FileWriter(jsonFile);
            gson.toJson(dataList, fw);
        }catch(Exception e) {e.printStackTrace();}
        finally
        {
            fw.close();
            System.out.println("[Data Parsing] Done: "+jsonFile.getParentFile().getAbsolutePath());
        }
    }
}
