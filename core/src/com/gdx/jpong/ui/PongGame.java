package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jpong.model.SongMap;
import com.gdx.jpong.ui.screen.*;

public class PongGame extends Game {

	// TODO VIRTUAL RESOLUTION
	public float virtualWidth;
	public float virtualHeight;

	private Preferences prefs;
	private MainMenuScreen menu;
	private PlayScreen play;
	private OptionsScreen options;

	// IN GAME SETTINGS
	private float volume = 1.f; // DEFAULT
	private boolean vSyncEnabled = true; // DEF.
	private int targetFPS;

	@Override
	public void create() {
		prefs = Gdx.app.getPreferences("config.xml");
		Gdx.graphics.setResizable(true);
		loadSettings();
		menu = new MainMenuScreen(this);
		options = new OptionsScreen(this);
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
		setScreen(options);
	}

	// ACCESS & SET

	public Vector2 getSize() {
		return new Vector2(getWidth(), getHeight());
	}

	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	public int getHeight() {
		return Gdx.graphics.getHeight();
	}

	// Sets display to fullscreen if true,
	// else windowed mode of size width, height
	public void changeDisplay(boolean fullscreen, int width, int height) {
		Gdx.graphics.setWindowedMode(width, height);
		if (fullscreen) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
	}

	public void setVolume(float volume) {
		if (volume >= 0 && volume <= 1) {
			this.volume = volume;
		}
	}

	public float getVolume() {
		return volume;
	}

	public void setVSync(boolean enabled) {
		this.vSyncEnabled = enabled;
		Gdx.graphics.setVSync(vSyncEnabled);
	}

	public boolean isVSyncEnabled() {
		return vSyncEnabled;
	}

	public int getTargetFPS() {
		return targetFPS;
	}

	public void setTargetFPS(int fps) {
		this.targetFPS = fps;
		Gdx.graphics.setForegroundFPS(targetFPS);
	}

	// CONTROL

	// Render current screen
	@Override
	public void render() {
		super.render();
	}

	// load from file
	private void loadSettings() {
		int width = prefs.getInteger("width");
		int height = prefs.getInteger("height");
		int fps = prefs.getInteger("targetFPS");
		boolean fullscreen = prefs.getBoolean("fullscreen");
		boolean vSync = prefs.getBoolean("vSyncEnabled");
		setVSync(vSync);
		setTargetFPS(fps);
		changeDisplay(fullscreen, width, height);
		Gdx.graphics.setResizable(false);
	}

	private void saveSettings() {
		prefs.putInteger("width", getWidth());
		prefs.putInteger("height", getHeight());
		prefs.putInteger("targetFPS", targetFPS);
		prefs.putBoolean("fullscreen", isFullscreen());
		prefs.putBoolean("vSyncEnabled", vSyncEnabled);
		prefs.putInteger("MSAA", 4); // TODO " " "
		prefs.flush();
		Gdx.app.log("SETTINGS", "saved");
	}

	public boolean isFullscreen() {
		return Gdx.graphics.isFullscreen();
	}

	public void exit() {
		saveSettings();
		Gdx.app.exit();
	}

}
