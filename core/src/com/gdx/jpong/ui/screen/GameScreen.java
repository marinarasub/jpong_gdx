package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Screen;
import com.gdx.jpong.ui.PongGame;

public abstract class GameScreen implements Screen {
    // GAME class
    protected PongGame game;

    public GameScreen(PongGame game) {
        this.game = game;
    }

}
