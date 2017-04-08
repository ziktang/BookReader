package example.tctctc.com.tybookreader.common;

import android.util.Log;

import java.io.File;
import java.util.Comparator;

/**
 * Created by tctctc on 2017/3/20.
 */

public class FileComparater implements Comparator<File> {
    @Override
    public int compare(File lhs, File rhs) {
        try {
            if (lhs.isFile() && rhs.isFile()) {
                return lhs.length() > rhs.length() ? -1 : 1;
            } else if (!lhs.isFile() && rhs.isFile()) {
                return 1;
            } else if (lhs.isFile() && !rhs.isFile()) {
                return -1;
            } else {
                return lhs.getName().compareTo(rhs.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("eee", "compare");
            return 0;
        }
    }
}
