package cn.microanswer.flappybird;

import android.content.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

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
        File dataDirectory = android.os.Environment.getDataDirectory();
        File file = new File(dataDirectory, USERINFO_FILENAME);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 将用户信息保存到文件
     * @param user
     * @return
     */
    public static File saveUserInfo2File(USER user) {
        File dataDirectory = android.os.Environment.getDataDirectory();
        File file = new File(dataDirectory, USERINFO_FILENAME);

        if (null == user) {
            if (file.exists()) {
                return file;
            }else {
                return null;
            }
        }

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try{
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            fileOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream!=null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
