package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.gdx.jpong.ui.PongGame;

public abstract class GameScreen implements Screen {
    // GAME class
    protected PongGame game;

    public GameScreen(PongGame game) {
        this.game = game;
    }

    protected void clear() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

}
