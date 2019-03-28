import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Reference {

    static private String macCSVdir="/Users/GyuMac/Desktop/reference/";
    static private String winCSVdir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/reference";
    static private File referenceCSVdir= new File(winCSVdir);

    static private Map<String, Map<String, Object>> refMap = new HashMap<>(); // refName: {refID: class(record)}
    static private Map<String, Map<String, String>> cweRefMap= new HashMap<>(); // cwe: {refName: refID}

    private class SDSG
    {
        private String id;
        private String name;
        private String CWE_ID;
        private String description;
        private String comments;

        public SDSG(String id, String name, String description, String comments)
        {
            this.id=id;
            this.name=name;
            this.description=description;
            this.comments=comments;
        }
    }

    private class EGOV
    {
        private String id;
        private String code;
        private String name;
        private String description;
        private String comments;

        public EGOV(String id, String code, String name, String description, String comments)
        {
            this.id=id;
            this.code=code;
            this.name=name;
            this.description=description;
            this.comments=comments;
        }
    }

    private class KISA
    {
        private String id;
        private String code;
        private String name;
        private String description;
        private String comments;

        public KISA(String id, String code, String name, String description, String comments)
        {
            this.id=id;
            this.code=code;
            this.name=name;
            this.description=description;
            this.comments=comments;
        }
    }

    private class EFSR
    {
        private String id;
        private String evaluationCriteria;
        private String description;
        private String notes;
        private String vul_ID_web;
        private String vul_ID_mob;
        private String vul_ID_hts;
        private String subNum;
        private String category;
        private String detailedCategory;

        public EFSR(String id, String evaluationCriteria, String description, String notes,
                    String vul_ID_web, String vul_ID_mob, String vul_ID_hts,
                    String subNum, String category, String detailedCategory)
        {
            this.id=id;
            this.evaluationCriteria=evaluationCriteria;
            this.notes=notes;
            this.description=description;
            this.vul_ID_web=vul_ID_web;
            this.vul_ID_mob=vul_ID_mob;
            this.vul_ID_hts=vul_ID_hts;
            this.subNum=subNum;
            this.category=category;
            this.detailedCategory=detailedCategory;
        }
    }

    private class MICI
    {
        private String id;
        private String code;
        private String name;
        private String description;
        private String comments;

        public MICI(String id, String code, String name, String description, String comments)
        {
            this.id=id;
            this.code=code;
            this.name=name;
            this.description=description;
            this.comments=comments;
        }
    }

    private class NIS8
    {
        private String id;
        private String code;
        private String name;
        private String description;
        private String comments;

        public NIS8(String id, String code, String name, String description, String comments)
        {
            this.id=id;
            this.code=code;
            this.name=name;
            this.description=description;
            this.comments=comments;
        }
    }

    private class OW17
    {
        private String id;
        private String code;
        private String name;
        private String notes;

        public OW17(String id, String code, String name, String notes)
        {
            this.id=id;
            this.code=code;
            this.name=name;
            this.notes=notes;
        }
    }

    public Map<String, Object> readSDSG()
    {
        File csv= new File(referenceCSVdir, "SDSG.csv");
        Map<String, Object> map = new HashMap<>(); //ID:record

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(!csvRecord.get(0).isEmpty()) {
                    SDSG temp = new SDSG(csvRecord.get(0), csvRecord.get(1), csvRecord.get(3), csvRecord.get(4));
                    map.put(csvRecord.get(0), temp);
                }

            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Object> readEGOV()
    {
        File csv= new File(referenceCSVdir, "EGOV.csv");
        Map<String, Object> map = new HashMap<>(); //ID:record

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(!csvRecord.get(0).isEmpty()) {
                    EGOV temp = new EGOV(csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(4), csvRecord.get(5));
                    map.put(csvRecord.get(0), temp);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Object> readKISA()
    {
        File csv= new File(referenceCSVdir, "KISA.csv");
        Map<String, Object> map = new HashMap<>(); //ID:record

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(!csvRecord.get(0).isEmpty()) {
                    KISA temp = new KISA(csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(4), csvRecord.get(5));
                    map.put(csvRecord.get(0), temp);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Object> readEFSR()
    {
        File csv= new File(referenceCSVdir, "EFSR.csv");
        Map<String, Object> map = new HashMap<>(); //ID:record

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(!csvRecord.get(0).isEmpty()) {
                    EFSR temp = new EFSR(csvRecord.get(0), csvRecord.get(1), csvRecord.get(3), csvRecord.get(4), csvRecord.get(5),
                            csvRecord.get(6), csvRecord.get(7), csvRecord.get(8), csvRecord.get(9), csvRecord.get(10));
                    map.put(csvRecord.get(0), temp);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Object> readMICI()
    {
        File csv= new File(referenceCSVdir, "MICI.csv");
        Map<String, Object> map = new HashMap<>();    //ID:record

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(!csvRecord.get(0).isEmpty()) {
                    MICI temp = new MICI(csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(4), csvRecord.get(5));
                    map.put(csvRecord.get(0), temp);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Object> readNIS8()
    {
        File csv= new File(referenceCSVdir, "NIS8.csv");
        Map<String, Object> map = new HashMap<>(); //ID:record

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(!csvRecord.get(0).isEmpty()) {
                    NIS8 temp = new NIS8(csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(4), csvRecord.get(5));
                    map.put(csvRecord.get(0), temp);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Object> readOW17()
    {
        File csv= new File(referenceCSVdir, "OW17.csv");
        Map<String, Object> map = new HashMap<>(); //ID:record

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(!csvRecord.get(0).isEmpty()) {
                    OW17 temp = new OW17(csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(4));
                    map.put(csvRecord.get(0), temp);
                }
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, Map<String, Object>> readRefMap()
    {
        refMap.put("SDSG", readSDSG());
        refMap.put("EGOV", readEGOV());
        refMap.put("KISA", readKISA());
        refMap.put("MICI", readMICI());
        refMap.put("NIS8", readNIS8());
        refMap.put("OW17", readOW17());
        refMap.put("EFSR", readEFSR());

        return refMap;
    }

    public Map<String, Map<String, String>> readCweReference()
    {
        File csv = new File(referenceCSVdir, "reference_domestic(id).csv");

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(csv), "euc-kr");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()))
        {
            for(CSVRecord csvRecord:csvParser)
            {
                if(csvRecord.get("CWE").isEmpty())
                    continue;
                Map<String, String> map = new HashMap<>();
                if(!csvRecord.get("SDSG").isEmpty())
                    map.put("SDSG", csvRecord.get("SDSG"));
                if(!csvRecord.get("KISA").isEmpty())
                    map.put("KISA", csvRecord.get("KISA"));
                if(!csvRecord.get("EGOV").isEmpty())
                    map.put("EGOV", csvRecord.get("EGOV"));
                if(!csvRecord.get("EFSR").isEmpty())
                    map.put("EFSR", csvRecord.get("EFSR"));
                if(!csvRecord.get("MICI").isEmpty())
                    map.put("MICI", csvRecord.get("MICI"));
                if(!csvRecord.get("NIS8").isEmpty())
                    map.put("NIS8", csvRecord.get("NIS8"));

                cweRefMap.put(csvRecord.get("CWE").toUpperCase(), map);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return cweRefMap;
    }
}
