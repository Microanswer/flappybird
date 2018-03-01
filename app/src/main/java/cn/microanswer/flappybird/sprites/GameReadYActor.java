package cn.microanswer.flappybird.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.GameScreen;

/**
 * 游戏准备和玩法提示
 * Created by Micro on 2018-2-14.
 */

public class GameReadYActor extends Actor implements Disposable {

    private GameScreen flappyBirdLibGDX;

    private float howPlayWidth, howPlayHeight, readyTxtHeight;

    public GameReadYActor init(GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;
        setWidth((196 * (1 - 0.19140625f) / 228f) * FlappyBirdGame.WIDTH);
        setHeight((160 / 512f) * FlappyBirdGame.HEIGHT);

        howPlayWidth = FlappyBirdGame.WIDTH * (114 / 288f);
        howPlayHeight = FlappyBirdGame.HEIGHT * (98 / 512f);
        readyTxtHeight = FlappyBirdGame.HEIGHT * (62 / 512f);
        setPosition((FlappyBirdGame.WIDTH - getWidth()) / 2f, (FlappyBirdGame.HEIGHT - howPlayHeight) / 2f);

        return this;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = batch.getColor();
        Color color1 = getColor();
        color1.a *= parentAlpha;
        batch.setColor(color1);
        // 绘制getready
        batch.draw(MAssetsManager.instance().text_ready, getX(), getY() + howPlayHeight, getOriginX(), getOriginY(), getWidth(), readyTxtHeight, getScaleX(), getScaleY(), getRotation());

        // 绘制 玩法提示
        batch.draw(MAssetsManager.instance().howPlay, getX() + (getWidth() - howPlayWidth) / 2f, getY() - (0.025f * FlappyBirdGame.HEIGHT), getOriginX(), getOriginY(), howPlayWidth, howPlayHeight, getScaleX(), getScaleY(), getRotation());

        batch.setColor(color);
    }

    @Override
    public void dispose() {

    }
}
