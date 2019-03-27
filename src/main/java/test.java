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

        ArrayList<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.out.println(list.toArray());
        System.out.println(list.toString());

    }
}
