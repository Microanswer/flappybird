package cn.microanswer.flappybird.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.NumberDrawHelper;
import cn.microanswer.flappybird.screens.GameScreen;

/**
 * 游戏成绩面板
 * Created by Micro on 2018-2-17.
 */

public class ScorePanelActor extends Actor {
    private GameScreen flappyBirdLibGDX;
    private Action showAction;
    private NumberDrawHelper numberDrawHelper;
    private boolean isnew;
    private boolean startNumberUp;

    private int score, bestScore;

    private TextureAtlas.AtlasRegion medal;

    public ScorePanelActor init(GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;

        setSize(238f / 288f * FlappyBirdGame.WIDTH, 126f / 512f * FlappyBirdGame.HEIGHT);
        setPosition((FlappyBirdGame.WIDTH - getWidth()) / 2f, -getHeight());
        isnew = false;
        time = 0.0f;
        startNumberUp = false;
        // Color color = getColor();
        // color.a = 0;
        // setColor(color);

        RunnableAction action = new RunnableAction();
        action.setRunnable(new Runnable() {
            @Override
            public void run() {
                MAssetsManager.instance().funSound.play();
                startNumberUp = true;
            }
        });
        ParallelAction s = Actions.parallel(action, Actions.moveTo(getX(), (FlappyBirdGame.HEIGHT - getHeight()) / 2f, .3f));

        DelayAction delay = Actions.delay(1.2f, s);
        // AlphaAction alpha1 = Actions.alpha(1, 0f);
        // MoveToAction moveToAction = Actions.moveTo(getX(), -getHeight(), 0);

        showAction = Actions.sequence(delay);

        if (numberDrawHelper == null) {
            numberDrawHelper = new NumberDrawHelper(MAssetsManager.instance().numberScoreFont);
            numberDrawHelper.setAlign(NumberDrawHelper.ALIGN_RIGHT);
            numberDrawHelper.setNumberHeight((20f / 512f) * FlappyBirdGame.HEIGHT);
        }
        return this;
    }

    private float time = 0.0f;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (flappyBirdLibGDX.getGameStatus() == GameScreen.STAT_OVER) {
            Color color = batch.getColor();
            Color color1 = getColor();
            batch.setColor(color1);

            // 绘制成绩面板
            batch.draw(MAssetsManager.instance().score_panel, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            float x = getX() + getWidth() - (28f / 288f * FlappyBirdGame.WIDTH);
            float y = getY() + (28f / 512f * FlappyBirdGame.HEIGHT);
            // 绘制当前成绩
            numberDrawHelper.setPosition(x, y + numberDrawHelper.getNumberHeight() + (23f / 512f * FlappyBirdGame.HEIGHT));
            if (time < 0.6f && startNumberUp && score > 10) {
                // 在0.6秒以内进行数字递增
                int num = Math.round(score * Interpolation.pow2In.apply(time/0.6f));
                numberDrawHelper.draw(batch, num);
                time += Gdx.graphics.getDeltaTime();
            } else {
                numberDrawHelper.draw(batch, score);
            }

            // 绘制最高成绩
            numberDrawHelper.setPosition(x, y);
            float totleNumberWidth = numberDrawHelper.draw(batch, bestScore);
            // 如果是新成绩， 绘制新成绩提示
            if (isnew) {
                float w = (32f / 288f * FlappyBirdGame.WIDTH);
                float h = (14f / 512f * FlappyBirdGame.HEIGHT);
                batch.draw(MAssetsManager.instance().new_, numberDrawHelper.getPosition().x - totleNumberWidth - w, numberDrawHelper.getPosition().y, w, h);
            }

            // 绘制奖牌
            if (medal != null) {
                float w = (44f / 288f * FlappyBirdGame.WIDTH);
                batch.draw(medal, getX() + (32f / 288f * FlappyBirdGame.WIDTH), getY() + (37f / 512f * FlappyBirdGame.HEIGHT), w, w);
            }

            batch.setColor(color);
        }
    }

    // 进行展示动画
    public void show(int score, int bestScore, boolean isnew) {
        this.score = score;
        this.bestScore = bestScore;
        this.isnew = isnew;
        if (score > 0) {
            if (score >= bestScore) {
                medal = MAssetsManager.instance().medals[3];
            } else if (score >= bestScore - 20) {
                medal = MAssetsManager.instance().medals[2];
            } else if (score >= bestScore - 40) {
                medal = MAssetsManager.instance().medals[1];
            } else if (score >= bestScore - 60) {
                medal = MAssetsManager.instance().medals[0];
            } else {
                medal = null;
            }
        }

        // removeAction(showAction);
        // showAction.reset();
        addAction(showAction);
    }
}
