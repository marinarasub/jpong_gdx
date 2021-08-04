package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.gdx.jpong.ui.screen.GameScreen;
import com.gdx.jpong.ui.screen.MainMenuScreen;
public class PongGame extends Game {

	private MainMenuScreen menuScreen;
	private GameScreen game;

	@Override
	public void create() {
		menu();
		Gdx.graphics.setResizable(false);
	}

	public void menu() {
		disposeCurrentScreen();
		menuScreen = new MainMenuScreen(this);
		setScreen(menuScreen);
	}

	public void play() {
		disposeCurrentScreen();
		game = new GameScreen(this);
		setScreen(game);
	}

	@Override
	public void render() {
		super.render();
	}

	private void disposeCurrentScreen() {
		if (screen != null)
			screen.dispose();
		System.gc();
	}

	public void exit() {
		Gdx.app.exit();
	}

}
