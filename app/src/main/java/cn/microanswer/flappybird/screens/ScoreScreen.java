package cn.microanswer.flappybird.screens;

import static cn.microanswer.flappybird.FlappyBirdGame.HEIGHT;
import static cn.microanswer.flappybird.FlappyBirdGame.WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.Random;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.Util;
import cn.microanswer.flappybird.sprites.Btn;

/**
 * Created by Micro on 2022年2月14日15:13:37。
 * 成绩面板。
 */

public class ScoreScreen extends BaseScreen implements Btn.OnClickListener {

    Label.LabelStyle labelStyle;

    public ScoreScreen(OrthographicCamera camera, Batch batch) {
        super(camera, batch);

        // TextureRegion tr = new TextureRegion()
        // BitmapFont bf = new BitmapFont()
        // labelStyle = new Label.LabelStyle()
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }


    @Override
    public void dispose() { }

    @Override
    public void onClick(Btn btn) {

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

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

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
