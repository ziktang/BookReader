package example.tctctc.com.tybookreader.bookshelf.common;

import android.content.Context;
import example.tctctc.com.tybookreader.bean.ReadConfigBean;
import example.tctctc.com.tybookreader.utils.SPUtils;

/**
 * Created by tctctc on 2017/3/26.
 * Function:所有阅读相关的配置操作
 */

public class ReadConfig {

    //保存着所有阅读配置的bean，每次先读到这个对象里，退出时再持久化
    private static ReadConfigBean mConfigBean;

    //配置的名字
    private static final String READ_SETTING = "read_setting";
    private SPUtils mSPUtils;

    /****
     * 字体
     ****/

    //键值
    private static final String FONT_SIZE = "font_size";
    private static final String FONT_TYPE = "font_type";

    //供选择的字体类型
    public static final String FONT_TYPE_DEFAULT = "default";
    public static final String FONT_TYPE_1 = "font/chinaMatch.ttf";
    public static final String FONT_TYPE_2 = "font/gg.ttf";
    public static final String FONT_TYPE_3 = "font/yygmb.ttf";

    //默认字体大小,类型
    public static final int DEFAULT_FONT_SIZE = 20;
    public static final String DEFAULT_FONT_TYPE = FONT_TYPE_DEFAULT;

    public static final int FONT_SIZE_MAX = 24;
    public static final int FONT_SIZE_MIN = 12;


    /****
     * 亮度
     ****/

    //键值
    private static final String BRIGHTNESS = "brightness";

    //默认亮度，是否跟随系统
    public static final int DEFAULT_BRIGHTNESS = -1;

    public static final int BRIGHTNESS_MAX = 255;
    public static final int BRIGHTNESS_MIN = 1;

    /****
     * 翻页
     ****/

    //键值
    private static final String PAGE_TURN_TYPE = "page_turn_type";

    //定义的翻页模式
    public static final int TURN_NO = 1;
    public static final int TURN_COVER = 2;
    public static final int TURN_PAPER = 3;
    public static final int TURN_SLIDE = 4;

    //默认的翻页类型
    private static final int DEFAULT_PAGE_TURN_TYPE = TURN_COVER;

    /****
     * 背景
     ****/

    //键值
    private static final String BACKGROUND = "background";

    //定义的背景模式
    public static final int BG_PAGER = -1;   //仿真纸
    public static final int BG_GRAY = 1;        //颜色1
    public static final int BG_GREEN = 2;        //颜色2
    public static final int BG_YELLOW = 3;        //颜色3
    public static final int BG_WHITE = 4;   //白色
    public static final int BG_NIGHT = 6;   //夜间

    //默认的背景
    public static final int DEFAULT_BACKGROUND = BG_GRAY;


    /****
     * 夜/日间模式
     ****/
    private static final String IS_NIGHT = "is_night";

    private static final boolean DEFAULT_IS_NIGHT = false;


    public ReadConfig(Context context) {
        if (mSPUtils == null)
            mSPUtils = new SPUtils(context, READ_SETTING);
    }

    public void saveConfig() {
        mSPUtils.saveInt(FONT_SIZE, mConfigBean.getFontSize());
        mSPUtils.saveString(FONT_TYPE, mConfigBean.getFontType());
        mSPUtils.saveInt(BRIGHTNESS, mConfigBean.getBrightness());
        mSPUtils.saveInt(PAGE_TURN_TYPE, mConfigBean.getPageTurn());
        mSPUtils.saveInt(BACKGROUND, mConfigBean.getBackground());
        mSPUtils.saveBoolean(IS_NIGHT, mConfigBean.isNight());
    }


    public ReadConfigBean getConfigBean() {
        if (mConfigBean == null) {
            mConfigBean = new ReadConfigBean(getFontSize(), getFontType(), getBrightness(), getPageTurnType(), getBackGround(), isNight());
        }
        return mConfigBean;
    }

    public int getFontSize() {
        return mSPUtils.getInt(FONT_SIZE, DEFAULT_FONT_SIZE);
    }

    public String getFontType() {
        return mSPUtils.getString(FONT_TYPE, DEFAULT_FONT_TYPE);
    }

    public int getPageTurnType() {
        return mSPUtils.getInt(PAGE_TURN_TYPE, DEFAULT_PAGE_TURN_TYPE);
    }

    public int getBrightness() {
        return mSPUtils.getInt(BRIGHTNESS, DEFAULT_BRIGHTNESS);
    }

    public int getBackGround() {
        return mSPUtils.getInt(BACKGROUND, DEFAULT_BACKGROUND);
    }

    public boolean isNight() {
        return mSPUtils.getBoolean(IS_NIGHT, DEFAULT_IS_NIGHT);
    }

}
