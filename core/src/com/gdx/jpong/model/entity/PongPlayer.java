package com.gdx.jpong.model.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.Paddle;
import com.gdx.jpong.model.entity.PongEntity;

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
        //paddle.getY() = Gdx.graphics.getHeight() - Gdx.input.getY();
        paddle.update(deltaTime);
    }

    public void draw(ShapeRenderer shape) {
        paddle.draw(shape);
    }
}
