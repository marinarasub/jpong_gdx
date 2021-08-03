package com.gdx.jpong.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.*;
import com.gdx.jpong.model.entity.PongAI;
import com.gdx.jpong.model.entity.PongEntity;
import com.gdx.jpong.model.entity.PongPlayer;

import java.util.*;

public class PongGame extends Game {

	static final int TEST_BALLS = 0; // FOR AI SORT TEST

	private float height;  // LOGICAL HEIGHT
	private float width;
	private final int fixedUPS = 120; // fixed Updates per second
	private float fixedTimeCarryover = 0; // time running until next update;

	private ShapeRenderer shape;
	private SpriteBatch batch;
	private Sprite background;

	private List<Ball> balls;
	private PongPlayer player;
	private PongAI ai;
	private Clock gameTime;

	private SongMap songMap;

	BitmapFont scoreLabel;

	@Override
	public void create() {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		balls = new LinkedList<>();
		players();
		gameTime = new Clock();
		for (int i = 0; i < TEST_BALLS; i++) {
			//balls.add(new Ball(300, 300, 10, 0, 0));
			balls.add(randomBall());
		}
		song();
		background();
		scoreLabel();

		Gdx.graphics.setResizable(false);
	}

	private void players() {
		float buffer = 50.f;
		player = new PongPlayer(new Paddle(250.f, buffer));
		player.setPaddleColor(Color.GREEN);
		ai = new PongAI(new Paddle(200.f, getHeight() - buffer));
		ai.setPaddleColor(Color.RED);
	}

	private void background() {
		Texture backgroundTex = songMap.getBackground();
		if (backgroundTex != null) {
			backgroundTex.setAnisotropicFilter(16.f);
			background = new Sprite(backgroundTex);
			background.setAlpha(1 - songMap.getBackgroundDim()); // assume <= 1
			background.setPosition(0, 0);
			background.setOrigin(0,0);

			float heightRatio = getHeight() / background.getHeight();
			float widthRatio = getWidth() / background.getWidth();

			if (heightRatio > widthRatio) {
				background.setScale(getHeight() / background.getHeight());
				background.setPosition((getWidth() - background.getWidth()) / 2, 0);
			} else {
				background.setScale(getWidth() / background.getWidth());
				background.setPosition(0,(getHeight() - background.getHeight()) / 2);
			}
		}
	}

	private void scoreLabel() {
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 24;
		fontParameter.color = Color.GREEN;
		scoreLabel = fontGenerator.generateFont(fontParameter);
	}

	private void song() {
//		songMap = new SongMap("Kano - Stella-rium.mp3", gameTime);
//		songMap.setBpm(177.f);
//		songMap.setStartOffset(0.951f);
//		songMap = new SongMap("ParagonX9 - Chaoz Fantasy.mp3", gameTime);
//		songMap.setBpm(162.35f);
//		songMap.setStartOffset(0.958f);
		songMap = new SongMap("DM DOKURO - SAVE (Encore).mp3", gameTime);
		songMap.setBpm(180.f);
		songMap.setStartOffset(0.476f);

		songMap.setBackground(new Texture("images/70858371_p0_master1200.jpg")); // TEST LANDSCAPE
		//songMap.setBackground(new Texture("images/89136015_p0.jpg")); // TEST PORTRAIT
		songMap.setBackgroundDim(0.75f);

		songMap.start();
		songMap.setOnCompletionListener(music -> exit());
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
				10 * (r.nextFloat() - 0.5f),
				gameTime.getTimeElapsedSeconds() % 2 == 1 ?
						velMultiplier * (r.nextFloat()) + 150.f
						: velMultiplier * (-r.nextFloat()) - 150.f,
				50.f * (r.nextFloat() - 0.5f),
				0);
	}

	public int getHeight() {
		return Gdx.graphics.getHeight();
	}

	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	private float getFixedTime() {
		return (float) 1 / fixedUPS;
	}

	private void addFixedTimeCarryover(float amt) {
		fixedTimeCarryover += amt;
	}

	private void subtractFixedTimeCarryover(float amt) {
		if (fixedTimeCarryover - amt >= 0)
			fixedTimeCarryover -= amt;
	}

	@Override
	public void render() {
		super.render();
		fixedUpdate(gameTime.getDeltaTime());
		update(gameTime.getDeltaTime());
		clear();
		renderBackground();
		renderGraphics();
		renderHUD();
	}

	private void renderBackground() {
		batch.begin();
		if (background != null) background.draw(batch);
		batch.end();
	}

	private void renderHUD() {
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

	private void renderGraphics() {
		shape.begin(ShapeRenderer.ShapeType.Filled);
		balls.forEach(b -> b.draw(shape));
		player.draw(shape);
		ai.draw(shape);
		shape.end();
	}

	private void clear() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	private void handleTime(float deltaTime) {
		gameTime.update();
		songMap.update();
		if (songMap.isWaiting()) { // TODO
			songMap.endWaiting();
			//if (balls.size() == 0)
			balls.add(randomBall());
		}
	}

	public void fixedUpdate(float deltaTime) {
		int numUpdates = (int) ((deltaTime + fixedTimeCarryover) / getFixedTime());
		addFixedTimeCarryover(deltaTime);
		subtractFixedTimeCarryover(numUpdates * getFixedTime());
		for (int i = 0; i < numUpdates; i++) {
			balls.forEach(b -> b.update(getFixedTime(), player.getPaddle()));
			balls.forEach(b -> b.update(getFixedTime(), ai.getPaddle()));
		}
	}

	public void update(float deltaTime) {
		handleTime(deltaTime);
		handleMouseInput();
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
					handleScore(player, b);
				} else {
					handleScore(ai, b);
				}
				i.remove();
			}
		}
	}

	private void handleScore(PongEntity p,  Ball b) {
		if (b.isImmune()) {
			p.addScore(2);
		} else {
			p.addScore(1);
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
