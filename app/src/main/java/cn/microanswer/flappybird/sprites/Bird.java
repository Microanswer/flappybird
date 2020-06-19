package cn.microanswer.flappybird.sprites;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

/**
 * Created by Micro on 2018-2-10.
 */

public class Bird extends Actor implements Disposable {

    private SequenceAction sequence1;
    private RepeatAction forever;
    private Animation animation;
    private GameScreen flappyBirdLibGDX;

    private Body body;

    public Bird init(GameScreen flappyBirdLibGDX) {
        this.flappyBirdLibGDX = flappyBirdLibGDX;
        setSize(0.16666667f, 0.17f);
        setPosition(0.2333f, (FlappyBirdGame.HEIGHT - getHeight()) / 2f);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setRotation(0f);

        int index = new Random().nextInt(3);
        animation = new Animation(0.13f,
                MAssetsManager.instance().bird[index][0],
                MAssetsManager.instance().bird[index][1],
                MAssetsManager.instance().bird[index][2],
                MAssetsManager.instance().bird[index][1]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        animation.setFrameDuration(.13f);
        if (body == null) {
            // 设定重力作用
            BodyDef bodyDef = new BodyDef();
            bodyDef.fixedRotation = true;
            // bodyDef.linearDamping = .1f;
            bodyDef.position.x = getX() + (getWidth() / 2f);
            bodyDef.position.y = getY() + (getHeight() / 2f);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            //  bodyDef.linearVelocity.x = 0;
            //  bodyDef.linearVelocity.y = -9.8f;
            body = flappyBirdLibGDX.getWorld().createBody(bodyDef);
            body.setUserData(this);

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(getWidth() / 2f, getHeight() / 2f);
            polygonShape.setRadius(-0.053f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = .7f;
            fixtureDef.friction = 0f;
            fixtureDef.restitution = 0f;
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }

        body.setActive(false);

        // 未开始游戏的时候，上下跳动的动画
        MoveByAction moveByUp = Actions.moveBy(0, getHeight() / 4f);
        moveByUp.setDuration(.38f);
        MoveByAction moveByDown = Actions.moveBy(0, -getHeight() / 4f);
        moveByDown.setDuration(.38f);
        SequenceAction sequence = Actions.sequence(moveByUp, moveByDown, moveByDown, moveByUp);
        forever = Actions.forever(Actions.forever(sequence));
        addAction(forever);
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        TextureRegion region = null;
        if (body.isActive()) {
            Transform transform = body.getTransform();
            Vector2 position = transform.getPosition();
            // setPosition(position.x - (getWidth() / 2f), position.y - (getHeight() / 2f));
            setPosition(getX(), position.y - (getHeight() / 2f));
            // setRotation(180 * transform.getRotation() / 3.141576523f);
            // System.out.println(body.getLinearVelocity());
            if (body.getLinearVelocity().y <= -1.2f) {
                // 小鸟下落，让翅膀不动，保持滑翔
                region = (TextureRegion) animation.getKeyFrames()[1];
            }
        } else {
            float x = getX() + (getWidth() / 2f);
            float y = getY() + (getHeight() / 2f);
            body.setTransform(x, y, 0f);
        }
        if (region == null) {
            region = (TextureRegion) animation.getKeyFrame(flappyBirdLibGDX.runTime);
        }
        if (flappyBirdLibGDX.getGameStatus() == GameScreen.STAT_OVER) {
            // 死了，翅膀就不要动了
            region = (TextureRegion) animation.getKeyFrames()[1];
        }
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

    }

    // 应用重力
    public void applyWeight() {
        body.setActive(true);
        // 移除action动画
        forever.finish();
        removeAction(forever);
    }

    // 向上跳跃。
    public void up() {
        // 给予小鸟向上的重力，使其达到向上跳跃一下
        body.setLinearVelocity(0, 0);
        body.applyForceToCenter(0, 1.29f, true);

        // 设置帧动画频率更快，让翅膀动得更加急促，显得很用力的在飞
        animation.setFrameDuration(0.055f);

        // 进行头部转向动画
        if (sequence1 != null) {
            removeAction(sequence1);
        }
        // 点一下就将头向上边仰一下, 然后头向下旋转
        RotateToAction rotateByAction = Actions.rotateTo(20f, 0.2f);
        DelayAction d = Actions.delay(0.23f);
        RotateToAction rotateByAction4 = Actions.rotateTo(-90f, 0.4f);
        rotateByAction4.setInterpolation(Interpolation.pow2In);
        sequence1 = Actions.sequence(rotateByAction, d, rotateByAction4);
        addAction(sequence1);

        MAssetsManager.instance().playSound(MAssetsManager.instance().birdSound);
    }

    @Override
    public void dispose() {
    }
}
