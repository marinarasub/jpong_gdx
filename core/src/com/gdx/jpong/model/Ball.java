package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.*;

public class Ball extends GameObject {

    private Sound hitSound;

    private float radius;
    private boolean immune; // ignore collision to paddles

    private Color color;

    public Ball(float x, float y, float radius, float velX, float velY) {
        super(x, y, velX, velY);
        this.radius = radius;
        this.color = Color.LIGHT_GRAY;
        hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/soft-hitclap.wav"));
        immune = true;
    }

    // copy object
    public Ball (Ball b) {
        super(b.getX(), b.getY(), b.getVelX(), b.getVelY());
        this.radius = b.getRadius();
        this.color = b.getColor();
        this.hitSound = b.hitSound;
    }

    public float getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    //@Override
    public void update(float deltaTime, Paddle paddle) {
        super.update(deltaTime);
        handlePaddleCollision(deltaTime, paddle);
        handleBorderCollision(deltaTime);
    }

    // no paddle.
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        handleBorderCollision(deltaTime);
    }

    // return last y pos, 0 if not
    public float isOutOfYBounds() {
        if (getY() + radius < 0 || getY() - radius > Gdx.graphics.getHeight()) {
            return getY();
        }
        return 0;
    }

    /**
     * @param paddle paddle to check collision with
     * @return true if collision
     */
    private boolean handlePaddleCollision(float deltaTime, Paddle paddle) {
        float halfPaddleHeight = paddle.getHalfHeight();
        float halfPaddleWidth = paddle.getHalfWidth();

        float deltaY = this.getY() - paddle.getY();
        float deltaX = this.getX() - paddle.getX();

        float intersectY = (halfPaddleHeight + this.radius) - Math.abs(deltaY);
        float intersectX = (halfPaddleWidth + this.radius) - Math.abs(deltaX);

        if (intersectX > 0 && intersectY > 0) {
            // SIDE COLL TODO holy shit its messy
            if (this.getX() + this.getRadius() / 2 >= paddle.getX() - halfPaddleWidth
                    && this.getX() - this.getRadius() / 2 <= paddle.getX() + halfPaddleWidth) {
                //X VEL BASED ON dX from PADDLE CENTER
                if (!immune) {
                    hitSound.play(0.3f);
                    calculateNewVelocity(halfPaddleWidth, deltaX, deltaY);
                    if (paddle.getY() < this.getY()) {
                        this.translate(0, intersectY);
                    } else {
                        this.translate(0, -intersectY);
                    }
                } else {
                    handleImmunity(paddle);
                }
            } else { //if (this.y >= paddle.y - halfPaddleHeight && this.y <= paddle.y + halfPaddleHeight) {
                if (!immune) {
                    hitSound.play(0.3f);
                    flipVelX();
                    // L OR R
                    if (paddle.getX() < this.getX()) {
                        this.translate(intersectX, 0);
                    } else {
                        this.translate(-intersectX, 0);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void handleImmunity(Paddle paddle) {
        if (this.getVelY() > 0) {
            if (this.getY() > Gdx.graphics.getWidth() / 2) {
                removeImmunity();
            }
        } else {
            if (this.getY() < Gdx.graphics.getWidth() / 2)
                removeImmunity();
        }
    }

    private void removeImmunity() {
        immune = false;
        this.color = Color.WHITE;
    }

    private void calculateNewVelocity(float halfPaddleWidth, float deltaX, float deltaY) {
        float totalVel = velocity.len();
        float newVelX = (deltaX / halfPaddleWidth) * 0.8f * totalVel;
        // TODO const 80% is max vel transfer into x dir.
        float newSpeedY = (float) Math.sqrt(velocity.len2()- newVelX * newVelX);
        this.setVelY(deltaY > 0 ? newSpeedY : -newSpeedY);
        this.setVelX(newVelX);
    }

//    /**
//     * @param paddle paddle to check collision in y direction with
//     * @return amount collided by (intersect), > 0 if collision
//     */
//    private float checkCollisionY(Paddle paddle) {
//        return 0; // TODO
//    }
//
//    private float checkCollisionX(Paddle paddle) {
//        return 0; // TODO
//    }

    // Returns new ball (not orignial after n updates) ghost update)
    public static void simulateBallUpdate(float deltaTime, Ball ball, int updates) {
        if (updates < 1) updates = 1;
        for (int i = 0; i < updates; i++) {
            ball.update(deltaTime);
        }
    }

    private void handleBorderCollision(float deltaTime) { // TODO collide only on incoming side
        if (getX() - radius < 0 || getX() + radius > Gdx.graphics.getWidth()) {
            //hitSound.play(0.3f);
            setVelX(-getVelX());
            if (getX() - radius < 0) {
                setX(0 + radius);
            } else {
                setX(Gdx.graphics.getWidth() - getRadius());
            }
        }
//        if (y - radius < 0 || y + radius > Gdx.graphics.getHeight()) {
//            velY *= -1;
//        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(getColor());
        shape.circle(getX(), getY(), getRadius());
    }

    @Override
    public String toString() {
        return "Ball @ (" + getX() + ", " + getY() +")";
    }


    // return new sorted list of balls order by closest euclidian distance to pos x, y
    // uses java's list sort for now
    public static void sortByClosest(float x, float y, List<Ball> balls) {
        while (true) {
            boolean sorted = true;
            for (int i = 1; i < balls.size()-1; i++) {
                Ball b1 = balls.get(i-1);
                Ball b2 = balls.get(i);
                if (!compareCloserBallY(x, y, b1, b2)) {
                    Collections.swap(balls, i, i-1);
                    sorted = false;
                }
            }
            if (sorted) break;
        }
    }

    public static void sortByClosestY(float y, List<Ball> balls) {
        while (true) {
            boolean sorted = true;
            for (int i = 1; i < balls.size()-1; i++) {
                Ball b1 = balls.get(i-1);
                Ball b2 = balls.get(i);
                if (!compareCloserBallY(y, b1, b2)) {
                    Collections.swap(balls, i, i-1);
                    sorted = false;
                }
            }
            if (sorted) break;
        }
    }

    public static void sortByClosestYVelocity(float y, List<Ball> balls) {
        while (true) {
            boolean sorted = true;
            for (int i = 1; i < balls.size()-1; i++) {
                Ball b1 = balls.get(i-1);
                Ball b2 = balls.get(i);
                if (!compareCloserBallYVel(y, b1, b2)) {
                    Collections.swap(balls, i, i-1);
                    sorted = false;
                }
            }
            if (sorted) break;
        }
    }

    // return true if 1 is closer or equal, pythag.
    public static boolean compareCloserBallY(float x, float y, Ball ball1, Ball ball2) {
        float ball1dist = ball1.velocity.len();
        float ball2dist = ball2.velocity.len();
        return ball1dist <= ball2dist;
    }

    public static boolean compareCloserBallY(float y, Ball ball1, Ball ball2) {
        float ball1dist = Math.abs(ball1.getY() - y);
        float ball2dist = Math.abs(ball2.getY() - y);
        return ball1dist <= ball2dist;
    }

    public static boolean compareCloserBallYVel(float y, Ball ball1, Ball ball2) {
        float ball1z = Math.abs(ball1.getY() - y) / ball1.getVelY();
        float ball2z= Math.abs(ball2.getY() - y) / ball2.getVelY();
        return ball1z <= ball2z;
    }

    public static boolean verifySort(float x, float y,List<Ball> balls) {
        for (int i = 0; i < balls.size()-2; i++) {
            if(!compareCloserBallY(x, y, balls.get(i), balls.get(i+1)))
                return false;
        }
        return true;
    }

}
