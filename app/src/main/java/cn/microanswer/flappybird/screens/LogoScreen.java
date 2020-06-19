package cn.microanswer.flappybird.screens;

import cn.microanswer.flappybird.FlappyBirdGame;
import cn.microanswer.flappybird.sprites.LogoActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static cn.microanswer.flappybird.FlappyBirdGame.HEIGHT;
import static cn.microanswer.flappybird.FlappyBirdGame.WIDTH;

/**
 * 显示logo的屏幕
 * Created by Micro on 2018-2-19.
 */

public class LogoScreen extends BaseScreen {

    private Stage stage;
    private LogoActor logoActor;

    private Texture progressbg, progress, progressforground;
    private float progressfloat, px, py, pw, ph, ppx, ppy, ppw, pph, ppwa;

    public LogoScreen(OrthographicCamera camera, Batch batch) {
        super(camera, batch);
    }

    public void setProgress(float progress) {
        this.progressfloat = progress;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        stage = new Stage(viewport, batch);
        logoActor = new LogoActor().init(this);
        stage.addActor(logoActor);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.drawPixel(0, 0, Color.rgba8888(1f, 1f, 1f, 1f));
        progressbg = new Texture(pixmap);
        pixmap.drawPixel(0, 0, Color.rgba8888(0f, 0f, 0f, 1f));
        progress = new Texture(pixmap);
        pixmap.drawPixel(0, 0, Color.rgba8888(1f, 1f, 1f, 1f));
        progressforground = new Texture(pixmap);
        pixmap.dispose();

        pw = logoActor.getWidth();
        ph = FlappyBirdGame.HEIGHT * (4f / 512f);
        px = (FlappyBirdGame.WIDTH - pw) / 2f;
        py = logoActor.getY() - (ph);

        pph = ph * .7f;
        ppwa = pw - (ph - pph);
        ppw = ppwa * progressfloat;
        ppx = px + ((pw - ppwa) / 2f);
        ppy = py + ((ph - pph) / 2f);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
        batch.begin();
        // 绘制进度条
        batch.draw(progressbg, px, py, pw, ph);
        batch.draw(progress, ppx, ppy, ppwa, pph);
        ppw = ppwa * progressfloat;
        batch.draw(progressforground, ppx, ppy, ppw, pph);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.dispose();
        logoActor.dispose();
        progress.dispose();
        progressbg.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        logoActor.dispose();
        progressbg.dispose();
        progress.dispose();
    }
}
