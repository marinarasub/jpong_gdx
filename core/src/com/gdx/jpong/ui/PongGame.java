package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.jpong.model.SongMap;
import com.gdx.jpong.ui.screen.PlayScreen;
import com.gdx.jpong.ui.screen.MainMenuScreen;
import com.gdx.jpong.ui.screen.SongSelectScreen;

public class PongGame extends Game {

	private Preferences prefs;
	private MainMenuScreen menuScreen;
	private SongSelectScreen songSelect;
	private PlayScreen game;

	@Override
	public void create() {
		prefs = Gdx.app.getPreferences("config.xml");
		Gdx.graphics.setResizable(true);
		loadSettings();
		newMenu();
		menu();
	}

	private void newMenu() {
		menuScreen = new MainMenuScreen(this);
		songSelect = new SongSelectScreen(this);
	}

	public void menu() {
		setScreen(menuScreen);
	}

	public void play() {
		SongMap songMap = new SongMap("dj TAKA - quaver.mp3");
		songMap.setBackground(new Texture("images/89136015_p0.jpg"));
		songMap.setBpm(186.f);
		songMap.setStartOffset(1.198f);
		game = new PlayScreen(this, songMap);
		setScreen(game);
	}

	public void songSelect() {
		setScreen(songSelect);
	}

	@Override
	public void render() {
		super.render();
	}

	private void loadSettings() {
		int width = prefs.getInteger("width");
		int height = prefs.getInteger("height");
		int fps = prefs.getInteger("targetFPS");
		boolean fullscreen = prefs.getBoolean("fullscreen");
		boolean vSync = prefs.getBoolean("vSyncEnabled");
		if (fullscreen) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else {
			Gdx.graphics.setWindowedMode(width, height);
		}
		Gdx.graphics.setForegroundFPS(fps);
		Gdx.graphics.setVSync(vSync);
	}

	private void saveSettings() {
//		prefs.putInteger("width", 1024);
//		prefs.putInteger("height", 768);
//		prefs.putInteger("targetFPS", 60);
//		prefs.putBoolean("fullscreen", false);
//		prefs.putBoolean("vSyncEnabled", false);
//		prefs.putInteger("MSAA", 4);
//		prefs.flush();
	}

	public void exit() {
		saveSettings();
		Gdx.app.exit();
	}

}
