package com.gdx.jpong.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * handles input and stuff.
 */
public class PongPlayer extends PongEntity {

    public PongPlayer(final Paddle paddle) {
        super(paddle);
    }

    @Override
    public void update() {
        paddle.x = Gdx.input.getX();
        //paddle.y = Gdx.graphics.getHeight() - Gdx.input.getY();
        paddle.update();
    }

    public void draw(ShapeRenderer shape) {
        paddle.draw(shape);
    }
}
