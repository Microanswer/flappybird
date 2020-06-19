package cn.microanswer.flappybird.dialogs;

import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.BaseScreen;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * 警告框
 */
public class AlertDialog extends Dialog {

    float mx,my;

    public AlertDialog(BaseScreen baseScreen, String msg) {
        super(baseScreen);
    }

    @Override
    public void onCreate() {
        setDialogWidth(250f/288f);

        mx = (getWidth() - (206f/288f))/2f;
        my = (getHeight() - (70f/512f))/2f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(MAssetsManager.instance().scoreuptip,mx, my, getOriginX()-mx, getOriginY()-my,206f/288f,70f/512f,getScaleX(),getScaleY(), 0f);
    }
}
