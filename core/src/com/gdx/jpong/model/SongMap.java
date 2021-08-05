package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class SongMap {

    static final String AUDIO_PATH = "audio/";
    private Clock timer;

    private Music music;
    private float bpm = 1; // CANNOT BE 0
    private float startOffset = 0;
    private boolean start = false;
    private float lastTime;

    private Texture background;
    private float backgroundDim = 0.5f;

    private boolean waiting; // waiting to spawn

    public SongMap(String songName) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal(AUDIO_PATH + songName));
        this.timer = new Clock();
    }

    public float getTime() {
        return music.getPosition();
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

    public void setBackground(Texture image) {
        this.background = image;
    }

    public Texture getBackground() {
        return background;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void endWaiting() {
        lastTime = music.getPosition();
        music.play();
        waiting = false;
    }

    public void setBpm(float bpm) {
        if (bpm != 0)
            this.bpm = bpm;
        else
            this.bpm = 1; // DEFAULT
    }


    public void setStartOffset(float startOffset) {
        this.startOffset = startOffset;
    }

    // return time interval per 1/4 note in seconds (4/4)
    private float getTimePerBeat() {
        return 60 / this.bpm;
    }

    public void start() {
        music.play();
    }

    public float lastInterval() {
        float curTime = music.getPosition();
        return curTime - lastTime;
    }

    public void update(float delta) {
        timer.update(music.getPosition());
        if (!start) {
            if (music.getPosition() >= startOffset) {
                start = true;
            } else return;
        }
        if (lastInterval() >= getTimePerBeat()) {
            waiting = true;
            music.pause();
        }
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
