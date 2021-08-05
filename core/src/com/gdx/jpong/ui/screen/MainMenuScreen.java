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
import com.gdx.jpong.model.SongMap;
import com.gdx.jpong.ui.PongGame;

public class MainMenuScreen extends GameScreen implements Screen {

    //private SpriteBatch batch;
    private Table menuTable;
    private TextButton playButton, exitButton, optionsButton;

    private Stage stage;

    public MainMenuScreen(PongGame game) {
        super(game);
        //batch = new SpriteBatch();
        stage = new Stage();
        menuTable = new Table();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        playButton(skin);
        optionsButton(skin);
        exitButton(skin);
        menuTable.add(playButton).width(300f).height(60f).space(10f).expandX().center().row();
        menuTable.add(optionsButton).width(300f).height(60f).space(10f).expandX().center().row();
        menuTable.add(exitButton).width(300f).height(60f).space(10f).expandX().center().row();
        menuTable.setFillParent(true);
        stage.addActor(menuTable);
    }

    private void playButton(Skin skin) {
        playButton = new TextButton("PLAY", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                //game.songSelect();
                Gdx.app.log("BUTTON", "Play clicked");
                game.play();
            }
        });
    }

    private void exitButton(Skin skin) {
        exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                Gdx.app.log("BUTTON", "Exit clicked");
                game.exit();
            }
        });
    }

    private void optionsButton(Skin skin) {
        optionsButton = new TextButton("OPTIONS", skin);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                Gdx.app.log("BUTTON", "Options clicked");
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
        Gdx.input.setInputProcessor(stage);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // TODO free
        stage.dispose();
    }
}
