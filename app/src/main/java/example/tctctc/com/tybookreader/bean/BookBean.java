package example.tctctc.com.tybookreader.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


import java.io.Serializable;

import example.tctctc.com.tybookreader.utils.CustomUUId;

/**
 * Created by tctctc on 2017/3/18.
 */

@Entity
public class BookBean implements Serializable {

    public static final long serialVersionUID = 1;

    @Id(autoincrement = true)
    private Long bookId;
    private String bookName;
    private String path;
    private String fileSize;
    private long length;
    //1 表示在书架上  2表示已移出书架(但此时数据库仍然有缓存)
    private int status;
    //书本类型，本地书 1 在线书 2
    private int  mBookType;
    private String charset;
    private int progress;
    @Generated(hash = 1685938753)
    public BookBean(Long bookId, String bookName, String path, String fileSize,
            long length, int status, int mBookType, String charset, int progress) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.path = path;
        this.fileSize = fileSize;
        this.length = length;
        this.status = status;
        this.mBookType = mBookType;
        this.charset = charset;
        this.progress = progress;
    }
    @Generated(hash = 269018259)
    public BookBean() {
    }
    public Long getBookId() {
        return this.bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public long getLength() {
        return this.length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getMBookType() {
        return this.mBookType;
    }
    public void setMBookType(int mBookType) {
        this.mBookType = mBookType;
    }
    public String getCharset() {
        return this.charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    public int getProgress() {
        return this.progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
}

