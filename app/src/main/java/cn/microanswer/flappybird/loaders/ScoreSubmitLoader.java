package cn.microanswer.flappybird.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import org.json.JSONObject;

import cn.microanswer.flappybird.HttpClientUtil;

public class ScoreSubmitLoader extends AsyncTaskLoader<Object> {
    public static final int LOADER_SUBMIT_SCORE = 123;

    private Bundle args;

    public ScoreSubmitLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        int score = args.getInt("score");
        String token = args.getString("token");
        String gameversion = args.getString("gameversion");
        String deviceinfo = args.getString("deviceinfo");
        String scoreId = args.getString("scoreId");

        try {
            JSONObject data = new JSONObject();
            data.put("score", score);
            data.put("token", token);
            data.put("scoreId", scoreId);
            data.put("gv", gameversion.replaceAll("\\.", "_"));
            data.put("deviceinfo", deviceinfo);

            String s = HttpClientUtil.post("http://microanswer.cn/flappybird/index.php", "uploadScore", data);

            JSONObject jr = new JSONObject(s);
            int code = jr.getInt("code");
            if (code == 200) {
                return "success";
            } else {
                if (code == 502) {
                    return "relogin"; // 登录信息失效。需要重新登录
                } else if (code == 501) {
                    return "noaccount"; // 无效的登录token
                }
            }

            return new JSONObject(s);
        } catch (Throwable e) {
            e.printStackTrace();
            return e;
        }
    }
}