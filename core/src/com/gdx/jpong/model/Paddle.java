package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle extends GameObject {

    static final int DEFAULT_Y = 25;
    static final int PADDLE_HEIGHT = 10;

    private int width;

    public int getWidth() {
        return width;
    }

    public Paddle(int width) {
        super(Gdx.graphics.getWidth() / 2, DEFAULT_Y);
        this.width = width;
    }

    @Override
    public void update() {
        x = Gdx.input.getX();
        y = Gdx.graphics.getHeight() - Gdx.input.getY();
    }

    @Override
    public void draw(ShapeRenderer shape) {
        float halfWidth = (float) width / 2;
        shape.rect(x - halfWidth, y, width, PADDLE_HEIGHT);
    }
}
