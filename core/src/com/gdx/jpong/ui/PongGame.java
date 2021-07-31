package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gdx.jpong.model.Ball;
import com.gdx.jpong.model.Paddle;
import com.gdx.jpong.model.PongAI;
import com.gdx.jpong.model.PongPlayer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PongGame extends Game {

	static final String AUDIO_PATH = "audio/";
	static final int TEST_BALLS = 0;

	private ShapeRenderer shape;
	private SpriteBatch batch;

	private List<Ball> balls;
	private PongPlayer player;
	private PongAI ai;

	private Music music;

	BitmapFont scoreLabel;

	@Override
	public void create () {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		balls = new LinkedList<>();
		for (int i = 0; i < TEST_BALLS; i++) {
			//balls.add(new Ball(300, 300, 10, 0, 0));
			balls.add(randomBall());
		}
		player = new PongPlayer(new Paddle(150.f, 50));
 		ai = new PongAI(new Paddle(150.f, getHeight() - 50));
		music();

		scoreLabel();

		Gdx.graphics.setResizable(false);
	}

	private void scoreLabel() {
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 32;
		fontParameter.color = Color.GREEN;
		scoreLabel = fontGenerator.generateFont(fontParameter);
	}

	private void music() {
		music = Gdx.audio.newMusic(Gdx.files.internal(AUDIO_PATH + "Kano - Stella-rium.mp3"));
		music.play();
	}

	private Ball randomBall() {
		Random r = new Random();
		return new Ball(
				r.nextInt(getWidth()),
				r.nextInt(getHeight()),
				25.f,
				2 * (r.nextFloat() - 0.5f),
				6 * (r.nextFloat() - 0.5f));
	}

	private int getHeight() {
		return Gdx.graphics.getHeight();
	}

	private int getWidth() {
		return Gdx.graphics.getWidth();
	}

	@Override
	public void render () {
		update();
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenUtils.clear(Color.CLEAR);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		balls.forEach(b -> b.draw(shape));
		player.draw(shape);
		ai.draw(shape);
		shape.end();

		batch.begin();
		scoreLabel.draw(batch, "SCORE: " + player.getScore(), 20, getHeight() - 50);
		batch.end();
	}

	public void update() {
		handleMouseInput();
		balls.forEach(b -> b.update(player.getPaddle()));
		balls.forEach(b -> b.update(ai.getPaddle()));
		handleOutOfBounds();
		player.update();
	}

	private void handleOutOfBounds() {
		Iterator<Ball> i = balls.listIterator();
		while (i.hasNext()) {
			Ball b = i.next();
			float yOut = b.isOutOfYBounds();
			if (yOut != 0) {
				if (yOut >= getHeight()) {
					player.addScore(1);
				} else {
					ai.addScore(1);
				}
				i.remove();
			}
		}
	}

	private void handleMouseInput() {
		if (Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
			Random r = new Random();
			balls.add(new Ball(
					(float) Gdx.input.getX(),
					(float) getHeight() - Gdx.input.getY(),
					15.f,
					2 * (r.nextFloat() - 0.5f),
					3 * (r.nextFloat()) + 1));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			balls.forEach(System.out::println);
		}
	}
}
