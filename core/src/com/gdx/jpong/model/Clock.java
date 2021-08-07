package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;

import java.util.Locale;

public class Clock {

    private float timeElapsed; //

    public Clock() {
        timeElapsed = 0;
    }


    public void reset() {
        timeElapsed = 0;
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

    // set clock to time
    public void update(float time) {
        timeElapsed = time;
    }

}
