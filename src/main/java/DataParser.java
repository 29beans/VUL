import java.io.*;
import com.google.gson.*;
import java.util.*;

public class DataParser {

    //private ArrayList<Data> dataList;
    //private Data tempData;
    static private File dataDir;
    static private File outputDir;
    static private ArrayList<String> parseDirList;
    static private List<Map<String, Object>> dataList;
    static private Map<String, Object> tempMap;
    static private Data tempData;


    public DataParser(File jsonDataDir, File parsedOutputDir)
    {
        dataDir =jsonDataDir;
        outputDir=parsedOutputDir;
        parseDirList = new ArrayList<>();
        for(String dir: dataDir.list())
            parseDirList.add(dir);
    }

    public void parse()
    {
        parsingDirCheck();
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

                    parseCVE(cveObj);
                    parseVendorData(vendorArr);
                    parseCWE(cweArr);
                    parseDescription(dsctArr);
                    parseCpe(nodesArr);

                    dataList.add(tempData.createMap());
                }

                try{ writeJsonResult(new File(outputDir.getAbsolutePath() + "/" + jsonFile.getParentFile().getName() + "/" + jsonFile.getName()));}
                catch(IOException e) { e.printStackTrace(); }
            }
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

    private void parseCVE(JsonObject jObject)
    {
        tempData.setCVE_ID(jObject.get("ID").getAsString());
    }

    private void parseVendorData(JsonArray jArray)
    {
        tempData.addVendorData(jArray);
    }

    private void parseCWE(JsonArray jArray)
    {
        tempData.addCWE(jArray);
    }

    private void parseDescription(JsonArray jArray)
    {
        tempData.addDescription(jArray);
    }

    private void parseCpe(JsonArray jArray)
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
