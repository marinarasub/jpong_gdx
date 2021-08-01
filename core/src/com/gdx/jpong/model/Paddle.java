package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle extends GameObject {

    static final int PADDLE_HEIGHT = 20;

    private float width;
    private float height;

    private Color color;

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
        this.color = Color.WHITE; // DEF
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setX(Math.min(Gdx.graphics.getWidth() - getHalfWidth(), Math.max(getHalfWidth(), getX())));
        setY(Math.min(Gdx.graphics.getHeight() - getHalfHeight() , Math.max(getHalfHeight(), getY())));
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(this.getColor());
        shape.rect(getX() - getHalfWidth(), getY() - getHalfHeight(), width, height);
    }

}
