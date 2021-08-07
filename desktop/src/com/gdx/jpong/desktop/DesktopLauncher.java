package com.gdx.jpong.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.gdx.jpong.ui.PongGame;

public class DesktopLauncher {

	static final int WIDTH = 1024;
	static final int HEIGHT = 768;
	private static final String TITLE = "JPong v0.0.0";

	private static Lwjgl3ApplicationConfiguration config;


	public static void main (String[] arg) {
		//System.out.println(System.getProperty("java.version", null));
		config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(TITLE);
		int samples = 4;
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, samples);
		new Lwjgl3Application(new PongGame(), config);
	}
}
