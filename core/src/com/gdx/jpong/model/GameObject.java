package com.gdx.jpong.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameObject {
    protected float x;
    protected float y;

    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(ShapeRenderer shape);

    public void update() {
        // TODO
    }
}
