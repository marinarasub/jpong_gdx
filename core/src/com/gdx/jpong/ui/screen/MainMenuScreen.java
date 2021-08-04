package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gdx.jpong.ui.PongGame;

public class MainMenuScreen implements Screen {

    // GAME class
    private PongGame game;


    private SpriteBatch batch;
    private Table menuTable;
    private TextButton playButton, exitButton;

    private Stage stage;

    public MainMenuScreen(PongGame game) {
        this.game = game;
        batch = new SpriteBatch();
        stage = new Stage();
        menuTable = new Table();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        playButton(skin);
        exitButton(skin);
        menuTable.add(playButton).width(200f).height(50f).space(10f).expandX().center().row();
        menuTable.add(exitButton).width(200f).height(50f).space(10f).expandX().center().row();
        menuTable.setFillParent(true);
        stage.addActor(menuTable);
    }

    private void playButton(Skin skin) {
        playButton = new TextButton("PLAY", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.play();
            }
        });
    }

    private void exitButton(Skin skin) {
        exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.exit();
            }
        });
    }

    public int getHeight() {
        return Gdx.graphics.getHeight();
    }

    public int getWidth() {
        return Gdx.graphics.getWidth();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        clear();

//        batch.begin();
//        playButton.draw(batch, 1);
//        batch.end();
        stage.act(delta);
        stage.draw();
    }

    private void clear() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    }

    @Override
    public void dispose() {

    }
}
