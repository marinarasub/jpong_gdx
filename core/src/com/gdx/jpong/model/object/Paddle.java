package com.gdx.jpong.model.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle extends GameObject {

    static final int PADDLE_HEIGHT = 20;
    static final Texture texture = new Texture(Gdx.files.internal("textures/paddle.png"));

    private SpriteBatch batch;
    private Sprite sprite;

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
        this.sprite = new Sprite(texture);
        this.batch = new SpriteBatch();
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
//        shape.setColor(this.getColor());
//        shape.rect(getX() - getHalfWidth(), getY() - getHalfHeight(), width, height);
        batch.begin();
        sprite.setSize(width, height);
        sprite.setPosition(getX() - getHalfWidth(), getY() - getHalfHeight());
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public String toString() {
        return "Paddle @ (" + this.getX() + ", " + this.getY() + ")";
    }

}
