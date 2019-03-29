import java.io.*;
import com.google.gson.*;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;

public class DataParser {

    private File dataDir;
    private File outputDir;
    private File cweCSV;
    private File cweToCve;
    private ArrayList<String> parseDirList;
    private Map<String, Map<String, Object>> dataMap;
    private Data tempData;

    private Map<String, Map<String, Object>> cweInfo;
    private Reference ref;
    private Map<String, Map<String, Object>> refMap;  //refName: {id: class}
    private Map<String, Map<String, String>> cweRefMap; //cwe: {refName: id}


    public DataParser(File jsonDataDir, File parsedOutputDir, File cweCsvFile, File cweToCveCsv)
    {
        dataDir =jsonDataDir;
        outputDir=parsedOutputDir;
        cweCSV=cweCsvFile;
        cweToCve=cweToCveCsv;
        cweInfo=new HashMap<String, Map<String, Object>>();
        parseDirList = new ArrayList<>();
        for(String dir: dataDir.list())
            parseDirList.add(dir);
        ref=new Reference();
        refMap=ref.readRefMap();
        cweRefMap=ref.readCweReference();
    }

    public void parse()
    {
        //parsingDirCheck();
        parseCweInfoFromCSV(cweInfo, cweCSV);
        Gson gson = new Gson();

        for(String dir: parseDirList)
        {
            File parseDir = new File(dataDir, dir);

            for (File jsonFile : parseDir.listFiles())
            {
                if (jsonFile.getName().startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                    continue;
                System.out.println("Parsing start... (File: "+jsonFile.getAbsolutePath()+")");
                JsonArray rootArr = null;

                try{ rootArr = gson.fromJson(new FileReader(jsonFile), JsonObject.class).get("CVE_Items").getAsJsonArray();}
                catch(FileNotFoundException e) { e.printStackTrace(); }

                dataMap = new HashMap<String, Map<String, Object>>();

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
                    parseReferences(tempData, refMap, cweRefMap);

                    dataMap.put(tempData.getCVE_ID(),tempData.createMap());
                }

                try{ writeJsonResult(new File(outputDir.getAbsolutePath() + "/" + jsonFile.getParentFile().getName() + "/" + jsonFile.getName()));}
                catch(IOException e) { e.printStackTrace(); }
            }
        }
        recordCweToCve(cweToCve, cweInfo);
    }

    public Map<String, Map<String, Object>> getSimilar(String cve_id)
    {
        Map<String, Map<String, Object>> similar = new HashMap<String, Map<String, Object>>();
        ArrayList<String> cve_list = new ArrayList<String>(); //format: "CVE-123"
        ArrayList<String> cwe_list = new ArrayList<String>(); //format: "CWE-123"
        Set<String> related_cwe_list = new HashSet<String>(); //format: "123"

        if(!dataMap.containsKey(cve_id))
        {
            System.out.println("There is no objects containing such CVE-ID! in our data set");
            return similar;
        }

        Map<String, Object> cwe_map=(Map<String, Object>)(((HashMap<String, Object>)dataMap.get(cve_id).get("References")).get("CWE"));
        System.out.println(cwe_map);

        for(String key : cwe_map.keySet())  //cwe format: "CWE-123"
        {
            cwe_list.add(key);
            related_cwe_list.addAll((ArrayList<String>)(((Map<String,Object>)cwe_map.get(key)).get("Related Weaknesses")));
        }

        System.out.println(cwe_list);
        System.out.println(related_cwe_list);

        for(String cwe: cwe_list)   //cwe format: "CWE-123"
        {
            ArrayList<String> cve_arr = (ArrayList<String>) cweInfo.get(number(cwe)).get("Related Vulnerabilities");
            cve_list.addAll(cve_arr);
        }

        for(String rel_cwe: related_cwe_list) //cwe format: "123"
        {
            ArrayList<String> cve_arr = (ArrayList<String>) cweInfo.get(number(rel_cwe)).get("Related Vulnerabilities");
            cve_list.addAll(cve_arr);
        }

        if(cve_list.contains(cve_id))
            cve_list.remove(cve_id);

        System.out.println("Total # of similar cases: "+cve_list.size());
        System.out.println(cve_list);

        for(String cve:cve_list)
        {
            if(similar.size() >=10)
                break;
            if(!similar.containsKey(cve))
                similar.put(cve, dataMap.get(cve));
        }

        return similar;
    }

    private String number(String cwe)
    {
        int dash_idx=cwe.indexOf("-");
        return cwe.substring(dash_idx+1);
    }

    public void parseJson()
    {
        parseCweInfoFromCSV(cweInfo, cweCSV);
        Gson gson = new Gson();
        dataMap = new HashMap<String, Map<String, Object>>();

        for (File jsonFile : dataDir.listFiles())
        {
            if (jsonFile.getName().startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                continue;
            System.out.println("Parsing start... (File: "+jsonFile.getName()+")");
            JsonArray rootArr = null;

            try{ rootArr = gson.fromJson(new FileReader(jsonFile), JsonObject.class).get("CVE_Items").getAsJsonArray();}
            catch(FileNotFoundException e) { e.printStackTrace(); }

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
                parseReferences(tempData, refMap, cweRefMap);

                dataMap.put(tempData.getCVE_ID(),tempData.createMap());
            }
            System.out.println("Parsing finished... (File: "+jsonFile.getName()+")");
        }
    }

    public void recordCweToCve(File cweToCve, Map<String, Map<String, Object>> cweInfo)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cweToCve));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("CWE_ID", "Name", "Related Vulnerabilities", "Related Weaknesses")))
        {
            for(String key: cweInfo.keySet())
            {
                csvPrinter.printRecord(key, cweInfo.get(key).get("Name"), cweInfo.get(key).get("Related Vulnerabilities"), cweInfo.get(key).get("Related Weaknesses"));
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void parseCweInfoFromCSV(Map<String, Map<String, Object>> cweInfo, File csv)
    {
        try(Reader reader = new FileReader(csv);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord: csvParser)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("Name", csvRecord.get("Name"));
                ArrayList<String> arr=new ArrayList<String>();
                splitWeaknesses(csvRecord.get("Related Weaknesses"),arr);
                map.put("Related Weaknesses", arr);
                map.put("Related Vulnerabilities", new ArrayList<String>());
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
            if(!arr.contains("CWE-"+id))
                arr.add("CWE-"+id);
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

    public Map<String, Map<String, Object>> getDataMap()
    {
        return dataMap;
    }

    private void parseCVE(Data tempData, JsonObject jObject)
    {
        tempData.setCVE_ID(jObject.get("ID").getAsString());
    }

    private void parseVendorData(Data tempData, JsonArray jArray)
    {
        tempData.addVendorData(jArray);
    }

    private void parseCWE(Data tempData, JsonArray jArray, Map<String, Map<String, Object>> cweInfo)
    {
        tempData.addCWE(jArray, cweInfo);
    }

    private void parseReferences(Data tempData,
                                 Map<String, Map<String, Object>> refMap,
                                 Map<String, Map<String, String>> cweRefMap)
    {
        tempData.addReferences(refMap, cweRefMap);
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

        if(!jsonFile.getParentFile().exists())
            jsonFile.getParentFile().mkdirs();

        try(OutputStreamWriter fw=new OutputStreamWriter(new FileOutputStream(jsonFile), "UTF-8"))
        {
            gson.toJson(dataMap, fw);
        }catch(Exception e) {e.printStackTrace();}
    }
}