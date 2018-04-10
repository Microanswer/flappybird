package cn.microanswer.flappybird;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import org.json.JSONObject;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends AndroidApplication implements LoaderManager.LoaderCallbacks<Object> {
    private final int LOADER_SUBMIT_SCORE = 1;
    private LoaderManager loaderManager;

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


        // 创建异步处理器
        loaderManager = getLoaderManager();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        FlappyBirdGame flappyBirdGame = new FlappyBirdGame().setMainActivity(this);

        View view = initializeForView(flappyBirdGame, config);
        setContentView(view);

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
     * 提交成绩
     *
     * @param score
     */
    public void submitScore(final int score) {

        // 获取 token
        final String token = App.user.getToken();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // 没有 token就必须要求先登录
                if (TextUtils.isEmpty(token)) {
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("提示").create();
                    dialog.setMessage("上传成绩，参与全球排行榜需要先登录。现在进行登录吗？");
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Util.jump2ScoreActivity(MainActivity.this, "http://microanswer.cn/user/page/login.html");
                        }
                    });
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "关闭", (DialogInterface.OnClickListener) null);
                    dialog.show();
                    return;
                }


                Bundle bundle = new Bundle();
                bundle.putInt("score", score);

                if (loaderManager.getLoader(LOADER_SUBMIT_SCORE) == null) {
                    loaderManager.initLoader(LOADER_SUBMIT_SCORE, bundle, MainActivity.this);
                } else {
                    loaderManager.restartLoader(LOADER_SUBMIT_SCORE, bundle, MainActivity.this);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        // 屏蔽返回按钮
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_SUBMIT_SCORE) {
            return new ScoreSubmitLoader(this, args);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
        if (loader instanceof ScoreSubmitLoader) {
            ScoreSubmitLoader loader1 = (ScoreSubmitLoader) loader;
            loader1.reset();
        }

    }

    private class ScoreSubmitLoader extends AsyncTaskLoader<Object> {
        private Bundle args;

        public ScoreSubmitLoader(Context context, Bundle args) {
            super(context);
            this.args = args;
        }

        @Override
        public Object loadInBackground() {
            int score = args.getInt("score");
            String token = args.getString("token");

            try {
                JSONObject data = new JSONObject();
                data.put("score", score);
                data.put("token", token);
                String s = HttpClientUtil.post("http://microanswer.cn/flappybird/index.php",
                        "uploadScore", data);
                return new JSONObject(s);
            } catch (Throwable e) {
                return e;
            }
        }
    }
}
