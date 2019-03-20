import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.input.ReversedLinesFileReader;


public class Updater {
    static private String updateDate;
    static private String[] updatedVersion = {"recent", "modified"};
    static private String updateUrl = "https://nvd.nist.gov/vuln/data-feeds";
    static private File cve_modified_log_file;
    static private File cve_recent_log_file;
    static private Map<String, String> updateMsg =  new HashMap<String, String>();
    static private String lastModifiedUpdateTime;
    static private String lastRecentUpdateTime;

    public Updater(File modified_log_file, File recent_log_file) {
        cve_modified_log_file = modified_log_file;
        cve_recent_log_file = recent_log_file;
    }

    public static boolean updateNecessary()
    {
        Crawler.updateTimeCheck(updateMsg);
        try{
            ReversedLinesFileReader tailReader1 = new ReversedLinesFileReader(cve_modified_log_file, Charset.forName("UTF-8"));
            ReversedLinesFileReader tailReader2 = new ReversedLinesFileReader(cve_recent_log_file, Charset.forName("UTF-8"));
            lastModifiedUpdateTime = tailReader1.readLine();
            lastRecentUpdateTime = tailReader2.readLine();

        }catch(IOException e)
        {
            e.printStackTrace();
        }

        if(updateMsg.get("CVE_Modified").equals(lastModifiedUpdateTime) && updateMsg.get("CVE_Recent").equals(lastRecentUpdateTime))
            return true;
        else
            return false;
    }

    public static void update(File updateDir)
    {
        if(updateNecessary()) {
            Crawler crwl = new Crawler(updateDir);
            crwl.setVersion(updatedVersion);
            crwl.crawl(cve_modified_log_file, cve_recent_log_file);
        }
    }
}
