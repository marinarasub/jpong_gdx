package com.gdx.jpong.model.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jpong.exception.FileLoadException;
import com.gdx.jpong.model.object.Ball;

import java.util.List;
import java.util.TreeMap;

/**
 * Reads JSON map and returns song info for non-play information (i.e. title, length etc) and song map
 * @see SongMap
 * (todo may use nested in SongReader)
 */
public class SongBuilder {

    private Vector2 playSize;

    // image music.png by https://creativemarket.com/Becris
    private Texture defaultImage = new Texture(Gdx.files.internal("images/music.png"));

    public SongBuilder(Vector2 playSize) {
        this.playSize = playSize;
    }

    /**
     * @param img Default background image for maps with no specified image
     */
    public void setDefaultImage(Texture img) {
        defaultImage = img;
    }

    public Texture getDefaultImage() {
        return defaultImage;
    }

    /**
     * Builds a full Song Map from song handle
     * @param sh song handle (i.e. map file)
     * @param dim background dim percent [0, 1] (0 is no dim)
     * @param ballSize ball radius multiplier
     * @param velMult ball velocity multiplier
     * @param diff Pong AI difficulty
     * @return song map built from values set in map file
     * @throws FileLoadException if any value is not loaded correctly
     */
    public SongMap build(SongHandle sh, float dim, float ballSize, float velMult, float diff) throws FileLoadException {
        if (sh == null) throw new FileLoadException("No Map to Build");
        SongMap map = createSongMap(sh);
        Texture bg = sh.getImage();
        if (bg != null) {
            map.setBackground(bg);
        } else map.setBackground(defaultImage);
        map.setBackgroundDim(dim);
        map.setMods(ballSize, velMult, diff);
        return map;
    }

    /**
     * @param sh Song handle to parse map from
     * @return Song map with valyes from song handle using minimum constructor
     */
    private SongMap createSongMap(SongHandle sh) throws FileLoadException {
        TreeMap<Float, List<Ball>> spawns;
        Music music;
        float offset;
        spawns = sh.getSpawns();
        music = sh.getMusic();
        offset = sh.getOffset();
        return new SongMap(spawns, music, this.playSize, offset);
    }

}
