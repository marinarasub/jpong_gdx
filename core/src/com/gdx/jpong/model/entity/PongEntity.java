package com.gdx.jpong.model.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.Paddle;

/**
 * a guy with a paddle.
 */
public abstract class PongEntity {

    protected Paddle paddle;

    private int score;

    public boolean addScore(int amt) {
        if (amt > 0) {
            score += amt;
            return true;
        }
        return false;
    }

    public int getScore() {
        return score;
    }

    public PongEntity(final Paddle paddle) {
        this.paddle = paddle;
        this.score = 0;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddleColor(Color color) {
        paddle.setColor(color);
    }

    public void update(float deltaTime) {
        // nothin'
    }

    public void draw(ShapeRenderer shape) {
        paddle.draw(shape);
    }
}
