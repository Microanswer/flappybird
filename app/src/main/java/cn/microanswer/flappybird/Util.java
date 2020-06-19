package cn.microanswer.flappybird;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Micro on 2018-2-10.
 */

public class Util {


    /**
     * 返回是白天还是夜晚
     *
     * @return
     */
    public static boolean isDay() {
        int hours = new Date().getHours();
        return 8 <= hours && hours <= 19;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static String getPhoneInfo() {
        String phoneInfo = "";

        try {
            phoneInfo = new JSONObject()
                    .put("系统定制商", Build.BRAND)
                    .put("主板", Build.BOARD)
                    .put("系统启动程序版本号", Build.BOOTLOADER)
                    .put("设备参数", Build.DEVICE)
                    .put("屏幕参数", Build.DISPLAY)
                    .put("唯一参数", Build.FINGERPRINT)
                    .put("硬件名称", Build.HARDWARE)
                    .put("硬件制造商", Build.MANUFACTURER)
                    .put("版本号", Build.MODEL)
                    .put("产品名称", Build.PRODUCT)
                    .put("无线电固件版本", Build.getRadioVersion())
                    .put("硬件序列号", Build.SERIAL)
                    .put("SDK", Build.VERSION.SDK)
                    .put("SDK_INT", Build.VERSION.SDK_INT)
                    .put("固件版本", Build.VERSION.RELEASE)
                    .put("CODENAME", Build.VERSION.CODENAME)
                    .put("基带版本", Build.VERSION.INCREMENTAL)
                    .toString();
        } catch (Exception e) {
        }
        return phoneInfo;
    }

    /**
     * 获取一个随机字符串
     *
     * @return
     */
    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }
}
