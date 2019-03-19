import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    //Below are the hard-coded absolute paths for json file input and output
    //Modify the paths for your own convenience

    // Parameter Setting for Crawler Class
    static private String macDownloadDir="/Users/GyuMac/Desktop/회사/멘토링/data/ZIP/";
    static private String winDownloadDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/ZIP/";
    static private File crawlingDir=new File(winDownloadDir);
    //End

    // Parameter Setting for Decompressor Class
    static private String macFileDir=macDownloadDir;
    static private String winFileDir=winDownloadDir;
    static private File zipFileDir=new File(winFileDir);

    static private String macOutputDir="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static private String winOutputDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/";
    static private File decompressDir=new File(winOutputDir);
    //End

    // Parameter Setting for Updater Class
    static private File updateDir = crawlingDir;
    static private String macUpdateLogPath ="/Users/GyuMac/Desktop/회사/멘토링/data/";
    static private String winUpdateLogPath = "C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/";
    static private String updateLogPath = winUpdateLogPath;
    static private File cve_modified_update_log_file = new File(updateLogPath+"cve_modified_update_log.txt");
    static private File cve_recent_update_log_file = new File(updateLogPath+"cve_recent_update_log.txt");
    //End

    // Parameter Setting for DataParser Class
    static String macDataPath="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static String winDataPath="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/";
    static String dataPath=winDataPath;

    static String macOutputPath="/Users/GyuMac/Desktop/회사/멘토링/data/output/";
    static String winOutputPath = "C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/output/";
    static String outputPath=winOutputPath;
    //End

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
        while(true)
        {
            int opt = menu();
            if(opt == 5)
                break;

            switch (opt)
            {
                case 1:
                    Crawler crwl = new Crawler(crawlingDir);
                    crwl.crawl(cve_modified_update_log_file, cve_recent_update_log_file);
                    break;
                case 2:
                    Decompressor dcmp = new Decompressor(zipFileDir, decompressDir);
                    dcmp.decompress();
                    break;
                case 3:
                    Updater updt= new Updater(cve_modified_update_log_file, cve_recent_update_log_file);
                    updt.update(updateDir);
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
                    }
                    break;
                default:
                    System.out.println("Unexpected option input!");
                    break;
            }
        }
    }
}