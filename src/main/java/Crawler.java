import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.text.SimpleDateFormat;


public class Crawler {

    final static int size = 1024*8;
    static private String fileName;
    static private File downloadDir;

    static private String defaultAddress= "https://nvd.nist.gov/feeds/json/cve/1.0/nvdcve-1.0-";
    static private String[] version={"2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012","2013","2014","2015","2016","2017","2018","2019","recent", "modified"};

    static private String fileFormat=".json.zip";
    static private String downloadDate;

    static private String updateUrl = "https://nvd.nist.gov/vuln/data-feeds";
    static private Map<String,String> updateMsg =  new HashMap<String, String>();
    static private Map<String, File> logFile = new HashMap<String, File>();

    public Crawler(File crawlDir)
    {
        timeLog();
        downloadDir=new File(crawlDir,downloadDate);
    }

    public static void fileUrlDownload(String fileAddress, String localFileName)
    {
        OutputStream outStream = null;
        URLConnection uCon = null;
        InputStream inStream = null;

        if(!downloadDir.exists())
            downloadDir.mkdirs();

        try
        {
            System.out.println("------- Download Start -------");
            URL url= new URL(fileAddress);
            byte[] buf;
            int byteRead;
            int byteWritten=0;

            outStream=new BufferedOutputStream(new FileOutputStream(downloadDir.getAbsolutePath()+"/"+localFileName));
            uCon=url.openConnection();
            inStream = uCon.getInputStream();
            buf = new byte[size];

            while((byteRead = inStream.read(buf)) != -1)
            {
                outStream.write(buf, 0, byteRead);
                byteWritten += byteRead;
                //outStream.flush();
                //System.out.println(localFileName+" : while loop");
            }
            System.out.println("Download Successfully!");
            System.out.println("File name: "+localFileName);
            System.out.println("Total Bytes: "+byteWritten);
            System.out.println("------- Download End -------");
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            try
            {
                inStream.close();
                outStream.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void downloadSetting(String fileAddress)
    {
        int slashIndex = fileAddress.lastIndexOf('/');
        int periodIndex= fileAddress.lastIndexOf('.');

        fileName=fileAddress.substring(slashIndex+1);

        if(!(periodIndex >= 1 && slashIndex >= 0 && slashIndex < fileAddress.length() -1))
        {
            System.err.println("path or file name Error!");
        }
    }

    public static void timeLog()
    {
        Date today=new Date();
        SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd");
        downloadDate=date.format(today);
    }

    public static void updateTimeCheck(Map<String, String> updateMsg)
    {
        Document doc = null;
        try{
            doc = Jsoup.connect(updateUrl).get();
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        Elements elements = doc.getElementsByClass("xml-feed-table table table-striped table-condensed");
        Elements body=elements.first().getElementsByTag("tbody").get(0).getElementsByTag("td");
        String cve_modified_update_time = body.get(1).text();
        String cve_recent_update_time = body.get(8).text();

        updateMsg.put("CVE_Modified", cve_modified_update_time);
        updateMsg.put("CVE_Recent", cve_recent_update_time);
    }

    public static void updateLog()
    {
        FileWriter fw_modified=null;
        FileWriter fw_recent=null;
        try{
            fw_modified=new FileWriter(logFile.get("CVE_Modified"), true);
            fw_modified.write(updateMsg.get("CVE_Modified")+"\n");
            fw_modified.flush();

            fw_recent=new FileWriter(logFile.get("CVE_Recent"), true);
            fw_recent.write(updateMsg.get("CVE_Recent")+"\n");
            fw_recent.flush();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try{
                if(fw_modified != null)
                    fw_modified.close();
                if(fw_recent != null)
                    fw_recent.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void setVersion(String[] ver)
    {
        version = ver;
    }

    public static void setLogFile(File modified_log_file, File recent_log_file)
    {
        logFile.put("CVE_Modified", modified_log_file);
        logFile.put("CVE_Recent", recent_log_file);
    }

    public static void crawl(File modified_log_file, File recent_log_file)
    {
        for(String ver:version)
        {
            String url= defaultAddress+ver+fileFormat;
            downloadSetting(url);
            fileUrlDownload(url, fileName);
        }

        System.out.println("[Crawling] All files are successfully done!");
        setLogFile(modified_log_file, recent_log_file);
        updateTimeCheck(updateMsg);
        updateLog();
    }
}
