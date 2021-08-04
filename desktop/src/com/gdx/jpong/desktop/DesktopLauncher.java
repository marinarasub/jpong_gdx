package com.gdx.jpong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.jpong.ui.PongGame;

public class DesktopLauncher {

	static final int WIDTH = 1024;
	static final int HEIGHT = 768;
	private static final String TITLE = "PONG";

	public static void main (String[] arg) {
		//System.out.println(System.getProperty("java.version", null));
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH;
		config.height = HEIGHT;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 24;
		config.samples = 4;
		config.title = TITLE;
		new LwjglApplication(new PongGame(), config);
	}
}
