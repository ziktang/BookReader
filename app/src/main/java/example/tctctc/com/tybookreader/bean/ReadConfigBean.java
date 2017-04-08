package example.tctctc.com.tybookreader.bean;

/**
 * Created by tctctc on 2017/3/26.
 * Function:
 */

public class ReadConfigBean {
    private int fontSize;
    private String fontType;
    private int brightness;
    private int pageTurn;
    private int background;
    private boolean isNight;

    public ReadConfigBean(int fontSize, String fontType, int brightness, int pageTurn, int background, boolean isNight) {
        this.fontSize = fontSize;
        this.fontType = fontType;
        this.brightness = brightness;
        this.pageTurn = pageTurn;
        this.background = background;
        this.isNight = isNight;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getPageTurn() {
        return pageTurn;
    }

    public void setPageTurn(int pageTurn) {
        this.pageTurn = pageTurn;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }
}
