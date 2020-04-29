package cn.microanswer.flappybird;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import org.json.JSONObject;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否支持运行
        if (!checkGL20()) {
            notSupport("您的手机不满足游戏运行的最低标准要求：不支持 OpenGL ES 2.0。");
            return;
        }

        if (android.os.Build.VERSION.SDK_INT < MINIMUM_SDK) {
            notSupport("您的手机版本过低，无法运行本游戏。最低要求 Android 版本 2.2 及以上。");
            return;
        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        FlappyBirdGame flappyBirdGame = new FlappyBirdGame().setMainActivity(this);

        // View view = initializeForView(flappyBirdGame, config);
        // setContentView(view);

        initialize(flappyBirdGame, config);
    }

    private void notSupport(String hint) {
        Intent intent = new Intent(this, NotSupportTipActivity.class);
        intent.putExtra("hint", hint);
        startActivity(intent);
        finish();
    }

    boolean checkGL20() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        int[] version = new int[2];
        egl.eglInitialize(display, version);

        int EGL_OPENGL_ES2_BIT = 4;
        int[] configAttribs = {EGL10.EGL_RED_SIZE, 4, EGL10.EGL_GREEN_SIZE, 4, EGL10.EGL_BLUE_SIZE, 4, EGL10.EGL_RENDERABLE_TYPE,
                EGL_OPENGL_ES2_BIT, EGL10.EGL_NONE};

        EGLConfig[] configs = new EGLConfig[10];
        int[] num_config = new int[1];
        egl.eglChooseConfig(display, configAttribs, configs, 10, num_config);
        egl.eglTerminate(display);
        return num_config[0] > 0;
    }

    /**
     * 检查用户是否登录，如果没有，将给出相关提示。
     */
    public void checkUserInfo() {

        if (App.user == null || TextUtils.isEmpty(App.user.getToken())) { // 没有用户信息， 表名用户没有登录
            // 弹出提示，让用户登录

            // 如果用户配置了不再弹出提示，不弹出
            if (App.config.isNeverHintLogin()) {
                return;
            }

            View inflate = getLayoutInflater().inflate(R.layout.dialog_logintip, null);
            AlertDialog alertDialog = new AlertDialog.Builder(this).setView(inflate).setTitle(getString(R.string.tipp)).create();
            alertDialog.setCancelable(false);
            final CheckBox checkbox = inflate.findViewById(R.id.checkbox);
            checkbox.setChecked(App.config.isNeverHintLogin());
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.loginnow), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    App.config.setNeverHintLogin(checkbox.isChecked());

                    jump2Login();

                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    App.config.setNeverHintLogin(checkbox.isChecked());
                }
            });
            alertDialog.show();
        }
    }

    private void jump2Login() {
        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
        intent.putExtra("url", "http://microanswer.cn/user/page/login.html");
        startActivityForResult(intent, 222);
        overridePendingTransition(R.anim.score_activity_in, R.anim.score_activity_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (222 == requestCode && tScore > 0 && !TextUtils.isEmpty(playId)) {
            // 登录成功了
            // 继续执行成绩上传。
            submitScore(tScore, playId);
        }

    }

    private int tScore = 0;
    private String playId;
    private boolean relogined; // 登录信息失效时， 标记是否重试登录， 如果重试了，不再重试

    /**
     * 提交成绩
     *
     * @param score
     */
    public void submitScore(final int score, final String playId) {
        this.playId = playId;
        tScore = score;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 获取 token
                String token = null;

                if (App.user != null) {
                    token = App.user.getToken();
                }

                // 没有 token就必须要求先登录
                if (TextUtils.isEmpty(token)) {
                    View f = getLayoutInflater().inflate(R.layout.dialog_scoreuploadtip, null);
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("提示").setView(f).create();
                    final CheckBox checkbox = f.findViewById(R.id.checkbox);
                    checkbox.setChecked(App.config.isNeverHintScoreUpload());
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.login), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.config.setNeverHintScoreUpload(checkbox.isChecked());
                            jump2Login();
                        }
                    });
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.config.setNeverHintScoreUpload(checkbox.isChecked());
                        }
                    });
                    dialog.show();
                    return;
                }

                // 有 TOKEN
                final String finalToken = token;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject();
                            data.put("score", score);
                            data.put("token", finalToken);
                            data.put("gv", Util.getVersion(MainActivity.this));
                            data.put("deviceinfo", Util.getPhoneInfo());

                            String s = HttpClientUtil.post("http://microanswer.cn/flappybird/index.php", "uploadScore", data);
                            System.out.println("成绩上传结果：" + s);
                            JSONObject jr = new JSONObject(s);
                            int code = jr.getInt("code");
                            if (code == 200) {
                                // return "success";
                            } else {
                                if (code == 502) {
                                    // return "relogin"; // 登录信息失效。需要重新登录
                                    if (!relogined) {
                                        relogin();
                                        relogined = true;
                                    }
                                } else if (code == 501) {
                                    // return "noaccount"; // 无效的登录token
                                    // 无效的登录token
                                    // 清空现有的用户信息
                                    App.user.setToken(null);
                                    App.user.setPassword(null);
                                    App.user.setAccount(null);
                                    App.user.setInfo(null);
                                    Util.saveUserInfo2File(App.user);
                                }
                            }

                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });
    }

    private void relogin() {
        try {
            JSONObject data = new JSONObject();
            data.put("name", App.user.getAccount());
            data.put("password", App.user.getPassword());
            data.put("loginType", "2");
            data.put("loginTypeName", "通过Android设备上的flappyBird游戏自动登录");

            String s = HttpClientUtil.post("http://microanswer.cn/user/index.php", "login", data);
            System.out.println("重新登录结果：" + s);
            JSONObject jr = new JSONObject(s);
            int code = jr.getInt("code");
            if (code == 200) {
                App.user.setToken(jr.getString("data"));
                Util.saveUserInfo2File(App.user);
                // 再次尝试上传成绩
                submitScore(tScore, playId);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // 屏蔽返回按钮
    }
}
