package com.gdx.jpong.model;

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

    public void update() {
        // nothin'
    }
}
