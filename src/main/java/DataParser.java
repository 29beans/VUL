import java.io.*;
import com.google.gson.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    //private ArrayList<Data> dataList;
    //private Data tempData;
    private List<Map<String, Object>> dataList;
    private Map<String, Object> tempMap;
    private Data tempData;


    public DataParser()
    {
        //dataList=new ArrayList<Data>();
        dataList= new ArrayList<Map<String, Object>>();
    }

    public void parser(String jsonPath, String fileName) throws IOException {
        Gson gson = new Gson();
        File jsonFile = Paths.get(jsonPath + fileName).toFile();
        JsonArray rootArr = gson.fromJson(new FileReader(jsonFile), JsonObject.class).get("CVE_Items").getAsJsonArray();

        //int i = 0;
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
            //System.out.println(++i);
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

        try
        {
            fw=new FileWriter(jsonFile);
            gson.toJson(dataList, fw);
        }catch(Exception e)
        {e.printStackTrace();}
        finally
        {
            fw.close();
        }
    }
}
