import java.util.ArrayList;
import com.google.gson.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Data {

    private String CVE_ID;
    private ArrayList<VendorData> vendorData;
    private Map<String, Object> CWE;
    private ArrayList<String> description;
    private ArrayList<String> cpe23Uri;

    private Map<String, Object> map;

    public Data() {
        vendorData = new ArrayList<VendorData>();
        CWE=new HashMap<String, Object>();    // key : value --> CWE_ID : vulnerability name
        description=new ArrayList<String>();
        cpe23Uri = new ArrayList<String>();

        map=new HashMap<String, Object>();
    }

    public void setCVE_ID(String ID) {
        this.CVE_ID = ID;
    }

    public void addVendorData(JsonArray ja)
    {
        for(JsonElement je: ja)
        {
            String vendorName=je.getAsJsonObject().get("vendor_name").getAsString();
            VendorData vData = new VendorData(vendorName);
            vData.addProductData(je.getAsJsonObject().get("product").getAsJsonObject().get("product_data").getAsJsonArray());
            vendorData.add(vData);
        }
    }

    public void addCWE(JsonArray ja, Map<String,Object> cweInfo)
    {
        for(JsonElement je: ja)
        {
            JsonArray ja_next=je.getAsJsonObject().get("description").getAsJsonArray();

            for(JsonElement je_next: ja_next)
            {
                String CWE_ID=je_next.getAsJsonObject().get("value").getAsString();
                CWE.put(CWE_ID, cweInfo.get(CWE_ID));
            }
        }
    }

    public void addDescription(JsonArray ja)
    {
        for(JsonElement je: ja)
        {
            String dsct=je.getAsJsonObject().get("value").getAsString();
            description.add(dsct);
        }
    }

    public void addCpe23Uri(JsonArray ja)
    {
        for(JsonElement je: ja)
        {
            String operator=je.getAsJsonObject().get("operator").getAsString();
            //System.out.println("operator = "+operator);
            JsonArray ja_next=null;
            //System.out.println(getCVE_ID());

            if(operator.equals("AND"))
            {
                if(je.getAsJsonObject().get("children") == null)
                    return;
                else
                    ja_next = je.getAsJsonObject().get("children").getAsJsonArray();
                addCpe23Uri(ja_next);
                return;
            }else if(operator.equals("OR"))
            {
                if(je.getAsJsonObject().get("cpe_match") == null)
                    return;
                else
                    ja_next = je.getAsJsonObject().get("cpe_match").getAsJsonArray();
            }else
            {
                System.out.println("Unrecognized Operator or Empty Nodes");
                return;
            }

            for(JsonElement je_next: ja_next)
            {
                String cpe=je_next.getAsJsonObject().get("cpe23Uri").getAsString();
                cpe23Uri.add(cpe);
            }
        }
    }

    public Map<String, Object> createMap()
    {
        map.put("CVE_ID", this.CVE_ID);
        map.put("Vendor_Data", this.vendorData);
        map.put("CWE_ID", this.CWE);
        map.put("Description", this.description);
        map.put("CPE_2.3_Uri", this.cpe23Uri);

        return map;
    }

    public String getCVE_ID()
    {
        return CVE_ID;
    }

    public ArrayList<VendorData> getVendorData()
    {
        return vendorData;
    }


    public ArrayList<String> getCpe23Uri()
    {
        return cpe23Uri;
    }

    // private classes within Data class
    private class VendorData
    {
        private String vendorName;
        private ArrayList<ProductData> productData;

        public VendorData(String vendorName)
        {
            this.vendorName=vendorName;
            productData=new ArrayList<ProductData>();
        }

        public void addProductData(JsonArray ja)
        {
            for(JsonElement je : ja)
            {
                String productName=je.getAsJsonObject().get("product_name").getAsString();
                ProductData pData = new ProductData(productName);
                pData.addVersionData(je.getAsJsonObject().get("version").getAsJsonObject().get("version_data").getAsJsonArray());
                productData.add(pData);
            }
        }

        public String getVendorName()
        {
            return vendorName;
        }

        public ArrayList<ProductData> getProductData()
        {
            return productData;
        }

        public VendorData getThis()
        {
            return this;
        }
    }

    private class ProductData{
        private String productName;
        private ArrayList<VersionData> versionData;

        public ProductData(String productName)
        {
            this.productName=productName;
            versionData = new ArrayList<VersionData>();
        }

        public void addVersionData(JsonArray ja)
        {
            for(JsonElement je : ja)
            {
                String version_value=je.getAsJsonObject().get("version_value").getAsString();
                String version_affected=je.getAsJsonObject().get("version_affected").getAsString();
                VersionData vData = new VersionData(version_value, version_affected);
                versionData.add(vData);
            }
        }

        public String getProductName()
        {
            return productName;
        }

        public ArrayList<VersionData> getVersionData()
        {
            return versionData;
        }

        public ProductData getThis()
        {
            return this;
        }
    }

    private class VersionData{
        private String version_value;
        private String version_affected;

        public VersionData(String version_value, String version_affected)
        {
            this.version_value = version_value;
            this.version_affected=version_affected;
        }

        public String getVersion()
        {
            return version_value;
        }

        public String getVersion_affected()
        {
            return version_affected;
        }

        public VersionData getThis()
        {
            return this;
        }
    }
}
