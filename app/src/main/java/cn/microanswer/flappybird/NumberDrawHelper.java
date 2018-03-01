package cn.microanswer.flappybird;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * 绘制数字的辅助类
 * Created by Micro on 2018-2-17.
 */

public class NumberDrawHelper {
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;


    private TextureRegion[] textureRegions; // 要绘制的内容
    private float numberHeight; // 文字高度， 宽度将通过高度自动适配
    private int align; // 对齐方式， 默认是左对齐。
    private Vector2 position; // 位置
    private float letterSpace; // 文字间距

    public NumberDrawHelper(TextureRegion[] textureRegions) {
        this(textureRegions, .1f);
    }

    public NumberDrawHelper(TextureRegion[] textureRegions, float numberHeight) {
        this(textureRegions, numberHeight, new Vector2(0, 0));
    }

    public NumberDrawHelper(TextureRegion[] textureRegions, float numberHeight, Vector2 position) {
        this.textureRegions = textureRegions;
        this.numberHeight = numberHeight;
        this.position = position;
        this.letterSpace = 0;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public void setNumberHeight(float numberHeight) {
        this.numberHeight = numberHeight;
    }

    public float getNumberHeight() {
        return numberHeight;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        if (this.position == null) {
            this.position = new Vector2(x, y);
        } else {
            this.position.x = x;
            this.position.y = y;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setTextureRegions(TextureRegion[] textureRegions) {
        this.textureRegions = textureRegions;
    }

    public void setLetterSpace(float letterSpace) {
        this.letterSpace = letterSpace;
    }

    public float draw(Batch batch, int number) {
        String numStr = String.valueOf(number);

        // 先计算出所有数字的总长度
        float totleNumberWidth = 0;
        for (int index = 0; index < numStr.length(); index++) {
            int num = Character.getNumericValue(numStr.charAt(index));
            TextureRegion region = textureRegions[num];
            float regionWidth = region.getRegionWidth();
            float regionHeight = region.getRegionHeight();
            regionWidth = regionWidth * (numberHeight / regionHeight);
            totleNumberWidth += (regionWidth - letterSpace);
        }

        float x = position.x;
        float y = position.y;

        // 根据不同的对齐方式计算不同的绘制开始位置
        if (align == ALIGN_LEFT) {

        } else if (align == ALIGN_CENTER) {
            x -= totleNumberWidth / 2f;
        } else if (align == ALIGN_RIGHT) {
            x -= totleNumberWidth;
        }

        // 开始绘制
        for (int index = 0; index < numStr.length(); index++) {
            int num = Character.getNumericValue(numStr.charAt(index));
            TextureRegion region = textureRegions[num];
            float regionWidth = region.getRegionWidth();
            float regionHeight = region.getRegionHeight();
            regionWidth = regionWidth * (numberHeight / regionHeight);
            regionHeight = numberHeight;

            batch.draw(region, x, y, regionWidth, regionHeight);
            x += (regionWidth - letterSpace);
        }
        return totleNumberWidth;
    }

}
