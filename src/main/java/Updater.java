import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Updater {
    static private String updateDate;
    static private String[] updatedVersion={"recent", "modified"};
    static private String url = "https://nvd.nist.gov/vuln/data-feeds";


    public static void udpateCheck()
    {

    }

    public static void updateLog() throws IOException
    {
        Document doc = Jsoup.connect(url).get();
//        System.out.println(doc.text());
        Elements elements = doc.getElementsByClass("xml-feed-table table table-striped table-condensed");
//        String ele = doc.getElementByID("row").className();
//        System.out.println(ele);
       // System.out.println(ele.text());
//        System.out.println(elements.size());
        Elements body=elements.first().getElementsByTag("tbody").get(0).getElementsByTag("td");
        //System.out.println(body.text());

        for(Element bd:body)
        {
            System.out.println(bd.text());
        }
        //System.out.println(elements.first().getElementsByTag("td").text());

        for(Element element: elements)
        {
            //System.out.println(element.text());
        }
    }


    public static void main(String[] args)
    {
        try {
            updateLog();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
