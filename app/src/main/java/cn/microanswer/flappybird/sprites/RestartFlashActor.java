package cn.microanswer.flappybird.sprites;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * Created by Micro on 2018-2-18.
 */

public class RestartFlashActor extends Actor {
    private Screen flappyBirdLibGDX;

    public RestartFlashActor init(Screen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;

        setWidth(FlappyBirdGame.WIDTH);
        setHeight(FlappyBirdGame.HEIGHT);

        Color color = getColor();
        color.a = 0f;
        setColor(color);

        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color color = batch.getColor();
        Color mColor = getColor();
        batch.setColor(mColor);
        batch.draw(MAssetsManager.instance().black, getX(), getY(), getWidth(), getHeight());
        batch.setColor(color);
    }

    public void d0Restart (Runnable runnable) {

        AlphaAction alpha = Actions.alpha(1, 0.3f);
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(runnable);
        AlphaAction alpha1 = Actions.alpha(0, 0.3f);
        SequenceAction sequence = Actions.sequence(alpha, runnableAction, alpha1);
        addAction(sequence);

    }
}
