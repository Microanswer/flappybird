package cn.microanswer.flappybird.dialogs;

import com.badlogic.gdx.graphics.g2d.Batch;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.BaseScreen;

/**
 * 游戏内弹出框
 */
public class LoadingDialog extends Dialog {


    private float deg; // 转动的角度
    private float loadingSize, lx, ly;

    public LoadingDialog(BaseScreen baseScreen) {
        super(baseScreen);
    }

    @Override
    public void onCreate() {
        setDialogWidth(FlappyBirdGame.WIDTH * .25f);
        setDialogHeight(getDialogWidth());
        loadingSize = 36f / 288f;
        lx = (getWidth() - loadingSize) / 2f;
        ly = (getHeight() - loadingSize) / 2f;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        batch.draw(MAssetsManager.instance().loading, lx,ly, loadingSize,loadingSize);
        batch.draw(MAssetsManager.instance().loading, lx, ly, getOriginX() - lx, getOriginY()-ly, loadingSize, loadingSize, getScaleX(), getScaleY(), deg);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

         deg -= delta * 300;

         if (deg <= -3600) {
             deg = 0;
         }
    }
}
