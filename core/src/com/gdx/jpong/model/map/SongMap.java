package com.gdx.jpong.model.map;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jpong.exception.IllegalValueException;
import com.gdx.jpong.model.object.Ball;
import com.gdx.jpong.model.Clock;

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

    public SongMap(TreeMap<Float, List<Ball>> rawSpawns, Music music, Vector2 playSize, float offset) {
        this.playSize = playSize;
        this.music = music;
        this.startOffset = offset;
        setBackgroundDim(1.f);
        setMods(1.f, 1.f, 5.f);
        this.spawns = processSpawns(rawSpawns);
        this.timer = new Clock();
    }

    /**
     * Sets the desired modifiers for gameplay
     * @param sizeMultiplier multiply raw size (radius) of spawns by this number
     * @param velocityMultiplier multiply raw velocities of balls by this number
     * @param difficulty AI difficulty, 1 - 10
     */
    public void setMods(float sizeMultiplier, float velocityMultiplier, float difficulty) {
        this.sizeMultiplier = sizeMultiplier;
        this.velocityMultiplier = velocityMultiplier;
        this.difficulty = difficulty;
    }

    /**
     * @param rawSpawns spawns specified without processing, i.e. considering play size, mods etc.
     * @return processed spawns for gameplay. Will be same reference as passed parameter
     */
    private TreeMap<Float, List<Ball>> processSpawns(TreeMap<Float, List<Ball>> rawSpawns) {
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
                b.setVelY(clamp(b.getVelY() * velocityMultiplier / 1000, -1, 1) * h); // TODO scale by w/h or no?
                b.setAccX(clamp(b.getAccX() / 1000, -0.5f, 0.5f) * w);
                b.setAccY(clamp(b.getAccY() / 1000, -0.1f, 0.1f) * h);
                //b.setColor(); TODO add color to json
            }
        }
        return rawSpawns;
    }

    // clamp x to -max, max
    private float clamp(float x, float min, float max) {
        return Math.min(max, Math.max(min, x));
    }

    public String getClockTime() {
        return timer.getDisplayTime();
    }

    public void setBackgroundDim(float backgroundDim) {
        if (backgroundDim <= 1 && backgroundDim >= 0) this.backgroundDim = backgroundDim;
        else throw new IllegalValueException("Background dim must be [0, 1]");
    }

    public float getBackgroundDim() {
        return backgroundDim;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public Music getMusic() {
        return this.music;
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

    /**
     * updates clock of song map and determines if there is a spawn available at current time, signals if yes
     * @param delta delta time
     */
    public void update(float delta) {
        timer.update(music.getPosition());
        if (!start) {
            if (music.getPosition() >= startOffset) {
                start = true;
            } else return;
        }
        if (!spawns.isEmpty()) {
            if (isOvertime()) {
                waitingSpawn = true;
                music.pause();
            }
        }
    }

    public boolean isOvertime() {
        if (!spawns.isEmpty()) {
            Float time = spawns.firstKey();
            return getTime() >= time + startOffset;
        }
        return false;
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
