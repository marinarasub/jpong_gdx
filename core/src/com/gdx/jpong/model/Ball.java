package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import org.jetbrains.annotations.NotNull;

public class Ball extends GameObject {

    private int radius;

    private int velX;
    private int velY;

    private Color color;

    public Ball(int x, int y, int radius, int velX, int velY) {
        super(x, y);
        this.radius = radius;
        this.velX = velX;
        this.velY = velY;
        this.color = Color.WHITE;
    }

    //@Override
    public void update(Paddle paddle) {
        x += velX;
        y += velY;
        handlePaddleCollision(paddle);
        handleBorderCollision();

    }

    private void handlePaddleCollision(Paddle paddle) {
        if (collidesWithPaddle(paddle)) {
            color = Color.GREEN;
            velY *= -1;
        }
        else
            color = Color.WHITE;

    }

    private boolean collidesWithPaddle(Paddle paddle) {
        return collidesWithPaddleX(paddle) && collidesWithPaddleY(paddle);
    }

    private boolean collidesWithPaddleY(Paddle paddle) {
        float halfPaddleHeight = (float) Paddle.PADDLE_HEIGHT / 2;
        return this.y + this.radius > paddle.y - halfPaddleHeight && this.y - this.radius < paddle.y + halfPaddleHeight;
    }

    private boolean collidesWithPaddleX(Paddle paddle) {
        float halfPaddleWidth = (float) paddle.getWidth() / 2;
        return this.x + this.radius > paddle.x - halfPaddleWidth && this.x - this.radius < paddle.x + halfPaddleWidth;
    }

    private void handleBorderCollision() {
        if (x - radius < 0 || x + radius > Gdx.graphics.getWidth())
            velX *= -1;
        if (y - radius < 0 || y + radius > Gdx.graphics.getHeight())
            velY *= -1;
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y + radius / 2, radius);
    }
}
