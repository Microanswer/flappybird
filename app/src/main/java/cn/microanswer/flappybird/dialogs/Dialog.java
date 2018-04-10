package cn.microanswer.flappybird.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.BaseScreen;

/**
 * 游戏内弹出框基类
 */
public abstract class Dialog extends Group implements InputProcessor {
    private final int BG_LEFT = 0;
    private final int BG_CENTER = 1;
    private final int BG_RIGHT = 2;
    private final int BG_TOP = BG_LEFT;
    private final int BG_BOTTOM = BG_RIGHT;


    private Texture black_03; // 透明度为0.3的黑色1像素点

    private BaseScreen baseScreen;
    private boolean cancelAble;
    private float x, y, width, height;

    // 为了让背景不被拉伸，实现向android中的9patch效果，定义这个二维数组来实现
    private TextureRegion[][] backgrounds;

    public Dialog(BaseScreen baseScreen) {
        super();
        this.baseScreen = baseScreen;

        setX(0);
        setY(0);
        setWidth(FlappyBirdGame.WIDTH);
        setHeight(FlappyBirdGame.HEIGHT);
        Color color = getColor();
        color.a = 0;
        setColor(color);
        setScale(0.5f);

        this.cancelAble = true;
        this.width = FlappyBirdGame.WIDTH * .7f;
        this.height = FlappyBirdGame.HEIGHT * .4f;
        this.x = (FlappyBirdGame.WIDTH - this.width) / 2f;
        this.y = (FlappyBirdGame.HEIGHT - this.height) / 2f;
        setOrigin(getWidth() / 2f, getHeight() / 2f);

        // 绘制一个透明度为0.4的黑色像素点，作为弹出框遮罩颜色
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.drawPixel(0, 0, Color.rgba8888(0, 0, 0, .4f));
        black_03 = new Texture(pixmap);

        this.backgrounds = new TextureRegion[3][3];
        this.backgrounds[BG_LEFT][BG_TOP] = new TextureRegion(MAssetsManager.instance().dialogBg, 0, 0, 8, 10);
        this.backgrounds[BG_CENTER][BG_TOP] = new TextureRegion(MAssetsManager.instance().dialogBg, 8, 0, 20, 10);
        this.backgrounds[BG_RIGHT][BG_TOP] = new TextureRegion(MAssetsManager.instance().dialogBg, 28, 0, 8, 10);
        this.backgrounds[BG_LEFT][BG_CENTER] = new TextureRegion(MAssetsManager.instance().dialogBg, 0, 10, 8, 16);
        this.backgrounds[BG_CENTER][BG_CENTER] = new TextureRegion(MAssetsManager.instance().dialogBg, 8, 10, 20, 16);
        this.backgrounds[BG_RIGHT][BG_CENTER] = new TextureRegion(MAssetsManager.instance().dialogBg, 28, 10, 8, 16);
        this.backgrounds[BG_LEFT][BG_BOTTOM] = new TextureRegion(MAssetsManager.instance().dialogBg, 0, 26, 8, 10);
        this.backgrounds[BG_CENTER][BG_BOTTOM] = new TextureRegion(MAssetsManager.instance().dialogBg, 8, 26, 20, 10);
        this.backgrounds[BG_RIGHT][BG_BOTTOM] = new TextureRegion(MAssetsManager.instance().dialogBg, 28, 26, 8, 10);


        initBaseActor();
    }


    private void initBaseActor() {

        onCreate();
    }

    public float getDialogX() {
        return x;
    }

    public float getDialogY() {
        return y;
    }

    public float getDialogWidth() {
        return width;
    }

    public float getDialogHeight() {
        return height;
    }

    public void setDialogWidth(float width) {
        this.width = width;
        this.x = (FlappyBirdGame.WIDTH - this.width) / 2f;
    }

    public void setDialogHeight(float height) {
        this.height = height;
        this.y = (FlappyBirdGame.HEIGHT - this.height) / 2f;
    }

    public void setCancelAble(boolean cancelAble) {
        this.cancelAble = cancelAble;
    }

    public boolean isCancelAble() {
        return cancelAble;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // 绘制弹出框背景

        Color color = getColor();
        batch.setColor(color);
        // 应用透明度
        batch.draw(black_03, getX(), getY(), getWidth(), getHeight());

        super.draw(batch, parentAlpha);

        float ww1 = 8f / 288f;
        float ww3 = ww1;
        float ww2 = this.width - ww1 - ww3;
        float hh1 = 10f / 288f;
        float hh3 = hh1;
        float hh2 = this.height - hh1 - hh3;
        // 绘制弹出框内容背景
        batch.draw(this.backgrounds[BG_LEFT][BG_BOTTOM], x, y, getOriginX() - x, getOriginY() - y, ww1, hh3, getScaleX(), getScaleY(), 0);
        float x2 = x + ww1;
        batch.draw(this.backgrounds[BG_CENTER][BG_BOTTOM], x2, y, getOriginX() - x2, getOriginY() - y, ww2, hh3, getScaleX(), getScaleY(), 0);
        float x3 = x2 + ww2;
        batch.draw(this.backgrounds[BG_RIGHT][BG_BOTTOM], x3, y, getOriginX() - x3, getOriginY() - y, ww3, hh3, getScaleX(), getScaleY(), 0);
        float y2 = y + hh3;
        batch.draw(this.backgrounds[BG_LEFT][BG_CENTER], x, y2, getOriginX() - x, getOriginY() - y2, ww1, hh2, getScaleX(), getScaleY(), 0);
        batch.draw(this.backgrounds[BG_CENTER][BG_CENTER], x2, y2, getOriginX() - x2, getOriginY() - y2, ww2, hh2, getScaleX(), getScaleY(), 0);
        batch.draw(this.backgrounds[BG_RIGHT][BG_CENTER], x3, y2, getOriginX() - x3, getOriginY() - y2, ww3, hh2, getScaleX(), getScaleY(),0);
        float y3 = y2 + hh2;
        batch.draw(this.backgrounds[BG_LEFT][BG_TOP], x, y3, getOriginX() - x, getOriginY() - y3, ww1, hh1, getScaleX(), getScaleY(), 0);
        batch.draw(this.backgrounds[BG_CENTER][BG_TOP], x2, y3, getOriginX() - x2, getOriginY() - y3, ww2, hh1, getScaleX(), getScaleY(), 0);
        batch.draw(this.backgrounds[BG_RIGHT][BG_TOP], x3, y3, getOriginX() - x3, getOriginY() - y3, ww3, hh1, getScaleX(), getScaleY(), 0);

    }

    public abstract void onCreate();

    /**
     * 隐藏并消耗
     */
    public void dismiss() {

        ScaleToAction scaleToAction = Actions.scaleTo(0.7f, 0.7f, 0.15f);
        AlphaAction alpha = Actions.alpha(0f, 0.15f);
        ParallelAction parallel = Actions.parallel(scaleToAction, alpha);
        RunnableAction runnableAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                baseScreen._removeDialog(Dialog.this);
                Gdx.input.setInputProcessor(baseScreen);
                black_03.dispose();
            }
        });
        addAction(Actions.sequence(parallel, runnableAction));
    }

    /**
     * 显示弹出框
     */
    public void show() {
        Gdx.input.setInputProcessor(this);

        AlphaAction alpha = Actions.alpha(1, 0.23f);
        ScaleToAction scaleToAction = Actions.scaleTo(1.1f, 1.1f, .15f);
        ScaleToAction scaleToAction1 = Actions.scaleTo(1f, 1f, .08f);
        SequenceAction sequence = Actions.sequence(scaleToAction, scaleToAction1);
        addAction(Actions.parallel(alpha, sequence));

        baseScreen._addDialog(this);
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 unproject = baseScreen.getCamera().unproject(new Vector3(screenX, screenY, 0));
        if (pointer == 0) {
            float yy = unproject.y;
            float xx = unproject.x;
            if (x < xx && xx < x + this.width) {
                if (y < yy && yy < y + this.height) {
                    // 点击弹出框内部
                    onDialogConentClick(xx, yy);
                    return true;
                }
            }

        }

        if (isCancelAble()) {
            dismiss();
        }

        return false;
    }

    protected void onDialogConentClick(float x, float y) {}

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
}
