package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;

import java.util.Locale;

public class Clock {

    private float timeElapsed; //
    private float markedTime; // To calc delta between two points

    private boolean paused;

    public Clock() {
        timeElapsed = 0;
        markedTime = 0;
    }

    public void setMarkedTime() {
        markedTime = timeElapsed;
    }

    public float getIntervalElapsed() {
        return timeElapsed - markedTime;
    }

    public void reset() {
        timeElapsed = 0;
    }

    public float getDeltaTime() {
        return Gdx.graphics.getDeltaTime();
    }

    public float getTimeElapsed() {
        return timeElapsed;
    }

    public int getTimeElapsedSeconds() {
        return (int) timeElapsed;
    }

    public int getTimeElapsedMinutes() {
        return ((int) timeElapsed / 60);
    }

    public String getDisplayTime() {
        if (getTimeElapsedMinutes() == 0)
            return (String.format(Locale.US, "%4.2f", getTimeElapsed() % 60));
        else
            return getTimeElapsedMinutes() + ":" + String.format(Locale.US, "%05.2f", getTimeElapsed() % 60);
    }

    // REQUIRES: called every render update in main game loop
    public void update() {
        if (!paused) {
            timeElapsed += getDeltaTime();
        }
    }

    public void pause() {
        paused = true;
    }

}
