package com.gdx.jpong.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PongAI extends PongEntity {

    public PongAI(final Paddle paddle) {
        super(paddle);
    }

    @Override
    public void update() {
        paddle.update();
    }

    public void draw(ShapeRenderer shape) {
        paddle.draw(shape);
    }
}
