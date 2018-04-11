package cn.microanswer.flappybird;

import android.app.Application;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import cn.microanswer.flappybird.other.CONFIG;
import cn.microanswer.flappybird.other.USER;

public class App extends Application {
    public static USER user = null;
    public static CONFIG config = null;

    @Override
    public void onCreate() {
        super.onCreate();
        user = USER.getUSer();
        config = CONFIG.getConfig(this);
    }
}
