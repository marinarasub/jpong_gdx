package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.jetbrains.annotations.NotNull;

public class Ball extends GameObject {

    private Sound hitSound;

    private float radius;

    private Color color;

    public Ball(float x, float y, float radius, float velX, float velY) {
        super(x, y, velX, velY);
        this.radius = radius;
        this.color = Color.WHITE;
        hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/soft-hitclap.wav"));
    }

    // copy object
    public Ball (Ball b) {
        super(b.x, b.y, b.velX, b.velY);
        this.radius = b.radius;
        this.color = b.color;
        this.hitSound = b.hitSound;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    //@Override
    public void update(Paddle paddle) {
        x += velX;
        y += velY;
        handlePaddleCollision(paddle);
        handleBorderCollision();
    }

    // no paddle.
    @Override
    public void update() {
        super.update();
        handleBorderCollision();
    }

    // return last y pos, 0 if not
    public float isOutOfYBounds() {
        if (y + radius < 0 || y - radius > Gdx.graphics.getHeight()) {
            return y;
        }
        return 0;
    }

    /**
     * @param paddle paddle to check collision with
     * @return true if collision
     */
    private boolean handlePaddleCollision(Paddle paddle) {
        float halfPaddleHeight = paddle.getHalfHeight();
        float halfPaddleWidth = paddle.getHalfWidth();

        float deltaY = this.y - paddle.y;
        float deltaX = this.x - paddle.x;

        float intersectY = (halfPaddleHeight + this.radius) - Math.abs(deltaY);
        float intersectX = (halfPaddleWidth + this.radius) - Math.abs(deltaX);



        if (intersectX > 0 && intersectY > 0) {
            hitSound.play(0.3f);
            // SIDE COLL TODO holy shit its messy
            if (this.x >= paddle.x - halfPaddleWidth && this.x <= paddle.x + halfPaddleWidth) {
                //X VEL BASED ON dX from PADDLE CENTER
                float totalVelSqr = velX * velX + velY * velY;
                try {
                    float totalVel = (float) Math.sqrt(totalVelSqr);
                    float newVelX = (deltaX / halfPaddleWidth) * 0.8f * totalVel; // 80% is max vel transfer into x dir.
                    float newSpeedY = (float) Math.sqrt(totalVelSqr - newVelX * newVelX);
                    this.velY = deltaY > 0 ? newSpeedY : -newSpeedY;
                    this.velX = newVelX;
                } catch (Exception e) {
                    System.err.println("Probably Math Error");
                    e.printStackTrace();
                }
                if (paddle.y < this.y) {
                    this.y += intersectY;

                } else {
                    this.y -= intersectY;
                }
            } else { //if (this.y >= paddle.y - halfPaddleHeight && this.y <= paddle.y + halfPaddleHeight) {
                this.velX = -this.velX;
                // L OR R
                if (paddle.x < this.x) {
                    this.x += intersectX;
                } else {
                    this.x -= intersectX;
                }
            }
            return true;
        }
        return false;
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
    public static void simulateBallUpdate(Ball ball, int updates) {
        for (int i = 0; i < updates; i++) {
            ball.update();
        }
    }

    private void handleBorderCollision() { // TODO collide only on incoming side
        if (x - radius < 0 || x + radius > Gdx.graphics.getWidth()) {
            //hitSound.play(0.3f);
            velX = -velX;
            if (x - radius < 0) {
                x = 0 + radius;
            } else {
                x = Gdx.graphics.getWidth() - radius;
            }
        }
//        if (y - radius < 0 || y + radius > Gdx.graphics.getHeight()) {
//            velY *= -1;
//        }
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, radius);
    }

    @Override
    public String toString() {
        return "Ball (" + x + ", " + y +")";
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
        float ball1dist = (float) Math.sqrt(ball1.x * ball1.x + ball1.y * ball1.y);
        float ball2dist = (float) Math.sqrt(ball2.x * ball2.x + ball2.y * ball2.y);
        return ball1dist <= ball2dist;
    }

    public static boolean compareCloserBallY(float y, Ball ball1, Ball ball2) {
        float ball1dist = Math.abs(ball1.y - y);
        float ball2dist = Math.abs(ball2.y - y);
        return ball1dist <= ball2dist;
    }

    public static boolean compareCloserBallYVel(float y, Ball ball1, Ball ball2) {
        float ball1z = Math.abs(ball1.y - y) / ball1.getVelY();
        float ball2z= Math.abs(ball2.y - y) / ball2.getVelY();
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
