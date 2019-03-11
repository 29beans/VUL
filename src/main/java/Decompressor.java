import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Decompressor {

    static String macFileDir="/Users/GyuMac/Desktop/회사/멘토링/data/ZIP/";
    static String winFileDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/ZIP/";
    static String fileDir=winFileDir;

    static String macOutputDir="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static String winOutputDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/";
    static String outputDir=winOutputDir;

    static String[] folderList;
    static String[] zipFileList;

    public static void decompress(File zipFile, String directoryPath) throws Throwable
    {
        FileInputStream fis =null;
        ZipInputStream zis = null;
        ZipEntry ze = null;

        try{
            fis= new FileInputStream(zipFile);
            zis= new ZipInputStream(fis);

            while((ze = zis.getNextEntry()) != null)
            {
                String fileName=ze.getName();
                File file = new File(directoryPath, fileName);
                //System.out.println(fileName);

                if(ze.isDirectory())
                {
                    file.mkdirs();
                }else
                {
                    createFile(file, zis);
                }
            }
        }catch(Throwable e)
        {
            throw e;
        }finally
        {
            if(zis != null)
                zis.close();
            if(fis != null)
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

    private static String[] readTargetFileList(String path)
    {
        File dir=new File(path);
        return dir.list();
    }

    public static void main(String[] args)
    {
        folderList=readTargetFileList(fileDir);

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
