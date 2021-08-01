package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.*;
import com.gdx.jpong.model.entity.PongAI;
import com.gdx.jpong.model.entity.PongPlayer;

import java.util.*;

public class PongGame extends Game {

	static final int TEST_BALLS = 0;

	private float height;  // LOGICAL HEIGHT
	private float width;

	private ShapeRenderer shape;
	private SpriteBatch batch;

	private List<Ball> balls;
	private PongPlayer player;
	private PongAI ai;
	private Clock gameTime;

	private SongMap music;

	BitmapFont scoreLabel;

	@Override
	public void create() {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		balls = new LinkedList<>();
		for (int i = 0; i < TEST_BALLS; i++) {
			//balls.add(new Ball(300, 300, 10, 0, 0));
			balls.add(randomBall());
		}
		player = new PongPlayer(new Paddle(200.f, 50));
		player.setPaddleColor(Color.GREEN);
		ai = new PongAI(new Paddle(200.f, getHeight() - 50));
		ai.setPaddleColor(Color.RED);
		gameTime = new Clock();
		music();
		scoreLabel();

		Gdx.graphics.setResizable(false);
	}

	private void scoreLabel() {
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 24;
		fontParameter.color = Color.GREEN;
		scoreLabel = fontGenerator.generateFont(fontParameter);
	}

	private void music() {
		music = new SongMap("Kano - Stella-rium.mp3", gameTime);
		music.setBpm(177.f);
		music.start();
		music.setOnCompletionListener(music -> exit());
	}

	private Ball randomBall() {
		Random r = new Random();
		float velMultiplier = 100.f;
		return new Ball(
				(gameTime.getTimeElapsedSeconds() * 50) % getWidth(),
				gameTime.getTimeElapsedSeconds() % 2 == 1 ?
						0 + player.getPaddle().getHeight()
						: getHeight() - 0 - ai.getPaddle().getHeight(), // TODO set const
				// for 50
				15.f, // TODO const ball radius
				5 * (r.nextFloat() - 0.5f),
				gameTime.getTimeElapsedSeconds() % 2 == 1 ?
						velMultiplier * (r.nextFloat()) + 100.f
						: velMultiplier * (-r.nextFloat()) - 100.f);
	}

	private int getHeight() {
		return Gdx.graphics.getHeight();
	}

	private int getWidth() {
		return Gdx.graphics.getWidth();
	}

	@Override
	public void render() {
		update(gameTime.getDeltaTime());
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
						+ "\nPSCORE: " + player.getScore()
						+ "\nAISCORE: " + ai.getScore()
						+ "\nTOTAL SCORE: " + (player.getScore() - ai.getScore())
						+ "\n" + gameTime.getDisplayTime(),
				20,
				getHeight() - 20);
		batch.end();
	}

	private void handleTime(float deltaTime) {
		gameTime.update();
		music.update();
		if (music.isWaiting()) { // TODO
			music.endWaiting();
			balls.add(randomBall());
		}
	}

	public void update(float deltaTime) {
		handleTime(deltaTime);
		handleMouseInput();
		balls.forEach(b -> b.update(deltaTime, player.getPaddle()));
		balls.forEach(b -> b.update(deltaTime, ai.getPaddle()));
		handleOutOfBounds();
		player.update(deltaTime);
		ai.update(deltaTime, balls);
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
			balls.add(randomBall());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			balls.forEach(System.out::println);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			pause(); // tODO
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
