import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class test {

    public static void main(String[] args) {
        /*
        Map<String, Map<String, Object>> m = new HashMap<String, Map<String, Object>>();
        Map<String, Object> n=new HashMap<String, Object>();
        n.put("a", new HashSet<String>());
        m.put("a", n);
        ((HashSet<String>)n.get("a")).add("123");
        System.out.println(m.get("a"));
        System.out.println(m.get("a").get("a"));
        */
        /*
        String a = "abcd";
        String b = new String("abcd");

        if (a == b)
            System.out.println("True");

        if (a.equals(b))
            System.out.println("equal");

        Map<String, String> map = new HashMap<String, String>();
        map.put("abcd", "ABCD");
        map.put(b, "aa");

        System.out.println(map.get("abcd"));
        System.out.println(map.get(a));
        System.out.println(map.get(b));
        System.out.println(map);
        */

        /*
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.out.println(list.toArray());
        System.out.println(list.toString());
        */
        /*
        Map<String, String> map = new HashMap<>();
        map.put("a","hello");
        map.put("b", "hi");

        for(String key:map.keySet())
        {
            key="z";
        }

        Map<String, String> mmap=map;
        for(String key:map.keySet())
        {
            mmap.replace(key,key+"AAA");
        }

        System.out.println(map);
        System.out.println(mmap);
        */
        /*
        Set<String> a = new HashSet<String>();
        a.add("a");
        Map<String, String> map = new HashMap<>();
        map.put("a","A");
        map.put("b","B");

        a.addAll(map.keySet());

        System.out.println(a);*/

        File csv = new File("C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/reference/MICI.csv");

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            InputStreamReader reader2 = new InputStreamReader(new FileInputStream(csv), "UTF-8");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            CSVParser csvParser2 = new CSVParser(reader2, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            int i=0;
            String text1=null;
            String text2=null;

            for(CSVRecord csvRecord:csvParser)
            {
                //String text=csvRecord.get(3);
                if(i==1)
                    break;
                text1=csvRecord.get(0);
                if(text1.isEmpty()) {
                    System.out.println("null true");
                }else
                {
                    System.out.println(text1);
                }
                i++;
            }
            i=0;
            for(CSVRecord csvRecord:csvParser2)
            {
                if(i==1)
                    break;
                text2=csvRecord.get(0);
                if(text2.isEmpty()) {
                    System.out.println("null true");
                }else
                {
                    System.out.println(text2);
                }
                i++;
            }

            System.out.println(text1 == text2);
            System.out.println((text1.equals(text2)));

            Map<String, String> map = new HashMap<>();
            map.put(text1, "aaa");
            System.out.println(map.get(text2));

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
