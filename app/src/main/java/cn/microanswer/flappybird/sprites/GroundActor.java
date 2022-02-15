package cn.microanswer.flappybird.sprites;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Micro on 2018-2-18.
 */

public class GroundActor extends Actor {
    private GameScreen flappyBirdLibGDX;
    private Body groundBody;
    private float groundWIDTH, groundHEIGHT;

    public GroundActor init(GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;

        groundWIDTH = FlappyBirdGame.WIDTH;
        groundHEIGHT = FlappyBirdGame.HEIGHT * .21875f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.linearVelocity.x = GameScreen.RUNSPEED;
        bodyDef.position.x = groundWIDTH;
        bodyDef.position.y = groundHEIGHT / 2f;
        groundBody = flappyBirdLibGDX.getWorld().createBody(bodyDef);
        groundBody.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(groundWIDTH, groundHEIGHT / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 99999f;
        fixtureDef.friction = 999999f;
        groundBody.createFixture(fixtureDef);
        return this;
    }

    @Override
    public void draw(Batch bh, float parentAlpha) {
        super.draw(bh, parentAlpha);
        // 绘制地板
        Transform transform = groundBody.getTransform();
        Vector2 p = transform.getPosition();
        bh.draw(MAssetsManager.instance().ground, p.x - groundWIDTH, p.y - (groundHEIGHT / 2f),groundWIDTH, groundHEIGHT);
        bh.draw(MAssetsManager.instance().ground, p.x, p.y - (groundHEIGHT / 2f), groundWIDTH, groundHEIGHT);
        if (p.x <= 0f) {
            p.x = groundWIDTH;
            groundBody.setTransform(p, 0f);
        }
    }

    public void setLinearVelocity(float x, float y) {
        groundBody.setLinearVelocity(x, y);
    }
}
