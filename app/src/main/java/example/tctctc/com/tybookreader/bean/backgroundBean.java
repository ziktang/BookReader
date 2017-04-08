package example.tctctc.com.tybookreader.bean;

import static android.R.attr.id;

/**
 * Created by tctctc on 2017/3/26.
 * Function:
 */

public class BackgroundBean {

    public static final int BG_IMG =1;
    public static final int BG_COLOR = 2;

    //1为图，2为颜色
    private int id;
    private int type;
    private int resource;

    public BackgroundBean(int type, int resuorce) {
        this.type = type;
        this.resource = resuorce;
    }

    public int getId() {
        return id;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResuorce() {
        return resource;
    }

    public void setResuorce(int resuorce) {
        this.resource = resuorce;
    }
}
