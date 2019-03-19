import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


public class Updater {
    static private String updateDate;
    static private String[] updatedVersion = {"recent", "modified"};
    static private String url = "https://nvd.nist.gov/vuln/data-feeds";
    static private File cve_modified_log_file;
    static private File cve_recent_log_file;

    public Updater(File modified_log_file, File recent_log_file) {
        cve_modified_log_file = modified_log_file;
        cve_recent_log_file = recent_log_file;
    }

    public static void update(File updateDir)
    {
        Crawler crwl = new Crawler(updateDir);
        crwl.setVersion(updatedVersion);
        crwl.crawl(cve_modified_log_file, cve_recent_log_file);
    }
}
