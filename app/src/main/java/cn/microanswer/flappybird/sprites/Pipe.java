package cn.microanswer.flappybird.sprites;

import android.util.Log;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.screens.GameScreen;

/**
 * Created by Micro on 2018-2-16.
 */

public class Pipe extends Actor {
    private GameScreen gameScreen;

    public Body pipe_up_body, pipe_down_body;
    public Vector2 pipeDownPosition, pipeUpPosition;
    public Vector2 pipeSize;
    private float space; // 上下间隔
    private int index = -1;
    private boolean passed; // 标记是否已经飞过
    // private NumberDrawHelper numberDrawHelper;

    public Pipe(int index) {
        super();
        this.index = index;
        // numberDrawHelper = new NumberDrawHelper(MAssetsManager.instance().numberScoreFont);
    }

    public Pipe init(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        float width = FlappyBirdGame.WIDTH * (52 / 288f);
        float height = FlappyBirdGame.HEIGHT * (320f / 512f);
        pipeSize = new Vector2(width, height);
        space = 0.16666667f * 2f;
        passed=false;

        setWidth(width);
        setHeight(FlappyBirdGame.HEIGHT);

        pipeDownPosition = getDownPipePosition();
        pipeUpPosition = new Vector2(pipeDownPosition.x, pipeDownPosition.y - (space) - height);

        if (pipe_down_body == null && pipe_up_body == null) {
            // 建立上方的管道物理body
            BodyDef bodyDef = new BodyDef();
            // bodyDef.linearVelocity.x = FlappyBirdLibGDX.RUNSPEED;
            bodyDef.position.x = pipeDownPosition.x;
            bodyDef.position.y = pipeDownPosition.y;
            bodyDef.type = BodyDef.BodyType.KinematicBody;

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / 2f, height / 2f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 0f;
            fixtureDef.shape = shape;
            fixtureDef.restitution = 0;
            fixtureDef.friction = 0f;
            pipe_down_body = gameScreen.getWorld().createBody(bodyDef);
            pipe_down_body.createFixture(fixtureDef);
            pipe_down_body.setUserData(this);

            // 下方的body
            bodyDef.position.y = pipeUpPosition.y;
            bodyDef.position.x = pipeUpPosition.x;
            pipe_up_body = gameScreen.getWorld().createBody(bodyDef);
            pipe_up_body.createFixture(fixtureDef);
            pipe_up_body.setUserData(this);
            shape.dispose();
        } else {
            pipe_down_body.setTransform(pipeDownPosition, 0);
            pipe_down_body.setActive(false);
            pipe_up_body.setTransform(pipeUpPosition, 0f);
            pipe_up_body.setActive(false);
        }

        // numberDrawHelper.setNumberHeight(15 / 512f * FlappyBirdLibGDX.HEIGHT);
        setPosition(pipeUpPosition.x - getWidth() / 2f, 0);
        return this;
    }

    // 获取管口朝下的管道的位置
    private Vector2 getDownPipePosition() {
//        Stage pipeStage = flappyBirdGame.getPipeStage();
//        Array<Actor> actors = pipeStage.getActors();

        // Math.random() ==>  [0,1)
        // Math.random() + 2 ==>  [2, 3)
        // Math.random() * 2 ==>  [0, 2)
        // (Math.random() + 2) * 2 ==> [2, 3) * 2 ==> [4, 6)
        // (Math.random() + 3) * 2 ==> [3, 4) * 2 ==> [6, 8)
        // (Math.random() + 4) * 6 ==> [4, 5) * 6 ==> [24, 30)
        // (Math.random() * 2) + 2 ==> [0, 2) + 2 ==> [2, 4)
        // (Math.random() * 3) + 6 ==> [0, 3) + 6 ==> [6, 9)
        // (Math.random() * 50) + 150 ==> [0, 50) ==> [150, 200)
        // (Math.random() * (y - x)) + x ==> [0, y-x) ==> [x, y)

        float maxY = (FlappyBirdGame.HEIGHT - space) * 0.95f;
        float minY = ((112f / 512f) * FlappyBirdGame.HEIGHT + space);
        float duY = maxY - minY;
        // float y = FlappyBirdLibGDX.HEIGHT/1.3f +  (float) (Math.random() * duY);
        float y = (float) (minY + (Math.random() * duY + minY));
        if (this.gameScreen.getGameStatus() == GameScreen.STAT_PLAYING) {
            int i;
            if (index == 0) {
                i = 2;
            } else if (index == 1) {
                i = 0;
            } else {
                i = 1;
            }

            Pipe actor = (Pipe) getStage().getActors().get(i);
            return new Vector2(actor.pipeDownPosition.x + pipeSize.x + ((((288f - 95f) / 2f) / 288f) * FlappyBirdGame.WIDTH), y);
        } else/* (this.flappyBirdGame.getGameStatus() == FlappyBirdLibGDX.STAT_NONE)*/ {
            return new Vector2(1.6f + (index * (pipeSize.x + ((((288f - 95) / 2f) / 288f) * FlappyBirdGame.WIDTH))), y);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (pipe_down_body != null && pipe_up_body != null) {
            if (gameScreen.getGameStatus() == GameScreen.STAT_PLAYING) {
                pipe_down_body.setActive(true);
                pipe_down_body.setLinearVelocity(GameScreen.RUNSPEED, 0f);
                pipe_up_body.setActive(true);
                pipe_up_body.setLinearVelocity(GameScreen.RUNSPEED, 0f);

                if (getX() < -pipeSize.x) {
                    // 已经移动出了屏幕了，将这个管道移动到最右边进行复用h
                    Vector2 downPipePosition = getDownPipePosition();
                    pipeUpPosition.x = downPipePosition.x;
                    pipeDownPosition.x = downPipePosition.x;
                    pipeDownPosition.y = downPipePosition.y;
                    pipeUpPosition.y = downPipePosition.y - (space) - pipeSize.y;
                    setX(downPipePosition.x - getWidth() / 2);
                    pipe_up_body.setTransform(pipeUpPosition, 0f);
                    pipe_down_body.setTransform(pipeDownPosition, 0f);
                    passed = false; // 重置为没有飞过的
                } else {
                    pipeUpPosition.x = pipe_up_body.getTransform().getPosition().x;
                    pipeDownPosition.x = pipe_down_body.getTransform().getPosition().x;
                    setX(pipeUpPosition.x - getWidth() / 2);
                }

                // 验证是否飞过
                Bird bird = gameScreen.getBird();
                if (getX() - (pipeSize.x / 2f) <= bird.getX() - (bird.getWidth()/2f) && !passed) {
                    gameScreen.getScoreActor().up();
                    passed = true;
                }

            } else if (gameScreen.getGameStatus() == GameScreen.STAT_OVER) {
                pipe_up_body.setActive(false);
                pipe_down_body.setActive(false);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (gameScreen.getGameStatus() != GameScreen.STAT_NONE) {
            // 上方的管道
            batch.draw(MAssetsManager.instance().pipe_down, pipeDownPosition.x - pipeSize.x / 2f, pipeDownPosition.y - pipeSize.y / 2f, pipeSize.x, pipeSize.y);

            // 下方的管道
            batch.draw(MAssetsManager.instance().pipe_up, pipeUpPosition.x - pipeSize.x / 2f, pipeUpPosition.y - pipeSize.y / 2f, pipeSize.x, pipeSize.y);

            // numberDrawHelper.setPosition(pipeDownPosition.x - pipeSize.x / 2f, pipeDownPosition.y- pipeSize.y / 2f);
            // numberDrawHelper.draw(batch, index);
        }
        super.draw(batch, parentAlpha);
    }
}
