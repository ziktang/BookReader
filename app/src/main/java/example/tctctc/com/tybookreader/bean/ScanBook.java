package example.tctctc.com.tybookreader.bean;

import java.io.File;

/**
 * Created by tctctc on 2017/6/9.
 * Function:
 */

public class ScanBook {
    private File file;
    private boolean isImported;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isImported() {
        return isImported;
    }

    public void setImported(boolean imported) {
        isImported = imported;
    }

    public ScanBook(File file, boolean isImported) {
        this.file = file;
        this.isImported = isImported;
    }

    public ScanBook() {
    }
}
