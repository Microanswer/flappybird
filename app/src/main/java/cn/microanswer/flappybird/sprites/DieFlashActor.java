package cn.microanswer.flappybird.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.GameScreen;

/**
 * 小鸟死亡瞬间的闪烁屏幕
 * Created by Micro on 2018-2-17.
 */

public class DieFlashActor extends Actor {
    private GameScreen flappyBirdLibGDX;

    private Action flashAction;

    public DieFlashActor init (GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;

        setSize(FlappyBirdGame.WIDTH, FlappyBirdGame.HEIGHT);
        setPosition(0, 0);

        flashAction = Actions.alpha(0, .25f);
        Color color = getColor();
        color.a = 0f;
        setColor(color);

        return this;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color batchColor = batch.getColor();
        Color color = getColor();
        batch.setColor(color);
        batch.draw(MAssetsManager.instance().whilte,getX(),getY(),getWidth(),getHeight());
        batch.setColor(batchColor);
    }

    public void flash () {

        removeAction(flashAction);
        Color color = getColor();
        color.a = 1f;
        setColor(color);
        flashAction.reset();
        addAction(flashAction);

    }


}
