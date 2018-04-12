package cn.microanswer.flappybird.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;

public class CONFIG implements Serializable {
    private static CONFIG config;
    public static CONFIG getConfig(Context context) {
        if (config==null) {
            config = new CONFIG(context);
        }
        return config;
    }
    private CONFIG (Context c) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        __init(preferences);
    }


    private SharedPreferences s;
    private boolean isNeverHintLogin = false; // 是否不再提示登录
    private boolean isNeverHintScoreUpload = false; // 是否不再提示成绩上传
    private String scoreId;

    public boolean isNeverHintLogin() {
        return isNeverHintLogin;
    }

    public void setNeverHintLogin(boolean neverHintLogin) {
        isNeverHintLogin = neverHintLogin;
        s.edit().putBoolean("isNeverHintLogin", isNeverHintLogin).commit();
    }

    public boolean isNeverHintScoreUpload() {
        return isNeverHintScoreUpload;
    }

    public void setNeverHintScoreUpload(boolean neverHintScoreUpload) {
        isNeverHintScoreUpload = neverHintScoreUpload;
        s.edit().putBoolean("isNeverHintScoreUpload", isNeverHintScoreUpload).commit();
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
        s.edit().putString("scoreId", scoreId).commit();
    }

    private void __init(SharedPreferences s) {
        this.s = s;
        isNeverHintLogin = s.getBoolean("isNeverHintLogin", false);
        isNeverHintScoreUpload = s.getBoolean("isNeverHintScoreUpload", false);
        scoreId = s.getString("scoreId", null);
    }
}
