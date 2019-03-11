import java.io.File;
import java.io.IOException;

public class Main {

    //Below are hard-coded absolute paths for json file input and output
    //Modify the paths for your own convenience
    static String macJsonPath="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static String winJsonPath="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/2019-02-08/";
    static String jsonPath=winJsonPath;

    static String macOutputPath="/Users/GyuMac/Desktop/회사/멘토링/data/output/";
    static String winOutputPath = "C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/output/";
    static String outputPath=winOutputPath;

    private static String[] getJsonFileList(String dirPath)
    {
        File dir=new File(dirPath);
        return dir.list();
    }

    public static void main(String[] args) throws IOException
    {
        String[] jsonFileList=getJsonFileList(jsonPath);

        for(String file: jsonFileList)
        {
            DataParser dp = new DataParser();

            if(file.startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                continue;

            System.out.println("------- Parsing Start ------- (File: "+file.toString()+")");
            dp.parser(jsonPath, file);
            dp.writeJsonResult(new File(outputPath+file));  // Write parsing result to Json file
            //System.out.println("Items: "+dp.getDataList().size());
            //Below is the code line for returning and storing parsed data list of Json file (format: List<Map<String, Object>>)
            //To handle memory error, the data list of one Json file will be returned for each iteration of for-loop

            //WHERE_YOU_WANT_TO_KEEP_DATA = dp.getDataList();   // Return type: List<Map<String,Object>>
        }
    }
}