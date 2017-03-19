package example.tctctc.com.tybookreader.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tctctc on 2017/3/19.
 */

public class FileUtils {

    private FileUtils() {

    }

    public static List<File> listFiles(File file) {
        List<File> files = new ArrayList<>(Arrays.asList(file.listFiles()));
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File file1 = iterator.next();
            if (file1.isHidden()) iterator.remove();
            else if (file1.isFile() && !file1.getName().endsWith(".txt")) {
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
}
