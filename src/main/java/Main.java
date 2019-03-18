import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    //Below are the hard-coded absolute paths for json file input and output
    //Modify the paths for your own convenience
    static String macJsonPath="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static String winJsonPath="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/2019-02-08/";
    static String jsonPath=winJsonPath;

    static String macOutputPath="/Users/GyuMac/Desktop/회사/멘토링/data/output/";
    static String winOutputPath = "C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/output/";
    static String outputPath=winOutputPath;

    static private String updateLogPath = "/Users/GyuMac/Desktop/회사/멘토링/data/";
    static private File updateLog = new File(updateLogPath+"update_log.txt");

    private static String[] getJsonFileList(String dirPath)
    {
        File dir=new File(dirPath);
        return dir.list();
    }

    private static int menu()
    {
        Scanner reader = new Scanner(System.in);
        showMenuScreen();

        return reader.nextInt();
    }

    private static void showMenuScreen()
    {
        System.out.println("------------------- Menu Screen -------------------");
        System.out.println("Press the number to execute an appropriate module.");
        System.out.println("1. Crawl json data feeds from NVD website");
        System.out.println("2. Decompress json zip files");
        System.out.println("3. Update the new data feeds from NVD");
        System.out.println("4. Parse the downloaded json data feeds");
        System.out.println("5. Exit");
        System.out.println("---------------------------------------------------");
        System.out.print("Enter: ");
    }

    public static void main(String[] args) throws IOException
    {
        String[] jsonFileList=getJsonFileList(jsonPath);

        while(true)
        {
            int opt = menu();
            if(opt == 5)
                break;

            switch (opt)
            {
                case 1:
                    Crawler crwl = new Crawler();
                    crwl.crawl();
                    break;
                case 2:
                    Decompressor dcmp = new Decompressor();
                    dcmp.decompress();
                    break;
                case 3:
                    Updater updt= new Updater();
                    break;
                case 4:
                    for(String file: jsonFileList)
                    {
                        DataParser dp = new DataParser();
                        if (file.startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                            continue;
                        System.out.println("------- Parsing Start ------- (File: "+file+")");
                        dp.parser(jsonPath, file);
                        dp.writeJsonResult(new File(outputPath+file));  // Write parsing result to Json file

                        //Below is the code line for returning and storing parsed data list of Json file (format: List<Map<String, Object>>)
                        //To handle memory error, the data list of one Json file will be returned for each iteration of for-loop

                        //WHERE_YOU_WANT_TO_KEEP_DATA = dp.getDataList();   // Return type: List<Map<String,Object>>
                    }
                    break;
                default:
                    System.out.println("Unexpected option input!");
                    break;
            }
        }
    }
}