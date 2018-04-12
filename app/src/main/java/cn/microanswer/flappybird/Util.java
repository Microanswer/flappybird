package cn.microanswer.flappybird;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.UUID;

import cn.microanswer.flappybird.other.USER;

/**
 * Created by Micro on 2018-2-10.
 */

public class Util {

    private static String USERINFO_FILENAME = "userinfo.data";


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
     * 跳转到成绩排行界面
     *
     * @param context
     */
    public static void jump2ScoreActivity(final MainActivity context, String url) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.score_activity_in, R.anim.score_activity_out);
    }

    /**
     * 获取用户信息文件
     * <div style="width: 300px;">
     * 用户信息再用户首次登录过后被缓存到手机内存存储空间中， 以对象的方式直接写入文件。
     * 读到的这个文件可将其转换为user对象, 如果返回为空，说明用户没有登录过。
     * </div>
     *
     * @return
     */
    public static File getUserInfoFile() {
        File dataDirectory = new File(android.os.Environment.getExternalStorageDirectory(), "flappyBirdd");
        File file = new File(dataDirectory, USERINFO_FILENAME);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 将用户信息保存到文件
     *
     * @param user
     * @return
     */
    public static File saveUserInfo2File(USER user) {
        File dataDirectory = new File(android.os.Environment.getExternalStorageDirectory(), "flappyBirdd");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        File file = new File(dataDirectory, USERINFO_FILENAME);

        if (null == user) {
            if (file.exists()) {
                return file;
            } else {
                return null;
            }
        }

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
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
