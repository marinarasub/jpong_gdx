package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.*;
import com.gdx.jpong.model.entity.PongAI;
import com.gdx.jpong.model.entity.PongPlayer;
import com.gdx.jpong.ui.screen.GameScreen;

import java.util.*;

public class PongGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen(this));
		Gdx.graphics.setResizable(false);
	}

	@Override
	public void render() {
		super.render();
	}

	public void exit() {
		Gdx.app.exit();
	}

}
