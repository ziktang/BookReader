package example.tctctc.com.tybookreader.bookshelf.common;

import java.util.List;

/**
 * Created by tctctc on 2017/4/4.
 * Function:
 */

public class PageTxt {
    //前开后闭 包括start，不包括end
    private int start;
    private int end;
    private String chapterName;
    private List<String> lines;

    public PageTxt(int start, List<String> lines) {
        this.start = start;
        this.lines = lines;
    }

    public PageTxt() {
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
