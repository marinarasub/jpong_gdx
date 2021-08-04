package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gdx.jpong.ui.screen.GameScreen;
import com.gdx.jpong.ui.screen.MainMenuScreen;

import java.util.*;

public class PongGame extends Game {

	private MainMenuScreen menuScreen;
	private GameScreen game;

	@Override
	public void create() {
		menu();
		Gdx.graphics.setResizable(false);
	}

	public void menu() {
		menuScreen = new MainMenuScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	public void play() {
		game = new GameScreen(this);
		setScreen(game);
	}

	public void exit() {
		Gdx.app.exit();
	}

}
