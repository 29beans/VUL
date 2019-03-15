import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Decompressor {

    static private String macFileDir="/Users/GyuMac/Desktop/회사/멘토링/data/ZIP/";
    static private String winFileDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/ZIP/";
    static private File fileDir=new File(winFileDir);

    static private String macOutputDir="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static private String winOutputDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/";
    static private File outputDir=new File(winOutputDir);

    static private File[] folderList;
    static private File[] zipFileList;

    public static void decompress()
    {
        for(File zipFile:zipFileList)
        {
            try {
                unzip(zipFile, new File(outputDir+zipFile.getParentFile().getName()+"/"));
                System.out.println("Decompressed!: "+ zipFile.getAbsolutePath());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void setting()
    {
        folderList=fileDir.listFiles();
        for(File folder: folderList)
        {
            if(!folder.isDirectory())
            {
                zipFileList=folder.listFiles();
            }
        }
    }
    public static void unzip(File zipFile, File unzipDir) throws Throwable
    {
        FileInputStream fis =null;
        ZipInputStream zis = null;
        ZipEntry ze = null;

        try {
            fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(fis);

            while ((ze = zis.getNextEntry()) != null) {
                String fileName = ze.getName();
                File file = new File(unzipDir, fileName);
                //System.out.println(fileName);

                if (ze.isDirectory()) {
                    file.mkdirs();
                } else {
                    createFile(file, zis);
                }
            }
        } catch (Throwable e) {
            throw e;
        } finally {
            if (zis != null)
                zis.close();
            if (fis != null)
                fis.close();
        }
    }

    private static void createFile(File file, ZipInputStream zis) throws Throwable
    {
        File parentDir = new File(file.getParent());

        if(!parentDir.exists())
        {
            System.out.println("Parent Dir Missing!");
            parentDir.mkdirs();
        }

        try(FileOutputStream fos = new FileOutputStream(file))
        {
            byte[] buffer = new byte[256];
            int size=0;

            while((size = zis.read(buffer))>0)
            {
                fos.write(buffer, 0, size);
            }
        }catch(Throwable e)
        {
            throw e;
        }
    }




    public static void main(String[] args)
    {
        for(String folderName:folderList)
        {
            File dir=new File(outputDir+folderName);

            if(!dir.isDirectory())
            {
                zipFileList=readTargetFileList(fileDir+folderName);

                for(String zipFileName:zipFileList)
                {
                    File zipFile = new File(fileDir+folderName+"/"+zipFileName);

                    try {
                        decompress(zipFile, outputDir+folderName+"/");
                        System.out.println("Decompressed!: "+ zipFile.getAbsolutePath());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
