package example.tctctc.com.tybookreader.bean;

import java.lang.ref.WeakReference;

/**
 * Created by tctctc on 2017/3/28.
 * Function:
 */

public class TxtCache {
    private WeakReference<String> content;
    private int size;

    public TxtCache(WeakReference<String> content, int size) {
        this.content = content;
        this.size = size;
    }

    public WeakReference<String> getContent() {
        return content;
    }

    public void setContent(WeakReference<String> content) {
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
