package com.gdx.jpong.model.map;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gdx.jpong.exception.FileLoadException;
import com.gdx.jpong.model.object.Ball;
import com.sun.xml.internal.bind.v2.model.core.ID;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * File handle with tools for representing a JSON song map file.
 * JSON KEYS FOR MAPPING (see below)
 */
public class SongHandle extends FileHandle {

    /**
     * KEYS ENUM: ver 0.0
     * AVOID ANY CHANGES
     */
    // META
    static final String META_KEY = "metadata";
    static final String TITLE_KEY = "title";
    static final String ARTIST_KEY = "artist";
    static final String AUTHOR_KEY = "author";
    static final String ID_KEY = "id"; // TODO in the future, check hash against id
    // TIMING
    static final String TIMING_KEY = "timing";
    static final String BPM_KEY = "bpm";
    static final String OFFSET_KEY = "startOffset";
    // FILES
    static final String FILES_KEY = "files";
    static final String AUDIO_KEY = "audio";
    static final String IMAGE_KEY = "background";
    // SPAWNS
    static final String OBJ_KEY = "objects";
    static final String TIME_KEY = "time";
    static final String POSX_KEY = "spawnX";
    static final String POSY_KEY = "spawnY";
    static final String RADIUS_KEY = "radius";
    static final String VELX_KEY = "velX";
    static final String VELY_KEY = "velY";
    static final String ACCX_KEY = "accX";
    static final String ACCY_KEY = "accY";

    // image music.png by https://creativemarket.com/Becris
    static final Texture DEFAULT_IMAGE = new Texture(Gdx.files.internal("images/music.png"));

    // FIELD
    private JsonValue json;

    /**
     * Creates a file handle for song map JSON file
     * @param path string name of file (Ex: file.json)
     */
    public SongHandle(String path) {
        super(path, Files.FileType.Internal);
        JsonReader reader = new JsonReader();
        json = reader.parse(this.name());
    }

    // GETTER FOR JSON VALUES USING KEYS

    /**
     * ASSUMES that caller knows the return type of object
     * @param key Key for value in meta data
     * @return Object associated with key in JSON object metadata
     */
    private Object metaData(String key) {
        JsonValue meta = json.get(META_KEY);
        return meta.getString(key);
    }

    /**
     * @return song title specified by file
     */
    public String getTitle() {
        return (String) metaData(TITLE_KEY);
    }

    /**
     * @return song artist specified by file
     */
    public String getArtist() {
        return (String) metaData(ARTIST_KEY);
    }

    /**
     * @return song author (map creator) specified by file
     */
    public String getAuthor() {
        return (String) metaData(AUTHOR_KEY);
    }

    /**
     * @return file (map) ID specified by file
     */
    public int getID() {
        return (int) metaData(ID_KEY);
    }

    /**
     * @return true if the ID stored in file matches the hash of the file
     */
    public boolean checkIDHash() {
        return getHash() == getID();
    }

    /**
     * @return hash of file TODO use non default hash
     */
    private int getHash() {
        return json.hashCode();
    }


    /**
     * File is always represented by String (its path)
     * @param key Key for value type of file
     * @return Object associated with key in JSON object files
     */
    private String file(String key) {
        JsonValue file = json.get(FILES_KEY);
        return file.getString(key);
    }

    /**
     * @return null if file does not exist in this (map) directory
     *         else return GDX Music from file
     */
    public Music getMusic() {
        FileHandle song = this.sibling(file(AUDIO_KEY));
        Music music = null;
        if (song.exists() && !song.isDirectory()) {
            music = Gdx.audio.newMusic(song);
        }
        return music;
    }

    /**
     * @return default texture if file does not exist
     *         else texture from image path given from file
     */
    public Texture getImage() {
        FileHandle image = this.sibling(file(IMAGE_KEY));
        Texture t;
        if (image.exists() && !image.isDirectory()) {
            t = new Texture(Gdx.files.internal(image.path()));
            t.setAnisotropicFilter(16);
        } else {
            t = DEFAULT_IMAGE;
        }
        return t;
    }

    /**
     * Timing section will always return a float
     * @param key key in timing object
     * @return a timing float that corresponds to given key
     */
    private float timing(String key) {
        JsonValue timing = json.get(TIMING_KEY);
        return timing.getFloat(key);
    }

    /**
     * @return song BPM specified by file
     */
    public float getBPM() {
        return timing(BPM_KEY);
    }

    /**
     * @return start offset for music playback
     */
    public float getOffset() {
        return timing(OFFSET_KEY);
    }

    /**
     * @return spawn points stored as TreeMap read from JSON file
     */
    public TreeMap<Float, List<Ball>> getSpawns() throws FileLoadException {
        TreeMap<Float, List<Ball>> balls = new TreeMap<>();
        JsonValue spawns = json.get(OBJ_KEY);
        for (JsonValue b : spawns) {
            Float time;
            float x, y, radius, velX, velY, accX, accY;
            try {
                time = b.getFloat(TIME_KEY);
                x = b.getFloat(POSX_KEY);
                y = b.getFloat(POSY_KEY);
                radius = b.getFloat(RADIUS_KEY);
                velX = b.getFloat(VELX_KEY);
                velY = b.getFloat(VELY_KEY);
                accX = b.getFloat(ACCX_KEY);
                accY= b.getFloat(ACCY_KEY);
            } catch (GdxRuntimeException e) {
                throw new FileLoadException("Failed to load spawn times from " + this.name());
            }
            Ball ball = new Ball(x, y, radius, velX, velY, accX, accY);
            List<Ball> timeList = balls.get(time);
            if (timeList == null) {
                List<Ball> list = new LinkedList<>();
                list.add(ball);
                balls.put(time, list);
            }
            else {
                timeList.add(ball);
            }
        }
        return balls;
    }
}
