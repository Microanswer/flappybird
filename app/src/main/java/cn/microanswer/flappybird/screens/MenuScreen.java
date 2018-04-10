package cn.microanswer.flappybird.screens;

import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import java.util.Date;
import java.util.Random;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.Util;
import cn.microanswer.flappybird.dialogs.AlertDialog;
import cn.microanswer.flappybird.dialogs.LoadingDialog;
import cn.microanswer.flappybird.sprites.Btn;

import static cn.microanswer.flappybird.FlappyBirdGame.HEIGHT;
import static cn.microanswer.flappybird.FlappyBirdGame.WIDTH;

/**
 * Created by Micro on 2018-2-21.
 */

public class MenuScreen extends BaseScreen implements Btn.OnClickListener {

    private Animation<TextureAtlas.AtlasRegion> birdAnimation;
    private float birdW, birdH, birdX, birdY;
    private float titleX, titleY, titleW, titleH;
    private float groundX, groundH, groundW;
    private TextureAtlas.AtlasRegion bg;
    private float runTime;
    private Btn btnRate, btnPlay, btnScore;
    private Stage stage;

    public MenuScreen(OrthographicCamera camera, Batch batch) {
        super(camera, batch);

        Gdx.input.setInputProcessor(this);
        stage = new Stage(getViewport(), batch);

        if (Util.isDay()) {
            bg = (MAssetsManager.instance().bg_day);
        } else {
            bg = (MAssetsManager.instance().bg_night);
        }

        runTime = 0;

        btnPlay = new Btn().init(this);
        btnPlay.setOnClickListener(this);
        btnPlay.setRegion(MAssetsManager.instance().btnPlay);
        btnPlay.setPosition((WIDTH - (238f / 288f * FlappyBirdGame.WIDTH)) / 2f, (102f / 512f * HEIGHT));

        btnScore = new Btn().init(this);
        btnScore.setOnClickListener(this);
        btnScore.setRegion(MAssetsManager.instance().btnScore);
        float x = btnPlay.getX() + (238f / 288f * FlappyBirdGame.WIDTH) - btnScore.getWidth();
        btnScore.setPosition(x, btnPlay.getY());

        btnRate = new Btn().initSmall(this);
        btnRate.setOnClickListener(this);
        btnRate.setRegion(MAssetsManager.instance().btnRate);
        btnRate.setPosition((WIDTH - btnRate.getWidth()) / 2f, btnPlay.getY() + btnPlay.getHeight() + btnRate.getHeight() / 2f);

        int index = new Random().nextInt(3);
        birdAnimation = new Animation<>(0.13f,
                MAssetsManager.instance().bird[index][0],
                MAssetsManager.instance().bird[index][1],
                MAssetsManager.instance().bird[index][2],
                MAssetsManager.instance().bird[index][1]);
        birdAnimation.setPlayMode(Animation.PlayMode.LOOP);
        birdW = 0.16666667f;
        birdH = 0.16666667f;
        birdX = (WIDTH - birdW) / 2f;
        birdY = btnRate.getY() + btnRate.getHeight() + btnRate.getHeight() / 3;

        titleW = 178f / 288f * WIDTH;
        titleH = 48f / 512f * HEIGHT;
        titleX = (WIDTH - titleW) / 2f;
        titleY = birdY + birdH + btnRate.getHeight() / 3;

        groundH = FlappyBirdGame.HEIGHT * .21875f;
        groundW = WIDTH;
        groundX = 0;

        stage.addActor(btnPlay);
        stage.addActor(btnScore);
        stage.addActor(btnRate);
    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        runTime += delta;
        batch.begin();
        batch.draw(bg, 0, 0, WIDTH, HEIGHT);
        batch.draw(MAssetsManager.instance().title, titleX, titleY, titleW, titleH);
        TextureAtlas.AtlasRegion keyFrame = birdAnimation.getKeyFrame(runTime);
        batch.draw(keyFrame, birdX, birdY, birdW, birdH);
        if (groundX <= -WIDTH) {
            groundX = 0;
        }
        batch.draw(MAssetsManager.instance().ground, groundX, 0, groundW, groundH);
        batch.draw(MAssetsManager.instance().ground, groundX + groundW, 0, groundW, groundH);
        groundX += -.4f * delta;
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();

    }

    @Override
    public void onClick(Btn btn) {
        if (btnPlay == btn) {
            // 开始游戏
            game.setScreen(new GameScreen(camera, batch).setGame(game));
        } else if (btnScore == btn) {
            // 跳转成绩排行
            Util.jump2ScoreActivity(game.getMainActivity(),"http://microanswer.cn/flappybird/scorebord.html?t=" + new Date().getDay());
        } else if (btnRate == btn) {

            AlertDialog alertDialog = new AlertDialog(this, "");
            alertDialog.show();

        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (btnRate != null) btnRate.touchDown(screenX, screenY, pointer, button);
        if (btnPlay != null) btnPlay.touchDown(screenX, screenY, pointer, button);
        if (btnScore != null) btnScore.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (btnRate != null) btnRate.touchUp(screenX, screenY, pointer, button);
        if (btnPlay != null) btnPlay.touchUp(screenX, screenY, pointer, button);
        if (btnScore != null) btnScore.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (btnRate != null) btnRate.touchDragged(screenX, screenY, pointer);
        if (btnPlay != null) btnPlay.touchDragged(screenX, screenY, pointer);
        if (btnScore != null) btnScore.touchDragged(screenX, screenY, pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
