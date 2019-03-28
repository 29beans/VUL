import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    //Below are the hard-coded absolute paths for json file input and output
    //Modify the paths for your own convenience (Marked: ***)

    // Parameter Setting for Crawler Class
    static private String macDownloadDir="/Users/GyuMac/Desktop/회사/멘토링/data/ZIP/";
    static private String winDownloadDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/ZIP/";  //***
    static private File crawlingDir=new File(winDownloadDir);
    //End of Crawler Setting

    // Parameter Setting for Decompressor Class
    static private String macFileDir=macDownloadDir;
    static private String winFileDir=winDownloadDir;
    static private File zipFileDir=new File(winFileDir);

    static private String macOutputDir="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static private String winOutputDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/"; //***
    static private File decompressDir=new File(winOutputDir);
    //End of Decompressor Setting

    // Parameter Setting for Updater Class
    static private File updateDir = crawlingDir;
    static private String macUpdateLogPath ="/Users/GyuMac/Desktop/회사/멘토링/data/";
    static private String winUpdateLogPath = "C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/Update_Log/"; //***
    static private File updateLogPath = new File(winUpdateLogPath);
    static private File cve_modified_update_log_file = new File(updateLogPath,"cve_modified_update_log.txt");
    static private File cve_recent_update_log_file = new File(updateLogPath,"cve_recent_update_log.txt");
    //End of Updater Setting

    // Parameter Setting for DataParser Class
    static private String macDataPath="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static private String winDataPath="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/"; //***
    static private File jsonDataDir=new File(winDataPath);

    static private String macOutputPath="/Users/GyuMac/Desktop/회사/멘토링/data/output/";
    static private String winOutputPath = "C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/output/"; //***
    static private File parsedOutputDir=new File(winOutputPath);  //parsed output directory

    static private String macCWEcsv="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/CWE/";
    static private String winCWEcsv="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/CWE/merged/2000.csv"; //***
    static private File cweCSV=new File(winCWEcsv);  //CWE CSV file directory

    static private File similarCheckDir = new File(jsonDataDir, "2019-03-21");  //data parsing directory of getSimilar() method 
    static private File cweToCveCSV = new File(cweCSV.getParentFile(),"cwe-cve.csv");
    //End of DataParser Setting

    private static int menu()
    {
        Scanner reader = new Scanner(System.in);
        showMenuScreen();

        return reader.nextInt();
    }

    private static void makePaths()
    {
        if(!crawlingDir.exists())
            crawlingDir.mkdirs();
        if(!zipFileDir.exists())
            zipFileDir.mkdirs();
        if(!decompressDir.exists())
            decompressDir.mkdirs();
        if(!updateLogPath.exists())
            updateLogPath.mkdirs();
        if(!jsonDataDir.exists())
            jsonDataDir.mkdirs();
        if(!parsedOutputDir.exists())
            parsedOutputDir.mkdirs();
        if(!cweCSV.getParentFile().exists())
            cweCSV.getParentFile().mkdirs();
    }

    private static void showMenuScreen()
    {
        System.out.println("------------------- Menu Screen -------------------");
        System.out.println("Press the number to execute an appropriate module.");
        System.out.println("1. Crawl json data feeds from NVD website");
        System.out.println("2. Decompress json zip files");
        System.out.println("3. Update the new data feeds from NVD");
        System.out.println("4. Parse the downloaded json data feeds");
        System.out.println("5. Get similar vulnerability instances with CVE-ID you enter");
        System.out.println("6. Exit");
        System.out.println("---------------------------------------------------");
        System.out.print("Enter: ");
    }

    public static void main(String[] args)
    {
        makePaths();
        while(true)
        {
            int opt = menu();
            if(opt == 6)   // 'Exit' selected
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
                    DataParser dp = new DataParser(jsonDataDir, parsedOutputDir, cweCSV, cweToCveCSV);
                    dp.parse();
                    break;
                case 5:
                    DataParser checker = new DataParser(similarCheckDir, parsedOutputDir, cweCSV, cweToCveCSV);
                    Map<String, Map<String, Object>> map= checker.getSimilar("CVE-2018-0001");   // 연관취약점 Top 10 리턴
                    //System.out.println(map.keySet());
                    //System.out.println(map.toString());
                    break;
                default:
                    System.out.println("Unexpected option input!");
                    break;
            }
        }
    }
}
