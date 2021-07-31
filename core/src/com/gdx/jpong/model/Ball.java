package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import org.jetbrains.annotations.NotNull;

public class Ball extends GameObject {

    private Sound hitSound;

    private float radius;

    private float velX;
    private float velY;

    private Color color;

    public Ball(float x, float y, float radius, float velX, float velY) {
        super(x, y);
        this.radius = radius;
        this.velX = velX;
        this.velY = velY;
        this.color = Color.WHITE;
        hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/soft-hitclap.wav"));
    }

    //@Override
    public void update(Paddle paddle) {
        x += velX;
        y += velY;
        handlePaddleCollision(paddle);
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
            hitSound.play(0.5f);
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

    /**
     * @param paddle paddle to check collision in y direction with
     * @return amount collided by (intersect), > 0 if collision
     */
    private float checkCollisionY(Paddle paddle) {
        return 0; // TODO
    }

    private float checkCollisionX(Paddle paddle) {
        return 0; // TODO
    }

    private void handleBorderCollision() { // TODO collide only on incoming side
        if (x - radius < 0 || x + radius > Gdx.graphics.getWidth()) {
            hitSound.play(0.5f);
            velX *= -1;
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
}
