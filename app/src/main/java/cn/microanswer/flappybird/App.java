package cn.microanswer.flappybird;

import android.app.Application;

import org.json.JSONObject;

public class App extends Application {
    public static USER user = null;

    public static class USER {
        private static USER user;

        private String account;
        private String password;

        private String token;

        private JSONObject info;

        public static USER getUSer() {
            if (null == user) {
                user = new USER();
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

        public void setInfo(JSONObject info) {
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

        public JSONObject getInfo() {
            return info;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (user == null) {
            user = USER.getUSer();
        }
    }
}
