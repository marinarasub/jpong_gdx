package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jpong.ui.PongGame;

import java.util.*;

/**
 * Represents a Song Map with ball spawns set for a time value from offset.
 * @see com.gdx.jpong.ui.screen.PlayScreen
 */
public class SongMap {

    private Clock timer;

    // INFO
    private Music music;
    //private String title;
    //private String artist;

    // DIFFICULTY
    private float sizeMultiplier; // 0 - 1
    private float difficulty; // 1 to 10 process in PlayScreen
    private float velocityMultiplier;

    // TIMING
    private float startOffset = 0;
    private boolean start = false;

    // SPAWNS
    private TreeMap<Float, List<Ball>> spawns; // KEY: spawn time (after offset), VALUE: List of balls to be spawned.

    // VISUAL
    private Texture background;
    private float backgroundDim = 0;
    private Vector2 playSize;

    // SIGNAL
    private boolean waitingSpawn; // waiting to spawn

    public SongMap(TreeMap<Float, List<Ball>> rawSpawns,
                   Music music, Texture bg, Vector2 playSize,
                   float offset, float dim, float ballSize, float velMult, float difficulty) {
        this.playSize = playSize;
        this.sizeMultiplier = ballSize;
        this.music = music;
        this.background = bg;
        this.startOffset = offset;
        this.backgroundDim = dim;
        this.velocityMultiplier = velMult;
        this.difficulty = difficulty;

        this.spawns = rawSpawns;
        processSpawns(rawSpawns);
        this.timer = new Clock();
    }

    private void processSpawns(TreeMap<Float, List<Ball>> rawSpawns) {
        for (Float time : rawSpawns.keySet()) {
            List<Ball> balls = rawSpawns.get(time);
            for (Ball b : balls) {
                float w = playSize.x;
                float h = playSize.y;
                float r = clamp(b.getRadius() * sizeMultiplier, 1,w / 2);
                // SCALE BY WIDTH - HEIGHT
                b.setRadius(r * sizeMultiplier);
                b.setX(clamp(b.getX(), 0, 1) * (w - 2*r) + r);
                int side = Math.round(b.getY()); // 1 is top (ai side) 0 is bot (player)
                b.setY(side == 0 ? -r : h+r); // TODO
                b.setVelX(clamp(b.getVelX() * velocityMultiplier / 1000, -1,  1) * w); // 1000 is one w/h per second
                b.setVelY(clamp(b.getVelY() * velocityMultiplier / 1000, -1, 1) * h);
                b.setAccX(clamp(b.getAccX() / 1000, -0.5f, 0.5f) * w);
                b.setAccY(clamp(b.getAccY() / 1000, -0.1f, 0.1f) * h);
                //b.setColor(); TODO add color to json
            }
        }
    }

    // clamp x to -max, max
    private float clamp(float x, float min, float max) {
        return Math.min(max, Math.max(min, x));
    }

    public String getClockTime() {
        return timer.getDisplayTime();
    }

    public void setBackgroundDim(float backgroundDim) {
        if (backgroundDim <= 1) this.backgroundDim = backgroundDim;
    }

    public float getBackgroundDim() {
        return backgroundDim;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setBackground(Texture image) {
        this.background = image;
    }

    public Texture getBackground() {
        return background;
    }

    public boolean isWaitingSpawn() {
        return waitingSpawn;
    }

    public Float getSpawnTime() {
        return spawns.firstKey();
    }

    public List<Ball> getSpawns(Float time) {
        waitingSpawn = false;
        music.play();
        return spawns.get(time);
    }

    public void removeAlreadySpawned(Float time) {
        spawns.remove(time);
    }

    public void start() {
        music.setPosition(0);
        timer.reset();
        music.play();
    }

    public void update(float delta) {
        timer.update(music.getPosition());
        if (!start) {
            if (music.getPosition() >= startOffset) {
                start = true;
            } else return;
        }
        if (!spawns.isEmpty()) {
            Float time = spawns.firstKey();
            if (getTime() >= time + startOffset) {
                waitingSpawn = true;
                music.pause();
            }
        }
    }

    public float getTime() {
        return music.getPosition();
    }
    public void pause() {
        music.pause();
    }

    public void resume() {
        music.play();
    }

    public void setOnCompletionListener(Music.OnCompletionListener listener) {
        music.setOnCompletionListener(listener);
    }

    public void dispose() {
        music.dispose();
        if (background != null)
            background.dispose();
    }

}
