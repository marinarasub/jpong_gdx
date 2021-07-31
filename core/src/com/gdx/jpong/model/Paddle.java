package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle extends GameObject {

    static final int PADDLE_HEIGHT = 20;

    private float width;
    private float height;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getHalfWidth() {
        return width / 2.f;
    }

    public float getHalfHeight() {
        return height / 2.f;
    }

    public Paddle(float width, float y) {
        super((float) Gdx.graphics.getWidth() / 2.f, y);
        this.width = width;
        this.height = PADDLE_HEIGHT;
    }

    @Override
    public void update() {
        super.update();
        x = Math.min(Gdx.graphics.getWidth() - getHalfWidth(), Math.max(getHalfWidth(), x));
        y = Math.min(Gdx.graphics.getHeight() - getHalfHeight() , Math.max(getHalfHeight(), y));
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.rect(x - getHalfWidth(), y - getHalfHeight(), width, height);
    }
}
