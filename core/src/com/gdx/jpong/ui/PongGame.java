package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jpong.model.SongMap;
import com.gdx.jpong.ui.screen.EditorScreen;
import com.gdx.jpong.ui.screen.PlayScreen;
import com.gdx.jpong.ui.screen.MainMenuScreen;
import com.gdx.jpong.ui.screen.SongSelectScreen;

public class PongGame extends Game {

	private Preferences prefs;
	private MainMenuScreen menu;
	private PlayScreen play;

	@Override
	public void create() {
		prefs = Gdx.app.getPreferences("config.xml");
		Gdx.graphics.setResizable(true);
		loadSettings();
		menu = new MainMenuScreen(this);
		menu();
	}


	public void menu() {
		setScreen(menu);
	}

	public void play(SongMap songMap) {
		play = new PlayScreen(this, songMap);
		setScreen(play);
	}

	public void songSelect() {
		setScreen(new SongSelectScreen(this));
	}

	public void editor() {
		//setScreen(new EditorScreen(this)); TODO
	}

	public void options() {
		// TODO
	}

	public Vector2 getSize() {
		return new Vector2(getWidth(), getHeight());
	}

	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	public int getHeight() {
		return Gdx.graphics.getHeight();
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
		Gdx.graphics.setResizable(false);
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
