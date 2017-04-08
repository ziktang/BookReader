package example.tctctc.com.tybookreader.utils;

import android.content.Context;
import android.graphics.Typeface;

import static android.R.attr.path;

/**
 * Created by tctctc on 2017/4/4.
 * Function:
 */

public class FontUtils {
    public void getTypeByPath(Context context,String typePath){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),typePath);
    }
}
