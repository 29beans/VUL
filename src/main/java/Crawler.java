import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;


public class Crawler {

    final static int size = 1024*8;
    static String fileName;
    static String macDownloadDir="/Users/GyuMac/Desktop/회사/멘토링/data/ZIP/";
    static String winDownloadDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/ZIP/";
    static String downloadDir=winDownloadDir;

    static String defaultAddress= "https://nvd.nist.gov/feeds/json/cve/1.0/nvdcve-1.0-";
    static String[] version={"2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012","2013","2014","2015","2016","2017","2018","2019","recent", "modified"};
    static String[] updatedVersion={"recent", "modified"};
    static String fileFormat=".json.zip";

    static String downloadDate;


    public static void fileUrlDownload(String fileAddress, String localFileName)
    {
        OutputStream outStream = null;
        URLConnection uCon = null;
        InputStream inStream = null;

        File dir= new File(downloadDir);
        if(!dir.exists())
            dir.mkdirs();

        try
        {
            System.out.println("------- Download Start -------");
            URL url= new URL(fileAddress);
            byte[] buf;
            int byteRead;
            int byteWritten=0;

            outStream=new BufferedOutputStream(new FileOutputStream(downloadDir+localFileName));
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

    public static void downloadSetting(String fileAddress, String downDir)
    {
        int slashIndex = fileAddress.lastIndexOf('/');
        int periodIndex= fileAddress.lastIndexOf('.');

        fileName=fileAddress.substring(slashIndex+1);
        downloadDir=downDir;

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
    public static void main(String[] args) {

        timeLog();
        String downDir=downloadDir+downloadDate+"/";

        for(String ver:version)
        {
            String url= defaultAddress+ver+fileFormat;
            downloadSetting(url, downDir);
            fileUrlDownload(url, fileName);

        }
    }
}
