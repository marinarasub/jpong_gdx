package com.gdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.jpong.exception.FileLoadException;
import com.gdx.jpong.model.map.SongHandle;
import com.gdx.jpong.model.object.Ball;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class SongHandleTest {

    static final String TEST_FOLDER = "songs/test/";

    SongHandle s;

    @BeforeEach
    void setUp() throws FileLoadException {
        //s = new SongHandle(TEST_FOLDER + "test.json");
    }

    @Test
    void TestLoadFile() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void TestLoadFileDNE() {
        try {
            s = new SongHandle(TEST_FOLDER + "thisfiledoesnotexist.json");
            fail("Song handle was not supposed to exist");
        } catch (FileLoadException e) {
            // pass
        }
    }

    @Test
    void getTitle() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
            assertEquals("TEST MAP", s.getTitle());
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getArtist() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
            assertEquals("ARTIST", s.getArtist());
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getAuthor() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
            assertEquals("marinarasub", s.getAuthor());
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getID() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
            assertEquals(0, s.getID());
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

//    @Test
//    void checkIDHash() {
//        fail("TODO"); // TODO
//    }

    @Test
    void getMusic() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
            assertEquals("marinarasub", s.getAuthor());
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getImage() {
//        try { TODO does not load Gdx class in JUnit, must test manually
//            s = new SongHandle(TEST_FOLDER + "test.json");
//            Texture bg = s.getImage();
//            Texture expect = new Texture(Gdx.files.internal(TEST_FOLDER + "image.png"));
//            assertEquals(expect.hashCode(), bg.hashCode());
//        } catch (FileLoadException e) {
//            fail(e.getMessage());
//        }
    }

    @Test
    void getBPM() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
            assertEquals(100.f, s.getBPM());
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getOffset() {
        try {
            s = new SongHandle(TEST_FOLDER + "test.json");
            assertEquals(1.f, s.getOffset());
        } catch (FileLoadException e) {
            fail(e.getMessage());
        }
    }

//    @Test
//    void testGetSpawns() { TODO does not work with JUnit, does not load Sound interface from LibGDX
//        try {
//            s = new SongHandle(TEST_FOLDER + "test.json");
//            TreeMap<Float, List<Ball>> spawns = s.getSpawns();
//            assertEquals(2, spawns.size());
//            assertEquals(null, spawns.get(0.f));
//        } catch (FileLoadException e) {
//            fail(e.getMessage());
//        }
//    }
}