package cn.microanswer.flappybird.sprites;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.NumberDrawHelper;
import cn.microanswer.flappybird.screens.GameScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Micro on 2018-2-15.
 */

public class ScoreActor extends Label implements Disposable {
    private GameScreen flappyBirdLibGDX;

    private int score = 0;

//    private NumberDrawHelper numberDrawHelper;

    public ScoreActor() {
//        super("0", new LabelStyle(new BitmapFont(), Color.WHITE));
        super("0", new LabelStyle(MAssetsManager.instance().font1, Color.WHITE));
    }


    public ScoreActor init(GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;
//        if (numberDrawHelper == null) {
//            numberDrawHelper = new NumberDrawHelper(MAssetsManager.instance().scoreFont);
//            numberDrawHelper.setAlign(NumberDrawHelper.ALIGN_CENTER);
//            numberDrawHelper.setNumberHeight(FlappyBirdGame.HEIGHT * (44f / 512f));
//        }

//        setPosition(0f, 0);
        setWidth(FlappyBirdGame.WIDTH); // 绘制成绩区域的宽度我定为全屏宽度
        setAlignment(Align.center);
//        setHeight(numberDrawHelper.getNumberHeight()); // 绘制成绩的高度就使用单个数字的高度

//        setX(FlappyBirdGame.WIDTH / 2f);
        setY(FlappyBirdGame.HEIGHT/1.7f);
//        numberDrawHelper.setPosition(getX(), getY());
//        numberDrawHelper.setLetterSpace(1.8f / 144f);

        setFontScale(0.2f);
        this.score = 0;
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (flappyBirdLibGDX.getGameStatus() != GameScreen.STAT_OVER) {
        super.draw(batch, parentAlpha);
        }
    }

    // 分数+1
    public void up() {
        this.score++;
        setText(String.valueOf(this.score));
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
        setText(String.valueOf(this.score));
    }
}
