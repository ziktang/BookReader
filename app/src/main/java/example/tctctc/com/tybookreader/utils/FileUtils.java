package example.tctctc.com.tybookreader.utils;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by tctctc on 2017/3/19.
 */

public class FileUtils {

    private FileUtils() {

    }

    private static List<File> listFilterAllFolder(File scanfile,String rex) {
        File[] files = scanfile.listFiles();
        if (files == null) return null;
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory() && !file.isHidden()) {
                fileList.addAll(listFilterAllFolder(file,rex));
            } else if (file.isFile() && !file.isHidden() && file.canRead()) {
                if (file.getName().endsWith(rex)) {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    public static List<File> listFilterOneFolder(File file, String rex) {
        List<File> files = new ArrayList(Arrays.asList(file.listFiles()));
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File file1 = iterator.next();
            if (file1.isHidden()) iterator.remove();
            else if (file1.isFile() && !file1.getName().endsWith(rex)) {
                iterator.remove();
            }
        }
        return files;
    }

    public static String getFileSize(File file) {
        DecimalFormat df = new DecimalFormat(".00");
        float si = file.length();
        if (si < 1024)
            return df.format(si) + "b";
        si /= 1024f;
        if (si > 1024f) {
            si /= 1024f;
            if (si > 1024f) {
                si /= 1024f;
                return df.format(si) + "Gb";
            }
            return df.format(si) + "mb";
        }
        return df.format(si) + "kb";
    }


    /**
     * 获取文件编码
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String getCharset(String fileName) throws IOException{
        String charset;
        FileInputStream fis = new FileInputStream(fileName);
        byte[] buf = new byte[4096];
        // (1)
        UniversalDetector detector = new UniversalDetector(null);
        // (2)
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        // (3)
        detector.dataEnd();
        // (4)
        charset = detector.getDetectedCharset();
        // (5)
        detector.reset();
        return charset;
    }
}
