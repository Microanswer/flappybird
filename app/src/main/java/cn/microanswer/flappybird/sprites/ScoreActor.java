package cn.microanswer.flappybird.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.NumberDrawHelper;
import cn.microanswer.flappybird.screens.GameScreen;

/**
 * Created by Micro on 2018-2-15.
 */

public class ScoreActor extends Actor implements Disposable {
    private GameScreen flappyBirdLibGDX;

    private int score;

    private NumberDrawHelper numberDrawHelper;


    public ScoreActor init(GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;
        if (numberDrawHelper == null) {
            numberDrawHelper = new NumberDrawHelper(MAssetsManager.instance().scoreFont);
            numberDrawHelper.setAlign(NumberDrawHelper.ALIGN_CENTER);
            numberDrawHelper.setNumberHeight(FlappyBirdGame.HEIGHT * (44f / 512f));
        }
        setWidth(FlappyBirdGame.WIDTH); // 绘制成绩区域的宽度我定为全屏宽度
        setHeight(numberDrawHelper.getNumberHeight()); // 绘制成绩的高度就使用单个数字的高度

        setX(FlappyBirdGame.WIDTH / 2f);
        setY(FlappyBirdGame.HEIGHT - (getHeight() * 2.65f));
        numberDrawHelper.setPosition(getX(), getY());
        numberDrawHelper.setLetterSpace(1.8f / 144f);

        this.score = 0;
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (flappyBirdLibGDX.getGameStatus() != GameScreen.STAT_OVER) {
            numberDrawHelper.draw(batch, score);
        }
    }

    // 分数+1
    public void up() {
        this.score++;
        MAssetsManager.instance().playSound(MAssetsManager.instance().scoreupSound);
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public void dispose() {
    }

    public void reset() {
        this.score = 0;
    }
}
