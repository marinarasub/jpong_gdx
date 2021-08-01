package com.gdx.jpong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.jpong.ui.PongGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		config.foregroundFPS = 60;
		config.backgroundFPS = 120;
		config.samples = 4;
		new LwjglApplication(new PongGame(), config);
	}
}
