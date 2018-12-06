package example.tctctc.com.tybookreader.bookstore.bean;

/**
 * Function:
 * Created by tanchao on 2018/10/19.
 */

public class SearchBook {

//    public static final int UPDATE_STATUS_DONE = 1;
//    public static final int UPDATE_STATUS_UPDATING = 2;

    private String site;
    private String bookName;
    private String author;
    private String bookHome;
    private String latestChapterName;
    private String latestChapterHome;
    private String updateStatus;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookHome() {
        return bookHome;
    }

    public void setBookHome(String bookHome) {
        this.bookHome = bookHome;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getLatestChapterName() {
        return latestChapterName;
    }

    public void setLatestChapterName(String latestChapterName) {
        this.latestChapterName = latestChapterName;
    }

    public String getLatestChapterHome() {
        return latestChapterHome;
    }

    public void setLatestChapterHome(String latestChapterHome) {
        this.latestChapterHome = latestChapterHome;
    }

    @Override
    public String toString() {
        return "SearchBook{" +
                "site='" + site + '\'' +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", bookHome='" + bookHome + '\'' +
                ", latestChapterName='" + latestChapterName + '\'' +
                ", latestChapterHome='" + latestChapterHome + '\'' +
                ", updateStatus=" + updateStatus +
                '}';
    }
}
