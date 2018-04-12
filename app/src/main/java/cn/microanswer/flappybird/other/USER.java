package cn.microanswer.flappybird.other;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import cn.microanswer.flappybird.Util;

public class USER implements Serializable {
    private static USER user;

    private String account;
    private String password;

    private String token;

    private String info;

    public static USER getUSer() {
        if (null == user) {
            // 尝试读取本地缓存的用户信息
            File userInfoFile = Util.getUserInfoFile();
            if (userInfoFile != null) {
                // 只有文件不为空的时候才是有信息的。
                FileInputStream fileInputStream = null;
                ObjectInputStream objectInputStream = null;
                try {
                    fileInputStream = new FileInputStream(userInfoFile);
                    objectInputStream = new ObjectInputStream(fileInputStream);
                    user = (USER) objectInputStream.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return user;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getInfo() {
        return info;
    }
}