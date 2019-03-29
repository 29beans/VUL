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
    private String updateDate;
    private String[] updatedVersion = {"recent", "modified"};
    private String updateUrl = "https://nvd.nist.gov/vuln/data-feeds";
    private File cve_modified_log_file;
    private File cve_recent_log_file;
    private Map<String, String> updateMsg =  new HashMap<String, String>();
    private String lastModifiedUpdateTime;
    private String lastRecentUpdateTime;

    public Updater(File modified_log_file, File recent_log_file) {
        cve_modified_log_file = modified_log_file;
        cve_recent_log_file = recent_log_file;
    }

    public boolean updateNecessary()
    {
        (new Crawler()).updateTimeCheck(updateMsg);

        try{
            ReversedLinesFileReader tailReader1 = new ReversedLinesFileReader(cve_modified_log_file, Charset.forName("UTF-8"));
            ReversedLinesFileReader tailReader2 = new ReversedLinesFileReader(cve_recent_log_file, Charset.forName("UTF-8"));
            lastModifiedUpdateTime = tailReader1.readLine();
            lastRecentUpdateTime = tailReader2.readLine();

        }catch(IOException e)
        {
            e.printStackTrace();
        }

        if(!(updateMsg.get("CVE_Modified").equals(lastModifiedUpdateTime)) || !(updateMsg.get("CVE_Recent").equals(lastRecentUpdateTime)))
            return true;
        else
            return false;
    }

    public void update(File updateDir)
    {
        if(updateNecessary()) {
            Crawler crwl = new Crawler(updateDir);
            crwl.setVersion(updatedVersion);
            crwl.crawl(cve_modified_log_file, cve_recent_log_file);
        }else
        {
            System.out.println("Data feeds are up-to-date! No updates are required!");
        }
    }
}
