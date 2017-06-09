package example.tctctc.com.tybookreader.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import example.tctctc.com.tybookreader.R;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

/**
 * Created by tctctc on 2017/4/3.
 * Function:设置状态栏颜色，透明悬浮，全屏
 */

public class StatusBarUtils {


    public static void setColor(AppCompatActivity activity, @ColorRes int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21(activity, color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19(activity, color);
        }
    }

    /**
     * 思路:直接设置状态栏的颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarUpperAPI21(AppCompatActivity activity, @ColorRes int color) {
        Window window = activity.getWindow();
        //取消设置悬浮透明状态栏,ContentView便不会进入状态栏的下方了
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //设置状态栏颜色
        window.setStatusBarColor(activity.getResources().getColor(color));
    }

    /**
     * 思路:设置状态栏悬浮透明，然后制造一个和状态栏等尺寸的View设置好颜色填进去，就好像是状态栏着色了一样
     */
    private static void setStatusBarUpperAPI19(AppCompatActivity activity, @ColorRes int color) {
        Window window = activity.getWindow();
        //设置悬浮透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        int statusBarHeight = getStatusBarHeight(activity);
        int statusColor = activity.getResources().getColor(color);

        View mTopView = mContentView.getChildAt(0);
        if (mTopView != null && mTopView.getLayoutParams() != null &&
                mTopView.getLayoutParams().height == statusBarHeight) {
            mTopView.setBackgroundColor(statusColor);
            return;
        }

        //制造一个和状态栏等尺寸的 View
        mTopView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        mTopView.setBackgroundColor(statusColor);
        //将view添加到第一个位置
        mContentView.addView(mTopView, 0, lp);
    }

    /**
     * 获取设备的状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }


    /**
     * 设置透明悬浮状态栏,让内容延伸进去
     *
     * @param activity
     */
    public static void setTranslucent(AppCompatActivity activity) {
        //5.0以上手动设置statusBar为透明。不能使用getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //因为这在4.4,5.0确实是全透明，但是在6.0是半透明!!!
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        );
    }


    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullScreen(AppCompatActivity activity) {
        //4.1及以上通用flags组合
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

}
