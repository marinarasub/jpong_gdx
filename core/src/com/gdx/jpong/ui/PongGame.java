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
import com.gdx.jpong.model.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PongGame extends Game {

	static final String AUDIO_PATH = "audio/";
	static final int TEST_BALLS = 0;
	static final float BPM = 177; // TODO songmap
	static final float SPAWN_DELAY = 60 / BPM;

	private ShapeRenderer shape;
	private SpriteBatch batch;

	private List<Ball> balls;
	private PongPlayer player;
	private PongAI ai;
	private Clock gameTime;

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
		player = new PongPlayer(new Paddle(200.f, 50));
 		ai = new PongAI(new Paddle(200.f, getHeight() - 50));
 		gameTime = new Clock();
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
		music.setOnCompletionListener(new Music.OnCompletionListener() {
			@Override
			public void onCompletion(Music music) {
				exit();
			}
		});
	}

	private Ball randomBall() {
		Random r = new Random();
		return new Ball(
				(gameTime.getTimeElapsedSeconds() * 50) % getWidth(),
				gameTime.getTimeElapsedSeconds() % 2 == 1 ?
						50 + player.getPaddle().getHeight()
						: getHeight() - 50 - ai.getPaddle().getHeight(), // TODO set const
				// for 50
				15.f, // TODO const ball radius
				1 * (r.nextFloat() - 0.5f),
				gameTime.getTimeElapsedSeconds() % 2 == 1 ?
						3 * (r.nextFloat()) + 1
						: -3 * (r.nextFloat()) - 1);
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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//ScreenUtils.clear(Color.CLEAR);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		balls.forEach(b -> b.draw(shape));
		player.draw(shape);
		ai.draw(shape);
		shape.end();

		batch.begin();
		scoreLabel.draw(
				batch, "FPS: " + Gdx.graphics.getFramesPerSecond()
						+ "\nSCORE: " + player.getScore()
						+ "\nAISCORE: " +ai.getScore()
						+ "\n" + gameTime.getDisplayTime(),
				20,
				getHeight() - 20);
		batch.end();
	}

	private void handleTime() {
		gameTime.update();
		if (gameTime.getIntervalElapsed() >= SPAWN_DELAY) {
			balls.add(randomBall());
			gameTime.setMarkedTime();
		}
	}

	public void update() {
		handleTime();
		handleMouseInput();
		balls.forEach(b -> b.update(player.getPaddle()));
		balls.forEach(b -> b.update(ai.getPaddle()));
		handleOutOfBounds();
		player.update();
		ai.update(balls);
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
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			exit();
		}
	}

	private void exit() {
		this.dispose();
		Gdx.app.exit();
	}
}
