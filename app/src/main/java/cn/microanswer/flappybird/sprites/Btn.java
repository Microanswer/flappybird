package cn.microanswer.flappybird.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.BaseScreen;
import cn.microanswer.flappybird.screens.GameScreen;

/**
 * Created by Micro on 2018-2-18.
 */

public class Btn extends Actor implements InputProcessor {
    private BaseScreen flappyBirdLibGDX;

    private boolean isTouchDown;
    private TextureRegion region;

    // 初始化一个大按钮
    public Btn init(BaseScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;
        isTouchDown = false;
        setSize(116f / 288f * FlappyBirdGame.WIDTH, 70f / 512f * FlappyBirdGame.HEIGHT);
        return this;
    }

    // 初始化一个小按钮
    public Btn initSmall (BaseScreen screen) {
        this.flappyBirdLibGDX = screen;
        isTouchDown = false;
        setSize(74f/288f * FlappyBirdGame.WIDTH, 48f / 512f * FlappyBirdGame.HEIGHT);
        return this;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float x = getX();
        float y = getY();
        if (isTouchDown) {
            y -= (2f / 512f) * FlappyBirdGame.HEIGHT;
        }
        batch.draw(region, x, y, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
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
        Vector3 unproject = flappyBirdLibGDX.getCamera().unproject(new Vector3(screenX, screenY, 0));
        if (pointer == 0) {
            float y = unproject.y;
            float x = unproject.x;
            if (getX() < x && x < getX() + getWidth()) {
                if (getY() < y && y < getY() + getHeight()) {
                    isTouchDown = true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isTouchDown) {
            if (onClickListener != null) {
                                MAssetsManager.instance().playSound(
                        MAssetsManager.instance().funSound
                );
                onClickListener.onClick(this);
            }
        }
        isTouchDown = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 unproject = flappyBirdLibGDX.getCamera().unproject(new Vector3(screenX, screenY, 0));
        if (pointer == 0) {
            float y = unproject.y;
            float x = unproject.x;
            isTouchDown = getX() < x && x < getX() + getWidth() && getY() < y && y < getY() + getHeight();
        }
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

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public static interface OnClickListener {
        void onClick(Btn btn);
    }

}
