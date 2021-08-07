package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gdx.jpong.ui.PongGame;

public class MainMenuScreen extends GameScreen implements Screen {

    //private SpriteBatch batch;
    private Skin skin;
    private Label title;
    private Table menuTable;
    private TextButton play, editor, options, exit;

    private Stage stage;

    public MainMenuScreen(PongGame game) {
        super(game);
        //batch = new SpriteBatch();
        stage = new Stage();
        menuTable = new Table();
        //menuTable.debug();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        title = new Label("JPong", skin);
        playButton();
        optionsButton();
        exitButton();
        editorButton();
        menuTable.add(title).expandX().pad(10f).center().row();
        menuTable.add(play).width(300f).height(60f).space(10f).expandX().center().row();
        menuTable.add(editor).width(300f).height(60f).space(10f).expandX().center().row();
        menuTable.add(options).width(300f).height(60f).space(10f).expandX().center().row();
        menuTable.add(exit).width(300f).height(60f).space(10f).expandX().center().row();
        menuTable.setFillParent(true);
        stage.addActor(menuTable);
    }

    private void playButton() {
        play = new TextButton("PLAY", skin);
        play.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Play clicked");
                game.songSelect();
            }
        });
    }

    private void editorButton() {
        editor = new TextButton("EDIT", skin);
        editor.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Edit clicked");
                game.editor();
            }
        });
    }

    private void optionsButton() {
        options = new TextButton("OPTIONS", skin);
        options.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Options clicked");
                // TODO options menu
            }
        });
    }

    private void exitButton() {
        exit = new TextButton("EXIT", skin);
        exit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Exit clicked");
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
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        clear();

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
        // TODO free
        stage.dispose();
    }
}
