package cn.microanswer.flappybird.sprites;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.GameScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;

/**
 * Created by Micro on 2018-2-18.
 */

public class GameOverActor extends Actor {
    private GameScreen flappyBirdLibGDX;

    public GameOverActor init(GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;

        setSize((204f / 288f * FlappyBirdGame.WIDTH), 54f / 512f * FlappyBirdGame.HEIGHT);
        setX((FlappyBirdGame.WIDTH - getWidth()) / 2f);

        Color color = getColor();
        color.a = 0;
        setColor(color);

        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color batchColor = batch.getColor();
        Color color = getColor();

        batch.setColor(color);
        batch.draw(MAssetsManager.instance().text_gameover, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(batchColor);
    }

    public void show() {
        MoveToAction moveToAction = Actions.moveTo(getX(), getY() + getHeight() / 4f, 0.1f);
        AlphaAction alpha = Actions.alpha(1, 0.23f);
        MoveToAction moveToAction1 = Actions.moveTo(getX(), getY(), 0.15f);
        SequenceAction sequence = Actions.sequence(moveToAction, moveToAction1);

        RunnableAction action = new RunnableAction();
        action.setRunnable(new Runnable() {
            @Override
            public void run() {
                                MAssetsManager.instance().playSound(
                        MAssetsManager.instance().funSound
                );
            }
        });

        ParallelAction parallel = Actions.parallel(alpha, sequence, action);

        addAction(Actions.delay(0.5f, parallel));
    }
}
