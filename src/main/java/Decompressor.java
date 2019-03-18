import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;


public class Decompressor {

    static private String macFileDir="/Users/GyuMac/Desktop/회사/멘토링/data/ZIP/";
    static private String winFileDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/ZIP/";
    static private File fileDir=new File(macFileDir);

    static private String macOutputDir="/Users/GyuMac/Desktop/회사/멘토링/data/JSON/";
    static private String winOutputDir="C:/Users/2019_NEW_07/Desktop/과제_멘토링/data/JSON/";
    static private File outputDir=new File(macOutputDir);

    static private File[] folderList;
    static private ArrayList<File[]> zipFileList = new ArrayList<>();

    public static void decompress()
    {
        decompressSetting();

        for(File[] zipFiles:zipFileList)
        {
            String newFolderName = zipFiles[0].getParentFile().getName();

            for(File zipFile: zipFiles) {

                if(zipFile.getName().startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                    continue;

                try {
                    unzip(zipFile, new File(outputDir, newFolderName));
                    System.out.println("Decompressed!: " + zipFile.getAbsolutePath());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void decompressSetting()
    {
        folderList=fileDir.listFiles();

        for(File folder: folderList)
        {
            if(folder.getName().startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                continue;
            //System.out.println(folder.getName());

            File outputDirFolder=new File(outputDir, folder.getName());
            if(!outputDirFolder.isDirectory())
            {
                zipFileList.add(folder.listFiles());
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
                //System.out.println(fileName);
                if(fileName.startsWith(".")) //To deal with file error in MacOS (.DS_STORE)
                    continue;

                File file = new File(unzipDir, fileName);

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
            System.out.println("Create Path : "+parentDir.getAbsolutePath()+"\n");
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
}
