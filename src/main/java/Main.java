import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    /* ************************************************************************ */
    /* ************* Modify below path for your own convenience!! ************* */
    static private String rootPath="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/";
    /* ************************************************************************ */

    // Parameter Setting for Crawler Class
    static private File crawlingDir=new File(rootPath+"/ZIP/");
    //End of Crawler Setting

    // Parameter Setting for Decompressor Class
    static private File zipFileDir=crawlingDir;
    static private File decompressDir=new File(rootPath+"/JSON/");
    //End of Decompressor Setting

    // Parameter Setting for Updater Class
    static private File updateDir = crawlingDir;
    static private File updateLogPath = new File(rootPath+"/update_log/");
    static private File cve_modified_update_log_file = new File(updateLogPath,"cve_modified_update_log.txt");
    static private File cve_recent_update_log_file = new File(updateLogPath,"cve_recent_update_log.txt");
    //End of Updater Setting

    // Parameter Setting for DataParser Class
    static private File jsonDataDir=new File(rootPath+"/JSON/");
    static private File parsedOutputDir=new File(rootPath+"/output/");  //parsed output directory

    static private File cweCSV=new File(rootPath+"/CWE/merged/2000.csv");  //CWE CSV file directory
    static private File similarCheckDir = new File(jsonDataDir, "2019-03-21");  //data parsing directory of getSimilar() method
    static private File cweToCveCSV = new File(rootPath+"/CWE/merged/","cwe-cve.csv");
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
                    // WHERE YOU WANT TO GET DATA = dp.getDataMap();
                    break;
                case 5:
                    DataParser checker = new DataParser(similarCheckDir, parsedOutputDir, cweCSV, cweToCveCSV);
                    checker.parseJson();
                    Map<String, Map<String, Object>> map= checker.getSimilar("CVE-2019-0001"); // 연관취약점 Top 10 리턴
                    System.out.println(map);
                    break;
                default:
                    System.out.println("Unexpected option input!");
                    break;
            }
        }
    }
}
