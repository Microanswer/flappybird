package cn.microanswer.flappybird.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

import cn.microanswer.flappybird.FlappyBirdGame;

/**
 * Created by Micro on 2018-2-21.
 */

public abstract class BaseScreen implements Screen {

    protected Batch batch;
    protected OrthographicCamera camera;
    protected FlappyBirdGame game;

    public BaseScreen(OrthographicCamera camera, Batch batch) {
        this.batch = batch;
        this.camera = camera;
    }

    public BaseScreen setGame(FlappyBirdGame game) {
        this.game = game;
        return this;
    }

    public FlappyBirdGame getGame() {
        return game;
    }

    public Batch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
