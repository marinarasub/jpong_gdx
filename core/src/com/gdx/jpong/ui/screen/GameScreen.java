package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.jpong.ui.PongGame;

public abstract class GameScreen implements Screen {
    // GAME class

    protected PongGame game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Music music;

    // TODO shared skin

    public GameScreen(PongGame game) {
        this.game = game;
        camera = new OrthographicCamera(game.getWidth(), game.getHeight());
        viewport = new ScreenViewport(camera);
        resize(game.getWidth(), game.getHeight());
    }

    // gl clear color and depth buffer.
    private void clear() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    // Clear and handle settings.
    @Override
    public void render(float delta) {
        clear();
        if (music != null) { // if updated?
            music.setVolume(game.getVolume());
        }
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(game.getWidth()/2, game.getHeight()/2, 0);
        viewport.apply();
    }

    @Override
    public void show() {
        // TODO set input

    }

//    private class GameInputAdapter extends InputAdapter {
//
//    } TODO

}
