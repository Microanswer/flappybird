package cn.microanswer.flappybird.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONObject;

import cn.microanswer.flappybird.App;
import cn.microanswer.flappybird.HttpClientUtil;
import cn.microanswer.flappybird.Util;

public class LoginLoader extends AsyncTaskLoader<Object> {
    public static final int LOADER_LOGIN = 124;


    public LoginLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Object loadInBackground() {

        try {
            JSONObject data = new JSONObject();
            data.put("name", App.user.getAccount());
            data.put("password", App.user.getPassword());
            data.put("loginType", "2");
            data.put("loginTypeName", "通过Android设备上的flappyBird游戏自动登录");

            String s = HttpClientUtil.post("http://microanswer.cn/user/index.php",
//            String s = HttpClientUtil.post("http://127.0.0.1/flappybird/index.php",
                    "login", data);

            JSONObject jr = new JSONObject(s);
            int code = jr.getInt("code");
            if (code == 200) {
                App.user.setToken(jr.getString("data"));
                Util.saveUserInfo2File(App.user);
                return "success";
            }

            return new JSONObject(s);
        } catch (Throwable e) {
            e.printStackTrace();
            return e;
        }
    }
}