package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.Ball;
import com.gdx.jpong.model.Paddle;
import com.gdx.jpong.model.SongMap;
import com.gdx.jpong.model.entity.PongAI;
import com.gdx.jpong.model.entity.PongEntity;
import com.gdx.jpong.model.entity.PongPlayer;
import com.gdx.jpong.ui.PongGame;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represents screen used for gameplay of maps
 */
public class PlayScreen extends GameScreen implements Screen {

    // NUMBERS
    private float height;  // LOGICAL HEIGHT
    private float width;
    private final int fixedUPS = 120; // fixed Updates per second
    private float fixedTimeCarryover = 0; // time running until next update;

    // RENDER TOOLS
    private ShapeRenderer shape;
    private SpriteBatch batch;

    // GAME STATE
    private boolean running = true;

    // OBJECTS & ENTITIES
    private List<Ball> balls;
    private PongPlayer player;
    private PongAI ai;

    // UI
    private BitmapFont scoreLabel;
    private Sprite background;
    private float dim;

    // INPUT
    //private Stage stage;

    // MAP
    private SongMap songMap;


    /* SETUP METHODS */

    public PlayScreen(final PongGame game, SongMap song) {
        super(game);

        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        //stage = new Stage();

        balls = new LinkedList<>();
        song(song);
        background();
        players();
        scoreLabel();
    }

    private void players() {
        float buffer = 50.f;
        player = new PongPlayer(new Paddle(250.f, buffer));
        player.setPaddleColor(Color.GREEN);
        ai = new PongAI(
                new Paddle(200.f, game.getHeight() - buffer),
                songMap.getDifficulty() * 1000,
                1.5f - songMap.getDifficulty() / 10);
        ai.setPaddleColor(Color.RED);
    }

    private void background() {
        Texture backgroundTex = songMap.getBackground();
        if (backgroundTex != null) {
            backgroundTex.setAnisotropicFilter(16.f);
            background = new Sprite(backgroundTex);
            background.setAlpha(1 - songMap.getBackgroundDim()); // assume <= 1
            background.setOrigin(0,0);

            float heightRatio = game.getHeight() / background.getHeight();
            float widthRatio = game.getWidth() / background.getWidth();
            // TODO Custom x, y offset
            if (heightRatio > widthRatio) {
                background.setScale(heightRatio);
                background.setPosition((game.getWidth() - heightRatio * background.getWidth()) / 2, 0);
                System.out.println("H > W\n");
            } else {
                background.setScale(widthRatio);
                background.setPosition(0,(game.getHeight() - widthRatio * background.getHeight()) / 2);
                System.out.println("W > H\n");
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

    private void song(SongMap song) {
        songMap = song;
        songMap.start();
        songMap.setOnCompletionListener(music -> {
            //game.menu();
            game.setScreen(new SongEndScreen(game, songMap, player, ai));
            dispose();
        });
        music = song.getMusic();
    }


    /* MUTATION & ACCESSORS */

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


    /* RENDER */

    @Override
    public void render(float delta) {
        if (running) {
            fixedUpdate(delta);
            update(delta);
        }
        super.render(delta);
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
                        + "\n" + songMap.getClockTime(),
                        //+ "\n" + songMap.getClockTime(),
                20,
                game.getHeight() - 20);
        batch.end();
    }

    private void renderGraphics() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        balls.forEach(b -> b.draw(shape));
        player.draw(shape);
        ai.draw(shape);
        shape.end();
    }

    /* UPDATE HANDLING */

    private void handleTime(float deltaTime) {
        songMap.update(deltaTime);
        if (songMap.isWaitingSpawn()) {
            do {
                Float time = songMap.getSpawnTime();
                List<Ball> list = songMap.getSpawns(time);
                balls.addAll(list);
                songMap.removeAlreadySpawned(time);
            } while (songMap.isOvertime());
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
        player.update(deltaTime);
        ai.update(deltaTime, balls);
        handleOutOfBounds();
    }

    private void handleOutOfBounds() {
        Iterator<Ball> i = balls.listIterator();
        while (i.hasNext()) {
            Ball b = i.next();
            if (b.isOutOfYBounds(0-b.getRadius(), game.getHeight()+b.getRadius())) {
                if (b.getY() >= game.getHeight()) {
                    handleScore(player, b);
                } else {
                    handleScore(ai, b);
                }
                i.remove();
            }
        }
    }

    private void handleScore(PongEntity p, Ball b) {
        if (b.isImmune()) {
            p.addScore(2);
        } else {
            p.addScore(1);
        }
    }


//    private Ball randomBall() {
//        Random r = new Random();
//        float velMultiplier = 100.f;
//        return new Ball(
//                (songMap.getTime() * 50) % game.getWidth(),
//                (int) songMap.getTime() % 2 == 1 ?
//                        0 + player.getPaddle().getHeight()
//                        : game.getHeight() - 0 - ai.getPaddle().getHeight(), // TODO set const
//                // for 50
//                16.f, // TODO const ball radius
//                10 * (r.nextFloat() - 0.5f),
//                (int) songMap.getTime() % 2 == 1 ?
//                        velMultiplier * (r.nextFloat()) + 150.f
//                        : velMultiplier * (-r.nextFloat()) - 150.f,
//                50.f * (r.nextFloat() - 0.5f),
//                0);
//    }


    /* CONTROL METHODS */

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown (int keycode) {
                if (keycode == Input.Keys.P) {
                    if (running)
                        pause();
                    else
                        resume();
                }
                if (keycode == Input.Keys.R) {
                    dispose();
                    game.songSelect();
                }
                if (keycode == Input.Keys.ESCAPE) {
                    dispose();
                    game.menu();
                }
                return true;
            }

        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        songMap.pause();
        running = false;
    }

    @Override
    public void resume() {
        songMap.resume();
        running = true;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        songMap.dispose();
        scoreLabel.dispose();
        batch.dispose();
        shape.dispose();
    }
}
