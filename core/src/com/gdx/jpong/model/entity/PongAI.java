package com.gdx.jpong.model.entity;

import com.badlogic.gdx.Gdx;
import com.gdx.jpong.model.Ball;
import com.gdx.jpong.model.Paddle;

import java.util.List;

public class PongAI extends PongEntity {

    private float maxSpeed = 5000.f; // how fast this boi move side 2 side like a boss
    private float maxError; // +- exact x position
    private int updatePrecison; // how many updates/step to check REQUIRES > 0
    private float reaction = 1.f;

    Ball target;

    public PongAI(final Paddle paddle, float maxSpeed, float reaction) {
        super(paddle);
        this.maxSpeed = maxSpeed; // default 5000
        this.maxError = paddle.getHalfWidth();
        this.reaction = reaction; // default 1;
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
        return (float) (maxSpeed * Math.pow(distanceX / (Gdx.graphics.getWidth() - paddle.getWidth()), reaction));
    }

    private void predict(float deltaTime, List<Ball> inList) {
        if (!inList.isEmpty()) {
            float paddleYAim = paddle.getY() - paddle.getHalfHeight();
            List<Ball> balls = Ball.sortByClosestVelY(paddleYAim, inList);
            // FACING PADDLE
            target = selectTargetBall(balls);
            if (target != null) {
                Ball sim = aimAtBall(deltaTime, target, paddleYAim);
                float deltaX = sim.getX() - paddle.getX();
                trackX(deltaX, (float) (maxError * Math.random() + 0.5f));
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
                return ball;
            }
        }
        return null;
    }


}
