package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public class PongAI extends PongEntity {

    public float maxSpeed; // how fast this boi move side 2 side like a boss
    public int maxError; // +- exact x position

    Ball target;

    public PongAI(final Paddle paddle) {
        super(paddle);
        this.maxSpeed = 60.f;
        this.maxError = ((int) paddle.getHalfWidth() / 2);
    }

    public void update(List<Ball> balls) {
        predict(balls);
        paddle.update();
    }

    private float scaleSpeed(float distanceX) {
        return maxSpeed * (distanceX / (Gdx.graphics.getWidth() - paddle.getHalfWidth()));
    }

    private void predict(List<Ball> balls) {
        if (!balls.isEmpty()) {
            Ball.sortByClosestYVelocity(paddle.y, balls);
            // FACING PADDLE
            target = selectTargetBall(balls);
            if (target != null) {
                float paddleYAim = paddle.y - paddle.getHalfHeight();
                Ball sim = aimAtBall(target, paddleYAim);
                float deltaX = sim.getX() - paddle.getX();
                float distanceX = Math.abs(deltaX);
                float lenience = maxError;
                if (distanceX > lenience) {
                    if (deltaX > 0) {
                        paddle.setVelX(scaleSpeed(distanceX));
                    } else {
                        paddle.setVelX(-scaleSpeed(distanceX));
                    }
                } else {
                    paddle.setVelX(0.f);
                }
            }
        }
    }

    private Ball aimAtBall(Ball target, float paddleYAim) {
        Ball sim = new Ball(target);
        do {
            Ball.simulateBallUpdate(sim, 10);
        } while (!(sim.getY() >= paddleYAim) && target.getVelY() > 0);
        return sim;
    }

    private Ball selectTargetBall(List<Ball> balls) {
        for (Ball ball : balls) {
            if (ball.getVelY() > 0) {
                if (ball.y <= paddle.y)
                    return ball;
            }
        }
        return null;
    }


    public void draw(ShapeRenderer shape) {
        paddle.draw(shape);
    }
}
