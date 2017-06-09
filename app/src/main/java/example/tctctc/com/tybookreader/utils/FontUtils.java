package example.tctctc.com.tybookreader.utils;

import android.content.Context;
import android.graphics.Typeface;

import static android.R.attr.path;

/**
 * Created by tctctc on 2017/4/4.
 * Function:
 */

public class FontUtils {
    private static Typeface iconFont;

    public static void init(Context context){
        iconFont = Typeface.createFromAsset(context.getAssets(),"font/iconfont.ttf");
    }

    public static Typeface getIconfont(){
        return iconFont;
    }

    public void getTypeByPath(Context context,String typePath){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),typePath);
    }
}
