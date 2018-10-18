package example.tctctc.com.tybookreader.bean;

import java.io.File;

/**
 * Created by tctctc on 2017/6/9.
 * Function:
 */

public class ScanFile {
    private File file;
    private boolean isImported;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

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

    public ScanFile(File file, boolean isImported) {
        this.file = file;
        this.isImported = isImported;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScanFile){
            ScanFile scanFile = (ScanFile) obj;
            if (file==null || !file.exists() || scanFile.getFile() == null || !scanFile.getFile().exists() ){
                return false;
            }else{
                return file.getAbsolutePath().equals(scanFile.getFile().getAbsolutePath());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (file!=null && file.exists()){
            return file.getAbsolutePath().hashCode();
        }else{
            return super.hashCode();
        }
    }

    public ScanFile() {

    }

    @Override
    public String toString() {
        return "ScanFile{" +
                "file=" + file +
                ", isImported=" + isImported +
                ", isChecked=" + isChecked +
                '}';
    }
}
