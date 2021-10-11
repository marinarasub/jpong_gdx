package com.gdx.test;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.jpong.model.object.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameObjectTest {

    private TestObject o;

    // Test class (non-abstract) for GameObject
    private class TestObject extends GameObject {
        public TestObject() {
            super(0, 0, 0, 0, 0, 0);
        }

        public TestObject(float x, float y, float velX, float velY, float accX, float accY) {
            super(x, y, velX, velY, accX, accY);
        }

        @Override
        public void draw(ShapeRenderer shape) {
            // don't need to test draw here.
        }
    }

    @BeforeEach
    void setUp() {
        o = new TestObject(0, 0, 0, 0, 0, 0);
    }

    private void reset(GameObject o) {
        setAll(o, 0);
    }

    private void setAll(GameObject o, float i) {
        o.setX(i);
        o.setY(i);
        o.setVelX(i);
        o.setVelY(i);
        o.setAccX(i);
        o.setAccY(i);
    }

    // Tests for reflectVelX()

    @Test
    void TestReflectVelXZero() {
        o.setVelX(0.0f);
        assertEquals(0.0f, o.getVelX());
        o.reflectVelX();
        assertEquals(-0.0f, o.getVelX());
    }

    @Test
    void TestReflectVelXPos() {
        o.setVelX(1.0f);
        assertEquals(1.0f, o.getVelX());
        o.reflectVelX();
        assertEquals(-1.0f, o.getVelX());
    }

    @Test
    void TestReflectVelXNeg() {
        o.setVelX(-1.0f);
        assertEquals(-1.0f, o.getVelX());
        o.reflectVelX();
        assertEquals(1.0f, o.getVelX());
    }

    // Tests for reflectVelY()

    @Test
    void TestReflectVelYZero() {
        o.setVelY(0.0f);
        assertEquals(0.0f, o.getVelY());
        o.reflectVelY();
        assertEquals(-0.0f, o.getVelY());
    }

    @Test
    void TestReflectVelYPos() {
        o.setVelY(1.0f);
        assertEquals(1.0f, o.getVelY());
        o.reflectVelY();
        assertEquals(-1.0f, o.getVelY());
    }

    @Test
    void TestReflectVelYNeg() {
        o.setVelY(-1.0f);
        assertEquals(-1.0f, o.getVelY());
        o.reflectVelY();
        assertEquals(1.0f, o.getVelY());
    }

    // tests for translate()

    @Test
    void testTranslateZero() {
        reset(o);
        o.translate(0.0f,0.0f);
        assertEquals(0.0f, o.getX());
        assertEquals(0.0f, o.getY());
    }

    @Test
    void testTranslate() {
        reset(o);
        o.translate(10.0f,10.0f);
        assertEquals(10.0f, o.getX());
        assertEquals(10.0f, o.getY());
    }

    // tests for scaleVelX()

    @Test
    void TestScaleVelXZero() {
        o.setVelX(1.0f);
        assertEquals(1.0f, o.getVelX());
        float dt = 0.0f;
        float res = o.scaleVelX(dt);
        assertEquals(0.0f, res);
    }

    @Test
    void TestScaleVelX1() {
        o.setVelX(100.0f);
        assertEquals(100.0f, o.getVelX());
        float dt = 1.0f;
        float res = o.scaleVelX(dt);
        assertEquals(dt * o.getVelX(), res);
    }

    @Test
    void TestScaleVelX60() {
        o.setVelX(100.0f);
        assertEquals(100.0f, o.getVelX());
        float dt = 1.0f / 60;
        float res = o.scaleVelX(dt);
        assertEquals(dt * o.getVelX(), res);
    }

    // tests for scaleVelY()

    @Test
    void TestScaleVelYZero() {
        o.setVelY(1.0f);
        assertEquals(1.0f, o.getVelY());
        float dt = 0.0f;
        float res = o.scaleVelY(dt);
        assertEquals(0.0f, res);
    }

    @Test
    void TestScaleVelY1() {
        o.setVelY(100.0f);
        assertEquals(100.0f, o.getVelY());
        float dt = 1.0f;
        float res = o.scaleVelY(dt);
        assertEquals(dt * o.getVelY(), res);
    }

    @Test
    void TestScaleVelY60() {
        o.setVelY(100.0f);
        assertEquals(100.0f, o.getVelY());
        float dt = 1.0f / 60;
        float res = o.scaleVelY(dt);
        assertEquals(dt * o.getVelY(), res);
    }


//    @Test
//    void scaleAccX() {
//        fail("TODO");
//    }
//
//    @Test
//    void scaleAccY() {
//        fail("TODO");
//    }

//    @Test
//    void update() {
//        fail("TODO");
//    }

    @Test
    void testEquivalent0() {
        assertTrue(o.equivalent(new TestObject()));
    }

    @Test
    void testEquivalentX() {
        o.setX(1);
        o.setVelX(1);
        o.setAccX(1);
        TestObject g = new TestObject(1, 0, 1, 0, 1, 0);
        assertTrue(o.equivalent(g));
        assertTrue(g.equivalent(o));
    }

    @Test
    void testEquivalentY() {
        o.setY(1);
        o.setVelY(1);
        o.setAccY(1);
        TestObject g = new TestObject(0, 1, 0, 1, 0, 1);
        assertTrue(o.equivalent(g));
        assertTrue(g.equivalent(o));
    }

    @Test
    void testEquivalent() {
        setAll(o, 1);
        TestObject g = new TestObject(1, 1, 1, 1, 1, 1);
        assertTrue(o.equivalent(g));
        assertTrue(g.equivalent(o));
    }

    @Test
    void testNotEquivalentOneVal() {
        TestObject g = new TestObject();
        g.setY(1);
        assertFalse(o.equivalent(g));
        assertFalse(g.equivalent(o));
    }

    @Test
    void testNotEquivalent() {
        setAll(o, 1);
        TestObject g = new TestObject();
        assertFalse(o.equivalent(g));
        assertFalse(g.equivalent(o));
    }

    @Test
    void testNotEquivalentX() {
        o.setX(1);
        o.setVelX(1);
        o.setAccX(1);
        TestObject g = new TestObject(0, 1, 0, 1, 0, 1);
        assertFalse(o.equivalent(g));
        assertFalse(g.equivalent(o));
    }

    @Test
    void testNotEquivalentY() {
        o.setY(1);
        o.setVelY(1);
        o.setAccY(1);
        TestObject g = new TestObject(1, 0, 1, 0, 1, 0);
        assertFalse(o.equivalent(g));
        assertFalse(g.equivalent(o));
    }

    @Test
    void testNotEquivalentNegative() {
        float i = 1.f;
        setAll(o, i);
        TestObject g = new TestObject();
        setAll(g, -1);
        assertFalse(o.equivalent(g));
        assertFalse(g.equivalent(o));
    }
}