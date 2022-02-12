package cn.microanswer.flappybird;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

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

        FlappyBirdGame flappyBirdGame = new FlappyBirdGame();

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

    @Override
    public void onBackPressed() {
        // 屏蔽返回按钮
    }
}
