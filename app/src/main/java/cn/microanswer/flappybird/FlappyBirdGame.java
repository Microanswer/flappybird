package cn.microanswer.flappybird;

import android.content.Context;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

import cn.microanswer.flappybird.screens.BaseScreen;
import cn.microanswer.flappybird.screens.LogoScreen;
import cn.microanswer.flappybird.screens.MenuScreen;
import cn.microanswer.flappybird.sprites.RestartFlashActor;

/**
 * 像素鸟游戏主类
 * Created by Micro on 2018-2-5.
 */

public class FlappyBirdGame extends Game {
    private MainActivity mainActivity;

    public FlappyBirdGame setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        return this;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    private static final float WIDTH$HEIGHT = .5625f;
    public static final float WIDTH = 1f;
    public static final float HEIGHT = WIDTH / WIDTH$HEIGHT;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private RestartFlashActor screenChangeActor;
    private boolean isAssetsLoaded;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_NONE);
        isAssetsLoaded = false;
        MAssetsManager.instance().init();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        setScreen(new LogoScreen(camera, batch).setGame(this));
    }

    // 资源加载完成时调用
    private void loadOk() {
        MAssetsManager.instance().d0WhenLoaded();
        screenChangeActor = new RestartFlashActor().init(null);
        RunnableAction d = new RunnableAction();
        d.setRunnable(new Runnable() {
            @Override
            public void run() {
                                MAssetsManager.instance().playSound(
                        MAssetsManager.instance().funSound
                );
                setScreen(new MenuScreen(camera, batch).setGame(FlappyBirdGame.this));
            }
        });
        screenChangeActor.addAction(Actions.delay(1f, d));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();

        boolean isLoaded = MAssetsManager.instance().getAssetManager().update();

        if (!isAssetsLoaded) {
            Screen screen = getScreen();
            if (screen != null && screen instanceof LogoScreen) {
                float loadProgress = MAssetsManager.instance().getAssetManager().getProgress();
                LogoScreen screen1 = (LogoScreen) screen;
                screen1.setProgress(loadProgress);
            }
            if (isLoaded) {
                isAssetsLoaded = true;
                loadOk();
            }
        }
        if (screenChangeActor != null) {
            screenChangeActor.act(Gdx.graphics.getDeltaTime());
            batch.begin();
            screenChangeActor.draw(batch, 1f);
            batch.end();
        }

        // 绘制弹出框舞台
        if (screen != null && screen instanceof BaseScreen) {
            BaseScreen baseScreen = (BaseScreen) screen;
            baseScreen.renderDialogStage(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void setScreen(final Screen screen) {
        if (screenChangeActor == null) {
            super.setScreen(screen);
            return;
        }
        screenChangeActor.d0Restart(new Runnable() {
            @Override
            public void run() {
                FlappyBirdGame.super.setScreen(screen);
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
