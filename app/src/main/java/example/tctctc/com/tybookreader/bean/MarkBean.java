package example.tctctc.com.tybookreader.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import example.tctctc.com.tybookreader.utils.CustomUUId;

import static example.tctctc.com.tybookreader.bean.MarkBeanDao.Properties.Id;

/**
 * Created by tctctc on 2017/4/21.
 * Function:书签
 */

@Entity
public class MarkBean implements Serializable{

    public static final long serialVersionUID = 2;

    @Id
    private long id;
    private long bookId;
    private String time;
    private String BookName;
    private String ChapterName;
    private String describe;
    private int position;
    private String progress;
    @Generated(hash = 1169022335)
    public MarkBean(long id, long bookId, String time, String BookName,
            String ChapterName, String describe, int position, String progress) {
        this.id = id;
        this.bookId = bookId;
        this.time = time;
        this.BookName = BookName;
        this.ChapterName = ChapterName;
        this.describe = describe;
        this.position = position;
        this.progress = progress;
    }
    @Generated(hash = 1968029817)
    public MarkBean() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getBookId() {
        return this.bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getBookName() {
        return this.BookName;
    }
    public void setBookName(String BookName) {
        this.BookName = BookName;
    }
    public String getChapterName() {
        return this.ChapterName;
    }
    public void setChapterName(String ChapterName) {
        this.ChapterName = ChapterName;
    }
    public String getDescribe() {
        return this.describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public String getProgress() {
        return this.progress;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }
    

}
