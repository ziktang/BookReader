package example.tctctc.com.tybookreader.common.barlibrary;

import android.text.TextUtils;
import android.util.Log;
import example.tctctc.com.tybookreader.utils.DeviceUtil;

/**
 * 沉浸式状态栏工具类
 *
 * Created by hzj on 2018/6/25.
 */

public class ImmersionBarUtil {
    public static final String TAG = "ImmersionBarUtil";

    private static final String NEXUS_5 = "Nexus 5";

    public static boolean isSupportWhiteStatusBarDevice() {
        String model = DeviceUtil.getPhoneModel();
        if (!TextUtils.isEmpty(model) && model.contains(NEXUS_5)) {
            Log.i(TAG,"Nexus 5，不支持白色沉浸式状态栏");
            return false;
        }

        if (DeviceUtil.isHtcDevice()) {
            Log.i(TAG,"HTC手机，不支持白色沉浸式状态栏");
            return false;
        }
        return true;
    }

}
