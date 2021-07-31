package com.gdx.jpong.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameObject {
    protected float x;
    protected float y;

    protected float velX;
    protected float velY;

    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
        this.velX = 0;
        this.velY = 0;
    }

    public GameObject(float x, float y, float velX, float velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public abstract void draw(ShapeRenderer shape);

    public void update() {
        x += velX;
        y += velY;
    }
}
