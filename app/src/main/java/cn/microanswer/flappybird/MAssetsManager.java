package cn.microanswer.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 游戏中的纹理资源管理类
 * Created by Micro on 2018-2-10.
 */

public class MAssetsManager extends Thread implements Disposable, AssetErrorListener {
    private static final String TAG = "MAssetsManager";

    private static MAssetsManager mAssetsManager;
    private AssetManager assetManager;
    private TextureAtlas textureAtlas;
    private boolean disposed;

    public TextureAtlas.AtlasRegion[][] bird = new TextureAtlas.AtlasRegion[3][3];
    public TextureAtlas.AtlasRegion bg_day, bg_night;
    public TextureAtlas.AtlasRegion ground, title;
    public TextureAtlas.AtlasRegion text_ready, text_gameover, howPlay;
    public TextureAtlas.AtlasRegion scoreFont[] = new TextureAtlas.AtlasRegion[10];
    public TextureAtlas.AtlasRegion numberScoreFont[] = new TextureAtlas.AtlasRegion[10];
    public TextureAtlas.AtlasRegion pipe_up, pipe_down;
    public TextureAtlas.AtlasRegion whilte, black, score_panel;
    public TextureAtlas.AtlasRegion btnPlay, btnScore, new_, btnRate;
    public TextureAtlas.AtlasRegion medals[] = new TextureAtlas.AtlasRegion[4];
    public TextureAtlas.AtlasRegion dialogBg, loading,scoreuptip;

    public Sound funSound;
    public Sound birdSound;
    public Sound scoreupSound;
    public Sound dieSound, dieSound1;
    public BitmapFont font1,font2;

    private MAssetsManager() {
        this.disposed = false;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public static MAssetsManager instance() {
        if (mAssetsManager == null) {
            mAssetsManager = new MAssetsManager();
            mAssetsManager.start();
        }
        return mAssetsManager;
    }

    // 构造函数运行完成后第一时间执行该函数
    public MAssetsManager init() {
        assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load("atlas.atlas", TextureAtlas.class);
        assetManager.load("sounds/sfx_swooshing.ogg", Sound.class);
        assetManager.load("sounds/sfx_wing.ogg", Sound.class);
        assetManager.load("sounds/sfx_point.ogg", Sound.class);
        assetManager.load("sounds/sfx_die.ogg", Sound.class);
        assetManager.load("sounds/sfx_hit.ogg", Sound.class);
        assetManager.load("fonts/fnt.fnt", BitmapFont.class);
        assetManager.load("fonts/fnt1.fnt", BitmapFont.class);
        return instance();
    }

    public void d0WhenLoaded() {
        textureAtlas = assetManager.get("atlas.atlas");

        // 一共有3种颜色的小鸟
        // 初始化所有小鸟的纹理
        for (int i = 0; i < bird.length; i++) {
            for (int j = 0; j < bird[i].length; j++) {
                bird[i][j] = textureAtlas.findRegion("bird" + i + "_" + j);
            }
        }

        // 背景
        bg_day = textureAtlas.findRegion("bg_day");
        bg_night = textureAtlas.findRegion("bg_night");

        // 地板
        ground = textureAtlas.findRegion("land");

        // 玩法提示
        text_ready = textureAtlas.findRegion("text_ready");
        howPlay = textureAtlas.findRegion("tutorial");

        // 分数
        for (int i = 0; i < 10; i++) {
            scoreFont[i] = textureAtlas.findRegion("font_0" + (48 + i));
        }

        // 管道
        pipe_down = textureAtlas.findRegion("pipe_down");
        pipe_up = textureAtlas.findRegion("pipe_up");

        // 死了的时候会闪一下白屏，白色纹理
        whilte = textureAtlas.findRegion("white");

        // 游戏结束文案
        text_gameover = textureAtlas.findRegion("text_game_over");

        // 游戏结束后显示的成绩面板
        score_panel = textureAtlas.findRegion("score_panel");

        // 开始游戏按钮
        btnPlay = textureAtlas.findRegion("button_play");

        // 成绩按钮
        btnScore = textureAtlas.findRegion("button_score");

        // 成绩面板上面显示的数字
        for (int i = 0; i < 10; i++) {
            numberScoreFont[i] = textureAtlas.findRegion("number_score_0" + i);
        }

        // 奖牌
        for (int i = 0; i < 4; i++) {
            medals[i] = textureAtlas.findRegion("medals_" + i);
        }

        // 黑色
        black = textureAtlas.findRegion("black");

        // 新的最高成绩
        new_ = textureAtlas.findRegion("new");

        // flappyBird
        title = textureAtlas.findRegion("title");

        // rate
        btnRate = textureAtlas.findRegion("button_rate");

        // 弹出框背景
        dialogBg = textureAtlas.findRegion("dialogbg");

        // 加载图
        loading = textureAtlas.findRegion("loading");

        // 成绩上传提示
        scoreuptip = textureAtlas.findRegion("scoreuptip");

        // 按钮声音， 物件出现声音
        funSound = assetManager.get("sounds/sfx_swooshing.ogg");
        // 分数增加声音
        scoreupSound = assetManager.get("sounds/sfx_point.ogg");
        // 小鸟飞翔声音
        birdSound = assetManager.get("sounds/sfx_wing.ogg");
        // 小鸟死亡声音1
        dieSound = assetManager.get("sounds/sfx_die.ogg");
        // 小鸟死亡声音2
        dieSound1 = assetManager.get("sounds/sfx_hit.ogg");

        font1 = assetManager.get("fonts/fnt.fnt");
        font2 = assetManager.get("fonts/fnt1.fnt");
    }

    public synchronized void playSound(final Sound... s) {
        needPlaySounds.addAll(Arrays.asList(s));
        this.notify();
    }

    ArrayList<Sound> needPlaySounds = new ArrayList<>();
    @Override
    public void run() {
        super.run();
        synchronized (this) {
            while(!disposed) {
                while(needPlaySounds.size() <= 0) {
                    try { this.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                Sound s = needPlaySounds.remove(0);
                s.play();
            }
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        this.disposed = true;
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.log(TAG, "AssetsManager出错:" + asset.toString() + "\n exception:" + throwable.getMessage());
    }
}
