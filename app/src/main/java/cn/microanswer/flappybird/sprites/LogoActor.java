package cn.microanswer.flappybird.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.screens.LogoScreen;

/**
 * Created by Micro on 2018-2-19.
 */

public class LogoActor extends Actor implements Disposable {
    private LogoScreen logoScreen;

    private Texture microanswerTexture;
    private TextureRegion microanswer;

    public LogoActor init(LogoScreen logoScreen) {
        this.logoScreen = logoScreen;

        setWidth(166f / 288f * FlappyBirdGame.WIDTH);
        setHeight(40f / 512f * FlappyBirdGame.HEIGHT);
        setX((FlappyBirdGame.WIDTH - getWidth()) / 2f);
        setY((FlappyBirdGame.HEIGHT - getHeight()) / 2f);


        microanswerTexture = new Texture(Gdx.files.internal("logo.png"));
        microanswer = new TextureRegion(microanswerTexture);

        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(microanswer, getX(), getY(), getWidth(), getHeight());

    }


    @Override
    public void dispose() {
        microanswerTexture.dispose();
    }
}
