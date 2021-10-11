package com.gdx.test;

import com.badlogic.gdx.math.Vector2;
import com.gdx.jpong.model.object.Ball;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// NOTE: cannot test hitsound using JUnit, comment out audio related lines in class Ball
class BallTest {

    List<Ball> balls;
    Vector2 origin = new Vector2(0, 0);

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
    void testMergeSort() {
        balls.add(new Ball(-9, 7, 15.f, 0, -2));
        balls.add(new Ball(0, 3, 15.f, 0, -4));
        balls.add(new Ball(2, 2, 15.f, 0, 6));
        balls.add(new Ball(-2, 5, 15.f, 0, 3));
        balls.add(new Ball(4, 1, 15.f, 0, 1));
        balls.add(new Ball(0, 1, 15.f, 0, 0));
        balls.add(new Ball(0, 9, 15.f, 0, 4));

        List<Ball> sorted = Ball.sortByClosestVelY(0, balls);
        sorted.forEach(System.out::println);
        assertTrue(Ball.verifySortCloseVelY(0, sorted));
    }

    @Test
    void testVerifySortByClosestVelY() {
        balls.add(new Ball(0, -5, 15.f, 0, 1));
        balls.add(new Ball(2, -7, 15.f, 0, 1));
        balls.add(new Ball(-4, -7, 15.f, 0, 1));
        balls.add(new Ball(4, -9, 15.f, 0, 1));
        balls.add(new Ball(0, 0, 15.f, 0, 1));
        balls.add(new Ball(0, 3, 15.f, 0, 1));
        assertFalse(balls.isEmpty());
        balls.forEach(System.out::println);
        assertTrue(Ball.verifySortCloseVelY(0, balls));
    }

    @Test
    void testVerifySortByClosestVelY2() {
        balls.add(new Ball(0, 3, 15.f, 0, -4));
        balls.add(new Ball(-9, 7, 15.f, 0, -2));
        balls.add(new Ball(2, 2, 15.f, 0, 6));
        balls.add(new Ball(4, 1, 15.f, 0, 1));
        balls.add(new Ball(-2, 5, 15.f, 0, 3));
        balls.add(new Ball(0, 9, 15.f, 0, 4));
        balls.add(new Ball(0, 1, 15.f, 0, 0));

        balls.forEach(System.out::println);
        assertTrue(Ball.verifySortCloseVelY(0, balls));
    }

    @Test
    void testMerge() {
        List<Ball> balls1 = new LinkedList<>();
        balls1.add(new Ball(0, -5, 15.f, 0, 1));
        List<Ball> balls2 = new LinkedList<>();
        balls2.add(new Ball(-4, -7, 15.f, 0, 1));
        balls = Ball.merge(0, balls1, balls2);
        assertEquals(2, balls.size());
    }
}