package com.gdx.jpong.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameObject {
    protected int x;
    protected int y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(ShapeRenderer shape);

    public void update() {
        // TODO
    }
}
