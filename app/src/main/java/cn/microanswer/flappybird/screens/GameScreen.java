package cn.microanswer.flappybird.screens;

import static cn.microanswer.flappybird.FlappyBirdGame.HEIGHT;
import static cn.microanswer.flappybird.FlappyBirdGame.WIDTH;

import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import cn.microanswer.flappybird.MAssetsManager;
import cn.microanswer.flappybird.Util;
import cn.microanswer.flappybird.sprites.Bird;
import cn.microanswer.flappybird.sprites.Btn;
import cn.microanswer.flappybird.sprites.DieFlashActor;
import cn.microanswer.flappybird.sprites.GameOverActor;
import cn.microanswer.flappybird.sprites.GameReadYActor;
import cn.microanswer.flappybird.sprites.GroundActor;
import cn.microanswer.flappybird.sprites.Pipe;
import cn.microanswer.flappybird.sprites.RestartFlashActor;
import cn.microanswer.flappybird.sprites.ScoreActor;
import cn.microanswer.flappybird.sprites.ScorePanelActor;

/**
 * Created by Micro on 2018-2-19.
 */

public class GameScreen extends BaseScreen implements ContactListener, Btn.OnClickListener {
    public static final float RUNSPEED = -.45f;
    public static final int STAT_NONE = 0; // 游戏完全还没有开始
    public static final int STAT_PLAYING = 1; // 游戏进行中
    public static final int STAT_OVER = 2; // 游戏结束。

    private World world;
    private Preferences preferences;

    private Stage stage, pipeStage;
    private Bird bird;
    private GameOverActor gameOverActor;
    private GameReadYActor gameReadYActor;
    private ScoreActor scoreActor;
    private GroundActor groundActor;
    private DieFlashActor dieFlashActor;
    private RestartFlashActor restartFlashActor;
    private ScorePanelActor scorePanelActor;
    private Btn btnPlay, btnScores;
    private TextureAtlas.AtlasRegion bg;
    private String playId; // 每开始一局游戏，生成一个新的playId
    private int SecondWorldCount = 4; // 每秒钟物理世界计算次数

    //    private BitmapFont bitmapFont;

    private int gameStatus = STAT_NONE;

    public float runTime; // 游戏运行时间总和

    public World getWorld() {
        return world;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public Stage getPipeStage() {
        return pipeStage;
    }

    public Bird getBird() {
        return bird;
    }

    public ScoreActor getScoreActor() {
        return scoreActor;
    }

    public GameScreen(OrthographicCamera camera, Batch batch) {
        super(camera, batch);
        Gdx.input.setInputProcessor(this);
        preferences = Gdx.app.getPreferences("preferences");

        world = new World(new Vector2(0f, -5.25f), true);
        world.setContactListener(this);

        pipeStage = new Stage(viewport, batch);
        pipeStage.addActor(new Pipe(0).init(this));
        pipeStage.addActor(new Pipe(1).init(this));
        pipeStage.addActor(new Pipe(2).init(this));
        // pipeStage.setDebugAll(true);
        if (Util.isDay()) {
            bg = (MAssetsManager.instance().bg_day);
        } else {
            bg = (MAssetsManager.instance().bg_night);
        }
        stage = new Stage(viewport, pipeStage.getBatch());
        // stage.setDebugAll(true);
        bird = new Bird().init(this);
        gameReadYActor = new GameReadYActor().init(this);
        gameOverActor = new GameOverActor().init(this);
        scoreActor = new ScoreActor().init(this);
        dieFlashActor = new DieFlashActor().init(this);
        scorePanelActor = new ScorePanelActor().init(this);
        float y = (HEIGHT - scorePanelActor.getHeight()) / 2f;
        gameOverActor.setY(y + scorePanelActor.getHeight() + (25f / 512f * HEIGHT));
        btnPlay = new Btn().init(this);
        btnPlay.setRegion(MAssetsManager.instance().btnPlay);
        btnPlay.setX(scorePanelActor.getX());
        btnPlay.setY(-btnPlay.getHeight());
        btnPlay.setOnClickListener(this);
        btnScores = new Btn().init(this);
        btnScores.setRegion(MAssetsManager.instance().btnScore);
        btnScores.setX(scorePanelActor.getX() + scorePanelActor.getWidth() - btnScores.getWidth());
        btnScores.setY(-btnScores.getHeight());
        btnScores.setOnClickListener(this);
        groundActor = new GroundActor().init(this);
        restartFlashActor = new RestartFlashActor().init();

        stage.addActor(bird);                   // 鸟
        stage.addActor(gameReadYActor);         // Ready 文案
        stage.addActor(gameOverActor);          // 结束游戏文案
        stage.addActor(scoreActor);             // 成绩
        stage.addActor(groundActor);            // 地面
        stage.addActor(scorePanelActor);        // 成绩面板
        stage.addActor(btnPlay);                // 重新开始游戏按钮
        stage.addActor(btnScores);              // 成绩按钮
        stage.addActor(dieFlashActor);          // 死亡闪屏
        stage.addActor(restartFlashActor);      // 重新开始游戏闪屏
        // renderer = new Box2DDebugRenderer();
        // bitmapFont = new BitmapFont(Gdx.files.internal("arial-15.fnt"));
        // bitmapFont.setColor(Color.BLACK);

        buildWorldWall(); // 给游戏四周添加限制
    }



    @Override
    public void show() {

    }

    // 构建屏幕顶部的墙壁，以免小鸟飞出屏幕
    private void buildWorldWall() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        EdgeShape topEdge = new EdgeShape();
        topEdge.set(0, HEIGHT + 0.3f, WIDTH, HEIGHT + 0.3f); // 顶部允许小鸟超出30厘米
        Body topBody = world.createBody(bodyDef);
        topBody.createFixture(topEdge, Float.MAX_VALUE);
        topEdge.dispose();
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        double dt = Math.round(delta/0.0083333333333333);
        for (int i = 0; i < dt; i++) {
            world.step(0.008333333333333f, 6, 2);
        }
        Batch bh = stage.getBatch();
        // 绘制背景
        bh.begin();
        bh.draw(bg, 0, 0, WIDTH, HEIGHT);
        bh.end();
        if (pipeStage != null) {
            pipeStage.act();
            pipeStage.draw();
        }
        stage.act();
        stage.draw();
    }


    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        world.dispose();
        pipeStage.dispose();
        stage.dispose();
        gameReadYActor.dispose();
        scoreActor.dispose();
        // bitmapFont.dispose();
        bird.dispose();
        MAssetsManager.instance().dispose();
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
        if (btnPlay != null) {
            btnPlay.touchDown(screenX, screenY, pointer, button);
        }
        if (btnScores != null) {
            btnScores.touchDown(screenX, screenY, pointer, button);
        }
        if (0 == pointer) {
            // Gdx.app.log("touchDown", pointer + "");
            if (getGameStatus() == STAT_NONE) {
                // 游戏是未开始状态的时候
                bird.applyWeight();
                bird.up();

                // 隐藏玩法提示
                AlphaAction alphaAction = Actions.alpha(0, .5f);
                gameReadYActor.addAction(alphaAction);

                gameStatus = STAT_PLAYING;

                playId = Util.getRandomString(); // 生成一个随机ID
            } else if (getGameStatus() == STAT_PLAYING) {
                // 游戏正在进行
                bird.up();
            }
            // Gdx.app.log(getClass().getSimpleName(), "delta：" + Gdx.graphics.getDeltaTime());
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (btnPlay != null) {
            btnPlay.touchUp(screenX, screenY, pointer, button);
        }

        if (btnScores != null) {
            btnScores.touchUp(screenX, screenY, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (btnPlay != null) {
            btnPlay.touchDragged(screenX, screenY, pointer);
        }

        if (btnScores != null) {
            btnScores.touchDragged(screenX, screenY, pointer);
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

    // 重新开始游戏
    public void restartGame() {
        gameStatus = STAT_NONE;
        bird.init(this);

        // 重置水管
        Array<Actor> actors = pipeStage.getRoot().getChildren();
        for (Actor actor : actors) {
            Pipe pipe = (Pipe) actor;
            pipe.init(this);
        }

        // 分数重置
        scoreActor.reset();
        scorePanelActor.init(this);

        // 隐藏按钮
        btnPlay.setY(-btnPlay.getHeight());
        btnScores.setY(-btnScores.getHeight());
        Color color = gameOverActor.getColor();
        color.a = 0f;
        gameOverActor.setColor(color);

        // 显示提示
        Color c = gameReadYActor.getColor();
        c.a = 1f;
        gameReadYActor.setColor(c);

        // 地板移动
        groundActor.setLinearVelocity(RUNSPEED, 0);
    }

    private boolean btnClick;

    @Override
    public void onClick(Btn btn) {
        if (btn == btnPlay) {
            if (!btnClick) {
                btnClick = true;
                restartFlashActor.d0Restart(new Runnable() {
                    @Override
                    public void run() {
                        restartGame();
                    }
                });
            }
        } else if (btn == btnScores) {
            // 跳转到成绩界面
            // Util.jump2ScoreActivity(game.getMainActivity(), "http://microanswer.cn/flappybird/scorebord.html");
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if (getGameStatus() == STAT_PLAYING) {
            Object o = contact.getFixtureA().getBody().getUserData();
            Object o1 = contact.getFixtureB().getBody().getUserData();

            Object bird, other;

            if (o instanceof Bird) {
                bird = o;
                other = o1;
            } else {
                bird = o1;
                other = o;
            }


            if (bird != null && (other instanceof GroundActor || other instanceof Pipe)) {
                btnClick = false;
                // 小鸟撞击水管 或 地面
                MAssetsManager.instance().playSound(MAssetsManager.instance().dieSound1, MAssetsManager.instance().dieSound);
                groundActor.setLinearVelocity(0, 0);
                dieFlashActor.flash();
                gameStatus = STAT_OVER;

                gameOverActor.show();
                // 显示成绩面板
                boolean isnew = false;
                int best = preferences.getInteger("bestScore", 0);
                if (best <= scoreActor.getScore() && scoreActor.getScore() > 0) {
                    best = scoreActor.getScore();
                    preferences.putInteger("bestScore", best).flush();
                    isnew = true;
                }
                scorePanelActor.show(scoreActor.getScore(), best, isnew);
                // 显示重新开始按钮
                MoveToAction moveToAction = Actions.moveTo(btnPlay.getX(), (102f / 512f * HEIGHT), 0);

                // ParallelAction parallel = Actions.parallel(moveToAction);
                btnPlay.addAction(Actions.delay(1.75f, moveToAction));
                btnScores.addAction(Actions.sequence(Actions.delay(1.75f), Actions.moveTo(btnScores.getX(), moveToAction.getY())));
            }
        }
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
