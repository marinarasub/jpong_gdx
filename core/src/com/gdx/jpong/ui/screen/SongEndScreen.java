package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gdx.jpong.model.map.SongMap;
import com.gdx.jpong.model.entity.PongAI;
import com.gdx.jpong.model.entity.PongEntity;
import com.gdx.jpong.model.entity.PongPlayer;
import com.gdx.jpong.ui.PongGame;

public class SongEndScreen extends GameScreen {

    //private SpriteBatch batch;
    private Table buttonTable;
    private TextButton replayButton, menuButton;

    private SongMap song;
    private PongEntity player, ai;

    private Stage stage;
    /* CONTROL */

    public SongEndScreen(final PongGame game, SongMap song, PongPlayer player, PongAI ai) {
        super(game);
        this.song = song;
        this.player = player;
        this.ai = ai;
        stage = new Stage();
        buttonTable = new Table();
        buttonTable.pad(20.f);
        buttonTable.bottom().right();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        replayButton(skin);
        menuButton(skin);
        buttonTable.add(replayButton).width(300f).height(60f).space(10f).row();
        buttonTable.add(menuButton).width(300f).height(60f).space(10f).row();
        buttonTable.setFillParent(true);
        stage.addActor(buttonTable);
    }

    private void replayButton(Skin skin) {
        replayButton = new TextButton("REPLAY", skin);
        replayButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                dispose();
                game.play(song);
            }
        });
    }

    private void menuButton(Skin skin) {
        menuButton = new TextButton("MENU", skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                dispose();
                game.menu();
            }
        });
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        song.dispose();
    }
}
