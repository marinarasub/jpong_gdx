package com.gdx.jpong.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.Paddle;

/**
 * handles input and stuff.
 */
public class PongPlayer extends PongEntity {

    public PongPlayer(final Paddle paddle) {
        super(paddle);
    }

    @Override
    public void update(float deltaTime) {
        paddle.setX(Gdx.input.getX());
        //paddle.setY(Gdx.graphics.getHeight() - Gdx.input.getY());
        paddle.update(deltaTime);
    }
}
