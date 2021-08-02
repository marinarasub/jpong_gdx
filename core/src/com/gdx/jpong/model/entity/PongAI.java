package com.gdx.jpong.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.Ball;
import com.gdx.jpong.model.Paddle;

import java.util.List;

public class PongAI extends PongEntity {

    private float maxSpeed; // how fast this boi move side 2 side like a boss
    private int maxError; // +- exact x position
    private int updatePrecison; // how many updates/step to check REQUIRES > 0
    private float reaction;

    Ball target;

    public PongAI(final Paddle paddle) {
        super(paddle);
        this.maxSpeed = 5500.f;
        this.maxError = ((int) paddle.getHalfWidth() / 2);
        this.reaction = 1.1f;
        this.updatePrecison = 60;
    }

    public int getUpdatePrecison() {
        return Gdx.graphics.getFramesPerSecond() / updatePrecison;
    }

    public void update(float deltaTime, List<Ball> balls) {
        predict(deltaTime, balls);
        paddle.update(deltaTime);
    }

    private float scaleSpeed(float distanceX) {
        return (float) (maxSpeed * Math.pow(distanceX / (Gdx.graphics.getWidth() - paddle.getHalfWidth()), reaction));
    }

    private void predict(float deltaTime, List<Ball> inList) {
        if (!inList.isEmpty()) {
            List<Ball> balls = Ball.sortByClosestYVelocity(paddle.getY(), inList);
            // FACING PADDLE
            target = selectTargetBall(balls);
            if (target != null) {
                float paddleYAim = paddle.getY() - paddle.getHalfHeight();
                Ball sim = aimAtBall(deltaTime, target, paddleYAim);
                float deltaX = sim.getX() - paddle.getX();
                trackX(deltaX, (float) (maxError * Math.random()));
            } else {
                trackX(Gdx.graphics.getWidth() / 2 - paddle.getX() - paddle.getHalfWidth(), maxError);
            }
        }
    }

    private void trackX(float deltaX, float lenience) {
        float distanceX = Math.abs(deltaX);
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

    private Ball aimAtBall(float deltaTime, Ball target, float yAim) {
        Ball sim = new Ball(target);
        do {
            Ball.simulateBallUpdate(deltaTime, sim, getUpdatePrecison());
        } while (!(sim.getY() >= yAim) && target.getVelY() > 0);
        return sim;
    }

    private Ball selectTargetBall(List<Ball> balls) {
        for (Ball ball : balls) {
            if (ball.getVelY() > 0) {
                if (ball.getY() <= paddle.getY())
                    return ball;
            }
        }
        return null;
    }


}
