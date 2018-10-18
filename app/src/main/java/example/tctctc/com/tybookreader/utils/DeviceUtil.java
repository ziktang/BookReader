package example.tctctc.com.tybookreader.utils;

import android.text.TextUtils;

/**
 * 跟手机设备相关的工具类
 * <p>
 */

public class DeviceUtil {
    public static final String TAG = "DeviceUtil";

    private static final String OPPO_PHONE = "oppo";

    private static final String VIVO_PHONE = "vivo";

    private static final String HTC_PHONE = "htc";

    private static final String HUAWEI_PHONE = "huawei";

    /**
     * 外部应用包名
     */
    public static final String QQPackageName = "com.tencent.mobileqq";
    public static final String WeixinPackageName = "com.tencent.mm";


    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }


    /**
     * 获取手机品牌
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机生产商
     */
    public static String getPhoneProduct() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取SDK版本号
     *
     * @return
     */
    public static int getSDKVersionInt() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取android系统版本
     *
     * @return
     */
    public static String getAndroidOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    private static boolean isSameDevice(String deviceName) {
        String product = getPhoneProduct();
        if (!TextUtils.isEmpty(product) && product.toLowerCase().contains(deviceName)) {
            return true;
        }

        String brand = getPhoneBrand();
        if (!TextUtils.isEmpty(brand) && brand.toLowerCase().contains(deviceName)) {
            return true;
        }

        String model = getPhoneModel();
        if (!TextUtils.isEmpty(model) && model.toLowerCase().contains(deviceName)) {
            return true;
        }
        return false;
    }

    public static boolean isOPPODevice() {
        return isSameDevice(OPPO_PHONE);
    }

    public static boolean isVIVODevice() {
        return isSameDevice(VIVO_PHONE);
    }

    public static boolean isHtcDevice() {
        return isSameDevice(HTC_PHONE);
    }

    public static boolean isHuaweiDevice() {
        return isSameDevice(HUAWEI_PHONE);
    }

}
