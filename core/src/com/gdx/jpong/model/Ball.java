package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Ball extends GameObject {

    static final String SOUND = "sounds/soft-hitclap.wav";
    static final Sound hitSound = Gdx.audio.newSound(Gdx.files.internal(SOUND));
    static final Texture texture = new Texture(Gdx.files.internal("textures/ball.png"));

    private SpriteBatch batch;
    private Sprite sprite;
    private float radius;
    private boolean immune = true; // ignore collision to paddles

    private Color color;

    public Ball(float x, float y, float radius, float velX, float velY) {
        super(x, y, velX, velY);
        this.radius = radius;
        this.color = Color.PINK;
        this.batch = new SpriteBatch();
        this.sprite = new Sprite(texture);
        sprite.setSize(radius * 2, radius * 2);
    }

    public Ball(float x, float y, float radius, float velX, float velY, float accX, float accY) {
        super(x, y, velX, velY, accX, accY);
        this.radius = radius;
        this.color = Color.PINK;
        this.batch = new SpriteBatch();
        this.sprite = new Sprite(texture);
        sprite.setSize(radius * 2, radius * 2);
    }

    // copy object
    public Ball (Ball b) {
        super(b.getX(), b.getY(), b.getVelX(), b.getVelY());
        this.radius = b.getRadius();
        this.color = b.getColor();
        //this.hitSound = b.hitSound;
        this.batch = b.batch;
        this.sprite = b.sprite;
        this.immune = b.isImmune();
    }

    public boolean isImmune() {
        return immune;
    }

    public void setRadius(float r) {
        this.radius = r;
    }

    public float getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
    public boolean isOutOfYBounds(float lower, float upper) {
        return getY() + radius < lower || getY() - radius > upper;
    }

    private boolean isYCollision(Paddle paddle, float lenience) {
        float halfPaddleWidth = paddle.getHalfWidth();
        return getX() + lenience >= paddle.getX() - halfPaddleWidth
                && getX() - lenience <= paddle.getX() + halfPaddleWidth;
    }

    /** TODO clean up
     * @param paddle paddle to check collision with
     * @return true if collision
     */
    private boolean handlePaddleCollision(float deltaTime, Paddle paddle) { // TODO tidy
        float halfPaddleHeight = paddle.getHalfHeight();
        float halfPaddleWidth = paddle.getHalfWidth();

        float deltaY = this.getY() - paddle.getY();
        float deltaX = this.getX() - paddle.getX();

        float intersectY = (halfPaddleHeight + this.radius) - Math.abs(deltaY);
        float intersectX = (halfPaddleWidth + this.radius) - Math.abs(deltaX);

        if (intersectX > 0 && intersectY > 0) {
            float lenience = getRadius() / 2;
            if (isYCollision(paddle, lenience)) {
                //X VEL BASED ON dX from PADDLE CENTER
                if (!immune) {
                    playHitsound();
                    setVelocity(calculateNewVelocity(paddle, deltaX, getVelY()));
                    setYToPaddle(paddle);
                } else {
                    handleImmunity(paddle);
                }
            } else { //if (this.y >= paddle.y - halfPaddleHeight && this.y <= paddle.y + halfPaddleHeight) {
                if (!immune) {
                    playHitsound();
                    reflectVelX();
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

    private void setYToPaddle(Paddle paddle) {
        if (this.getVelY() < 0) {
            this.setY(paddle.getY() - paddle.getHalfHeight() - this.getRadius());
        } else {
            this.setY(paddle.getY() + paddle.getHalfHeight() + this.getRadius());
        }
    }

    private void playHitsound() {
        if (hitSound != null)
            hitSound.play(0.3f);
    }

    private boolean handleImmunity(Paddle paddle) {
        if (this.getVelY() > 0) {
            if (this.getY() > Gdx.graphics.getWidth() / 2) {
                removeImmunity();
                return true;
            }
        } else {
            if (this.getY() < Gdx.graphics.getWidth() / 2)
                removeImmunity();
                return true;
        }
        return false;
    }

    private void removeImmunity() {
        immune = false;
        this.color = Color.WHITE;
    }

    private Vector2 calculateNewVelocity(Paddle p, float deltaX, float velY) {
        float totalVel = velocity.len();
        float newVelX = (deltaX / p.getHalfWidth()) * 0.9f * totalVel; // TODO const 90% is max vel transfer into x dir.
        float newSpeedY = (float) Math.sqrt(velocity.len2()- newVelX * newVelX);

        return new Vector2(newVelX, velY < 0 ? newSpeedY : -newSpeedY);
    }

    // Returns new ball (not orignial after n updates) ghost update)
    public static void simulateBallUpdate(float deltaTime, Ball ball, int updates) {
        if (updates < 1) updates = 1;
        for (int i = 0; i < updates; i++) {
            ball.update(deltaTime);
        }
    }

    private void handleBorderCollision(float deltaTime) { // TODO collide only on incoming side
        float leftX = getX() - getRadius();
        float rightX = getX() + getRadius();
        float addVelX = scaleVelX(deltaTime);
        int wall = 0;
        if (leftX + addVelX <= wall || rightX + addVelX >= Gdx.graphics.getWidth() - wall) {
            //hitSound.play(0.3f);
            setVelX(-getVelX());
            if (leftX + addVelX <= wall) {
                setX(wall + radius);
            } else {
                setX(Gdx.graphics.getWidth() - wall - getRadius());
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
//        batch.begin();
//        sprite.setPosition(getX(), getY());
//        sprite.setOriginCenter();
//        sprite.draw(batch);
//        batch.end();
    }

    /**
     * @return String representation of ball position and velocity
     */
    @Override
    public String toString() {
        return "Ball @ (" + getX() + ", " + getY() +")"
                + " V (" + getVelX() + ", " + getVelY() + ")";
    }


    public static List<Ball> sortByClosestVelY(float y, List<Ball> balls) {
        return mergeSort(y, new LinkedList<>(balls));
    }

    private static List<Ball> mergeSort(float y, List<Ball> balls) {
        if (balls.size() <= 1)
            return balls;
        int midPoint = (balls.size() / 2);
        List<Ball> left = new LinkedList<>(balls.subList(0, midPoint));
        List<Ball> right = new LinkedList<>(balls.subList(midPoint, balls.size()));
        return merge(y, mergeSort(y, left), mergeSort(y, right));
    }

    public static List<Ball> merge(float y, List<Ball> left, List<Ball> right) {
        List<Ball> merge = new LinkedList<>();
        Iterator<Ball> iLeft = left.listIterator();
        Iterator<Ball> iRight = right.listIterator();
        while (iLeft.hasNext() && iRight.hasNext()) {
            Ball bl = iLeft.next();
            Ball br = iRight.next();
            if (compareCloserVelY(y, bl, br)) {
                merge.add(bl);
                iLeft.remove();
            } else {
                merge.add(br);
                iRight.remove();
            }
        }
        iLeft = left.listIterator();
        while (iLeft.hasNext()) {
            Ball b = iLeft.next();
            merge.add(b);
            iLeft.remove();
        }
        iRight = right.listIterator();
        while (iRight.hasNext()) {
            Ball b = iRight.next();
            merge.add(b);
            iRight.remove();
        }
        return merge;
    }

    /**
     * REQUIRES: y velocity is not 0
     * Compare two balls using their velocity and current position relative to a y position
     * @param y y value to compare distance to
     * @param ball1 first ball
     * @param ball2 second ball
     * @return true if ball1 will reach pos (~, y) sooner
     */
    public static boolean compareCloserVelY(float y, Ball ball1, Ball ball2) {
        float ball1Ratio = (ball1.getY() - y) / -ball1.getVelY();
        float ball2Ratio = (ball2.getY() - y) / -ball2.getVelY();
        boolean ball1closer;
        if (ball1Ratio > 0 && ball2Ratio > 0) {
            ball1closer = ball1Ratio <= ball2Ratio;
        } else if (ball1Ratio > 0) {
            ball1closer = true;
        } else {
            ball1closer = ball1Ratio >= ball2Ratio;
        }
        return ball1closer;
    }

    public static boolean verifySortCloseVelY(float y, List<Ball> balls) {
        for (int i = 0; i < balls.size()-2; i++) {
            if(!compareCloserVelY(y, balls.get(i), balls.get(i+1)))
                return false;
        }
        return true;
    }


}
