package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SongMap {

    static final String AUDIO_PATH = "audio/";

    private Clock timer;

    private Music music;
    private float bpm = 1; // CANNOT BE 0
    private float startOffset = 0;
    private float lastTime;

    private boolean waiting; // waiting to spawn

    public SongMap(String songName) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal(AUDIO_PATH + songName));
        this.timer = new Clock();
    }

    public SongMap(String songName, Clock timer) {
        this.music =  Gdx.audio.newMusic(Gdx.files.internal(AUDIO_PATH + songName));
        this.timer = timer;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void endWaiting() {
        lastTime = timer.getTimeElapsed();
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
        float curTime = timer.getTimeElapsed();
        return curTime - lastTime;
    }

    public void update() {
        if (lastInterval() < startOffset) return;
        if (lastInterval() >= getTimePerBeat()) {
            waiting = true;
            music.pause();
        }
    }


    public void setOnCompletionListener(Music.OnCompletionListener listener) {
        music.setOnCompletionListener(listener);
    }

}
