package com.gdx.test;

import com.gdx.jpong.model.Ball;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    List<Ball> balls;

    @BeforeEach
    void setUp() {
        balls = new LinkedList<>();
    }

    @Test
    void isOutOfYBounds() {
    }

    @Test
    void testToString() {
    }

    @Test
    void testCompareCloserBallY() {
        Ball b1 = new Ball(0, 1, 15.f, 0, 0);
        Ball b2 = new Ball(0, 2, 15.f, 0, 0);
        assertTrue(Ball.compareCloserBallY(0, 0, b1, b2));
        assertFalse(Ball.compareCloserBallY(0, 0, b2, b1));
    }

    @Test
    void testCompareCloserBallX() {
        Ball b1 = new Ball(1, 0, 15.f, 0, 0);
        Ball b2 = new Ball(2, 0, 15.f, 0, 0);
        assertTrue(Ball.compareCloserBallY(0, 0, b1, b2));
        assertFalse(Ball.compareCloserBallY(0, 0, b2, b1));
    }

    @Test
    void testCompareCloserBall() {
        Ball b1 = new Ball(1, 2, 15.f, 0, 0);
        Ball b2 = new Ball(3, 0, 15.f, 0, 0);
        Ball b3 = new Ball(0, -2, 15.f, 0, 0);
        assertTrue(Ball.compareCloserBallY(0, 0, b3, b2));
        assertTrue(Ball.compareCloserBallY(0, 0, b1, b2));
        assertFalse(Ball.compareCloserBallY(0, 0, b2, b1));
    }

    @Test
    void testVerifySort() {
        balls.add(new Ball(0, 0, 15.f, 0, 0));
        balls.add(new Ball(0, 1, 15.f, 0, 0));
        balls.add(new Ball(0, 2, 15.f, 0, 0));
        balls.add(new Ball(1, 2, 15.f, 0, 0));
        balls.add(new Ball(3, 0, 15.f, 0, 0));
        balls.add(new Ball(0, 3, 15.f, 0, 0));

        assertTrue(Ball.verifySort(0, 0, balls));
    }

    @Test
    void sortByClosestY() {
        balls.add(new Ball(0, 2, 15.f, 0, 0));
        balls.add(new Ball(2, 4, 15.f, 0, 0));
        balls.add(new Ball(-4, 1, 15.f, 0, 0));
        balls.add(new Ball(4, -9, 15.f, 0, 0));
        balls.add(new Ball(0, 0, 15.f, 0, 0));
        balls.add(new Ball(0, 1, 15.f, 0, 0));
        assertFalse(balls.isEmpty());
        Ball.sortByClosest(0, 0, balls);

        Ball.verifySort(0, 0, balls);
    }
}