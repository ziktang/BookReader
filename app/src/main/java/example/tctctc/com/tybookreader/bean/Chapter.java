package example.tctctc.com.tybookreader.bean;

/**
 * Created by tctctc on 2017/3/28.
 * Function:
 */

public class Chapter {
    private String name;
    private String path;
    private int startPosition;
    private int endPosition;

    public Chapter(String name, int startPosition) {
        this.name = name;
        this.startPosition = startPosition;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
}
