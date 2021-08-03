package com.gdx.jpong.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 acceleration;

    public GameObject(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);
    }

    public GameObject(float x, float y, float velX, float velY) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(velX, velY);
        this.acceleration = new Vector2(0, 0);
    }

    public GameObject(float x, float y, float velX, float velY, float accX, float accY) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(velX, velY);
        this.acceleration = new Vector2(accX, accY);
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

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
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

    public void reflectVelX() {
        velocity.x = -velocity.x;
    }

    public void reflectVelY() {
        velocity.y = -velocity.y;
    }

    public float scaleVelX(float deltaTime) {
        return velocity.x * deltaTime;
    }

    public float scaleVelY(float deltaTime) {
        return velocity.y * deltaTime;
    }

    public float scaleAccX(float deltaTime) {
        return acceleration.x * deltaTime;
    }

    public float scaleAccY(float deltaTime) {
        return acceleration.y * deltaTime;
    }

    public abstract void draw(ShapeRenderer shape);

    public void update(float deltaTime) {
        position.add(scaleVelX(deltaTime), scaleVelY(deltaTime));
        velocity.add(scaleAccX(deltaTime), scaleAccY(deltaTime));
    }
}
