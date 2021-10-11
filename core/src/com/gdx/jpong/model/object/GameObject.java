package com.gdx.jpong.model.object;

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

    public void setAccX(float accX) {
        this.acceleration.x = accX;
    }

    public void setAccY(float accY) {
        this.acceleration.y = accY;
    }

    public float getVelX() {
        return velocity.x;
    }

    public float getVelY() {
        return velocity.y;
    }

    public float getAccX() {
        return acceleration.x;
    }

    public float getAccY() {
        return acceleration.y;
    }

    public void reflectVelX() {
        velocity.x = -velocity.x;
    }

    public void reflectVelY() {
        velocity.y = -velocity.y;
    }

    public void translate(float x, float y) {
        this.position.add(x, y);
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

    /**
     * Checks if current game object is equivalent with another object
     * @return true if all fields are equal in value
     *         false otherwise
     * ! Note that game objects can only be equal by reference !
     */
    public boolean equivalent(Object o) {
        if (!(o instanceof  GameObject)) return false;
        GameObject g = (GameObject) o;
        return position.equals(g.position) &&
                velocity.equals(g.velocity) &&
                acceleration.equals(g.acceleration);
    }
}
