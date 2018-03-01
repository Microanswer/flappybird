package cn.microanswer.flappybird.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Micro on 2018-2-5.
 */

public class TestSprite extends Sprite {

    public static Pixmap getPixMap() {

        Pixmap pixmap = new Pixmap(30, 30, Pixmap.Format.RGB565);
        pixmap.setColor(Color.BLUE);
        pixmap.fillCircle(0, 0, 15);
        return pixmap;
    }

    public TestSprite() {
        super(new Texture(getPixMap()), 100, 100);
        setX(0);
        setY(0);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
