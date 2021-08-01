package com.gdx.jpong.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

    protected Vector2 position;
    protected Vector2 velocity;

    public GameObject(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
    }

    public GameObject(float x, float y, float velX, float velY) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(velX, velY);
    }
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setX(float x) {
        this.position.x = x;
    }

    public void setY(float y) {
        this.position.y = y;
    }

    public void setVelX(float velX) {
        this.velocity.x = velX;
    }

    public void setVelY(float velY) {
        this.velocity.y = velY;
    }

    public void translate(float x, float y) {
        this.position.add(x, y);
    }

    public float getVelX() {
        return velocity.x;
    }

    public float getVelY() {
        return velocity.y;
    }

    public void flipVelX() {
        velocity.x = -velocity.x;
    }

    public void flipVelY() {
        velocity.y = -velocity.y;
    }

    public float scaleVelX(float deltaTime) {
        return velocity.x * deltaTime;
    }

    public float scaleVelY(float deltaTime) {
        return velocity.y * deltaTime;
    }

    public abstract void draw(ShapeRenderer shape);

    public void update(float deltaTime) {
        position.add(scaleVelX(deltaTime), scaleVelY(deltaTime));
    }
}
