package com.gdx.jpong.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gdx.jpong.model.Ball;
import com.gdx.jpong.model.Paddle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JPongUI extends ApplicationAdapter {

	final static String AUDIO_PATH = "audio/";

	private ShapeRenderer shape;
	private List<Ball> balls;
	private Paddle paddle;

	private Music music;

	@Override
	public void create () {
		shape = new ShapeRenderer();
		balls = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			//balls.add(new Ball(300, 300, 10, 0, 0));
			balls.add(randomBall());
		}
		paddle = new Paddle(150);
		music = Gdx.audio.newMusic(Gdx.files.internal(AUDIO_PATH + "Kano - Stella-rium.mp3"));
		//music.play();

		Gdx.graphics.setResizable(false);
	}

	private Ball randomBall() {
		Random r = new Random();
		return new Ball(
				r.nextInt(Gdx.graphics.getWidth()),
				r.nextInt(Gdx.graphics.getHeight()),
				15,
				r.nextInt(6) - 3,
				r.nextInt(18) - 9);
	}

	@Override
	public void render () {
		update();
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenUtils.clear(Color.CLEAR);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		balls.forEach(b -> b.draw(shape));
		paddle.draw(shape);
		shape.end();
	}

	public void update() {
		balls.forEach(b -> b.update(paddle));
		paddle.update();
	}
}
