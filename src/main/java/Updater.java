public class Updater {
    static private String updateDate;
    static private String[] updatedVersion={"recent", "modified"};

    public static void udpateCheck()
    {

    }

    public static void updateLog()
    {

    }

    public static void main(String[] args)
    {
        Crawler.timeLog();
        String downDir=Crawler.downloadDir+Crawler.downloadDate+"/";

        for(String ver:Crawler.updatedVersion)
        {
            String url= Crawler.defaultAddress+ver+Crawler.fileFormat;
            Crawler.downloadSetting(url, downDir);
            Crawler.fileUrlDownload(url, Crawler.fileName);
        }
    }

}
