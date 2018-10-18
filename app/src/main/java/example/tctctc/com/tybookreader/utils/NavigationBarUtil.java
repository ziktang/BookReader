package example.tctctc.com.tybookreader.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Method;

/**
 * Function:
 * Created by tanchao on 2018/10/13.
 */

public class NavigationBarUtil {

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            possiblyResizeChildOfContent();
        }
    };

    public NavigationBarUtil() {

    }

    public static void fitActivity(final View content) {
        new NavigationBarUtil().fit(content);
    }

    private void fit(final View content) {
        mChildOfContent = content;
        final Context context = content.getContext();
        ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                int navigationBarIsMin = Settings.System.getInt(context.getContentResolver(),"navigationbar_is_min", 0);
                //导航键隐藏了
                if (navigationBarIsMin == 1) {
                    Log.e("导航键隐藏了", "-----");
                    mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
                } else {//导航键显示了
                    Log.e("导航键显示了", "-----");
                    mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
                }
            }
        };
        context.getContentResolver().registerContentObserver(Settings.System.getUriFor("navigationbar_is_min"), true, mNavigationStatusObserver);

    }

    private void possiblyResizeChildOfContent() {
        ViewGroup.LayoutParams frameLayoutParams = mChildOfContent.getLayoutParams();
        int usableHeightNow = computeUsableHeight(mChildOfContent);
        if (usableHeightNow != usableHeightPrevious) {
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private static int computeUsableHeight(View view) {
        Rect r = new Rect();
        view.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }
}
