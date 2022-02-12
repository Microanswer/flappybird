package cn.microanswer.flappybird.screens;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.dialogs.Dialog;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static cn.microanswer.flappybird.FlappyBirdGame.HEIGHT;
import static cn.microanswer.flappybird.FlappyBirdGame.WIDTH;

/**
 * Created by Micro on 2018-2-21.
 */

public abstract class BaseScreen extends InputAdapter implements Screen {

    protected Batch batch;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected FlappyBirdGame game;
//    private Stage dialogStage; // 弹出框舞台

    public BaseScreen(OrthographicCamera camera, Batch batch) {
        this.batch = batch;
        this.camera = camera;
        this.viewport =  new ScalingViewport(Scaling.fill, WIDTH, HEIGHT, camera);

//        dialogStage = new Stage(this.viewport, batch);
    }

    public BaseScreen setGame(FlappyBirdGame game) {
        this.game = game;
        return this;
    }

    @Override
    public void resize(int width, int height) {
        viewport.apply(true);
    }

//    public FlappyBirdGame getGame() {
//        return game;
//    }

    public Batch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

//    public void renderDialogStage (float delay) {
//        dialogStage.act(delay);
//        dialogStage.draw();
//        Color color = dialogStage.getBatch().getColor();
//        color.a = 1;
//        dialogStage.getBatch().setColor(color);
//    }

//    @Override
//    public void dispose() {
//        dialogStage.dispose();
//    }

//    public void _addDialog(Dialog dialog) {
//        dialogStage.addActor(dialog);
//    }

//    public void _removeDialog(Dialog dialog) {
//        dialogStage.getRoot().removeActor(dialog);
//    }
}
