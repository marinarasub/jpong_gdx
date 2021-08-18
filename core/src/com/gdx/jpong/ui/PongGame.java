package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gdx.jpong.exception.FileLoadException;
import com.gdx.jpong.exception.FileSaveException;
import com.gdx.jpong.exception.IllegalSizeException;
import com.gdx.jpong.exception.IllegalValueException;
import com.gdx.jpong.model.SongMap;
import com.gdx.jpong.ui.screen.*;

/**
 * <h1>Main JPong game handler class</h1>
 * <p>
 *     The main game class, extending LibGDX's Game abstract class. Manages screen rendering and switching. Also
 *     includes useful helper methods for accessing core application configuration. <br>
 *     <i> This project is created with LibGDX. </i>
 * </p>
 * <h2>For more information, see:</h2>
 * <ul>
 *     <li><a href="https://libgdx.com/">LibGDX Website</a></li>
 *     <li><a href="https://github.com/libgdx/libgdx">LibGDX Repository</a></li>
 *     <li><a href="https://libgdx.badlogicgames.com/ci/nightlies/docs/api/">LibGDX Javadocs</a></li>
 * </ul>
 * @author marinarasub
 * @version 0.0.0
 */

/* TASK LIST TODO
* - exception handling
* - level editor
*/
public class PongGame extends Game {

	static final String CONFIG = "config.xml";

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

	/**
	 * Creates a JPong application
	 * <p>
	 * Attempts to initialize the JPong application with preferences specified in external config file and creates
	 * sets active screen to main menu, provides a log error if failed
	 */
	@Override
	public void create() {
		Gdx.graphics.setResizable(true);
		try {
			prefs = Gdx.app.getPreferences(CONFIG);
			loadSettings();
		} catch (FileLoadException e) {
			// TODO: load defaults from internal file/static variables
			Gdx.app.error("SETTINGS", "Failed to load");
		}
		menu();
	}


	/**
	 * Sets the active screen to the main menu
	 * <p>
	 * Creates new menu if null or disposed, or loads existing main menu from memory
	 */
	public void menu() {
		if (menu == null) {
			menu = new MainMenuScreen(this);
		}
		setScreen(menu);
	}

	/**
	 * Creates a new play screen using a specific mapped song level and sets as active screen
	 * <p>
	 * Map file parsing is handled by {@link SongSelectScreen}
	 * @param songMap the level you want to play
	 * @see SongSelectScreen
	 */
	public void play(SongMap songMap) {
		play = new PlayScreen(this, songMap);
		setScreen(play);
	}

	/**
	 * Creates a new song select menu and sets as active screen
	 */
	public void songSelect() {
		setScreen(new SongSelectScreen(this));
	}

	/**
	 * Creates a new editor screen for creating your own maps/levels
	 * <p>
	 *     User can choose a song and background image for a level. Allows user to specify timing points, spawn
	 *     balls with time and state (i.e. position, velocity, acceleration) as well as graphical effects.
	 * </p>
	 */
	// TODO: bring into existence
	public void editor() {
		//setScreen(new EditorScreen(this));
	}

	/**
	 * Sets the active screen to the options menu
	 * <p>
	 * Creates new options menu if null or disposed, or loads existing options menu from memory
	 */
	public void options() {
		if (options == null) {
			options = new OptionsScreen(this);
		}
		setScreen(options);
	}


	/* ACCESSORS & SETTERS */

	/**
	 * @return A 2D vector representation of the graphical window size by width, height in pixels
	 * @see #getWidth()
	 * @see #getHeight()
	 */
	public Vector2 getSize() {
		return new Vector2(getWidth(), getHeight());
	}

	/**
	 * @return Width of the application window
	 */
	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	/**
	 * @return Height of the application window
	 */
	public int getHeight() {
		return Gdx.graphics.getHeight();
	}

	/**
	 * Sets the application display mode to either fullscreen or windowed mode using a specified width, height in
	 * pixels.
	 * @param fullscreen If true then set display mode to fullscreen, else windowed mode
	 * @param width Desired window width in pixels, ignored if fullscreen
	 * @param height Desired window height in pixels, ignored if fullscreen
	 * @throws IllegalSizeException If either the width or height is not a positive integer
	 */
	public void changeDisplay(boolean fullscreen, int width, int height) throws IllegalSizeException {
		if (width > 0 && height > 0) {
			Gdx.graphics.setWindowedMode(width, height);
		} else {
			throw new IllegalSizeException("Attempted to change window size to " + width + ", " + height);
		}
		if (fullscreen) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
	}

	/**
	 * Sets the desired universal audio volume for all screens using value provided
	 * <p>
	 * Assumes screen will use {@link #getVolume()} to set audio volume
	 * Volume is represented by a float bound by [0, 1]
	 * @param volume Desired audio volume
	 */
	public void setVolume(float volume) {
		if (volume >= 0 && volume <= 1) {
			this.volume = volume;
		} else {
			throw new IllegalValueException("Attempted to set volume to " + volume);
		}
	}

	/**
	 * @return Universal audio volume for all JPong screens
	 */
	public float getVolume() {
		return volume;
	}

	/**
	 * Enable or disable VSync
	 * <p>
	 * Sets {@link #vSyncEnabled} to signify whether VSync should be enabled and attempts to set VSync through GDX
	 * @param enabled whether or not to enable Vertical Sync
	 */
	public void setVSync(boolean enabled) {
		this.vSyncEnabled = enabled;
		Gdx.graphics.setVSync(vSyncEnabled);
	}

	/**
	 * @return whether VSync is supposed to be enabled
	 */
	public boolean isVSyncEnabled() {
		return vSyncEnabled;
	}

	/**
	 * Sets the universal target frame rate for all screens
	 * <p>
	 *     Attempts to set the target foreground frame rate through GDX
	 *     The game will call {@link #render()} every frame which may cause differing behavior dependent of frame rate
	 *     for the active screen
	 * </p>
	 * @param fps The desired frame rate
	 * @see #render()
	 */
	public void setTargetFPS(int fps) {
		this.targetFPS = fps;
		Gdx.graphics.setForegroundFPS(targetFPS);
	}

	/**
	 * @return The universal target frame rate
	 */
	public int getTargetFPS() {
		return targetFPS;
	}

	/**
	 * @return Whether the application is in fullscreen mode
	 */
	public boolean isFullscreen() {
		return Gdx.graphics.isFullscreen();
	}


	/* CONTROL */

	/**
	 * Renders the current screen based on the screen's render method with the application frame delta time
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Loads the preferences from External file {@link #CONFIG} and sets JPong variables as read from file
	 * Windows location C:\Users\$user$\.prefs\config.xml
	 *
	 * @throws FileLoadException If failed to read settings from file
	 */
	// TODO default settings, non Windows
	private void loadSettings() throws FileLoadException {
		try {
			int width = prefs.getInteger("width");
			int height = prefs.getInteger("height");
			int fps = prefs.getInteger("targetFPS");
			boolean fullscreen = prefs.getBoolean("fullscreen");
			boolean vSync = prefs.getBoolean("vSyncEnabled");
			setVSync(vSync);
			setTargetFPS(fps);
			changeDisplay(fullscreen, width, height);
			Gdx.graphics.setResizable(false);
		} catch (GdxRuntimeException e) {
			throw new FileLoadException("Failed to load app preferences");
		}
		Gdx.app.log("SETTINGS", "loaded");
	}

	/**
	 * Writes the JPong settings variables to prefs file {@link #CONFIG}
	 * Windows location C:\Users\$user$\.prefs\config.xml
	 *
	 * @throws FileSaveException If could not write to preferences file
	 */
	private void saveSettings() throws FileSaveException {
		try {
			prefs.putInteger("width", getWidth());
			prefs.putInteger("height", getHeight());
			prefs.putInteger("targetFPS", targetFPS);
			prefs.putBoolean("fullscreen", isFullscreen());
			prefs.putBoolean("vSyncEnabled", vSyncEnabled);
			prefs.putInteger("MSAA", 4); // TODO figure out how to set with libgdx lwjgl3
			prefs.flush();

		} catch (GdxRuntimeException e) {
			throw new FileSaveException("Failed to save app preferences");
		}
		Gdx.app.log("SETTINGS", "saved");
	}

	/**
	 * Exits the JPong application
	 * <p>
	 * Attempts to save the settings on exit, provides a log error if failed
	 */
	public void exit() {
		try {
			saveSettings();
		} catch (FileSaveException e) {
			Gdx.app.error("SETTINGS", "Failed to save");
		}
		Gdx.app.exit();
	}

}
