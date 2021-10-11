package com.gdx.jpong.model.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.jpong.exception.FileLoadException;

import java.util.Random;

/**
 * Reads songs from directory;
 * @see SongHandle
 */
public class SongReader {

    private Array<SongHandle> songs;
    private SongBuilder builder;

    static final String DEFAULT_FILENAME = "song.json";

    public SongReader(Vector2 playSize) {
        songs = new Array<>();
        builder = new SongBuilder(playSize);
    }

    public Array<SongHandle> getSongs() {
        return this.songs;
    }

    public void addSong(SongHandle songHandle) {
        songs.add(songHandle);
    }

    public SongHandle getRandomFile() {
        if (songs.size == 0) return null;
        Random r = new Random();
        return songs.get(r.nextInt(songs.size));
    }

    public void clear() {
        songs.clear();
    }

    /**
     * Retrieves all map files "song.json" in sub folders and adds to song handle array
     * @param root root folder path file handle
     * @throws FileLoadException if a song file cannot be created
     */
    public void getHandles(FileHandle root) throws FileLoadException {
        FileHandle[] folders = root.list();
        for (FileHandle f : folders) {
            if (f.isDirectory() && !f.name().equals("test")) {
                FileHandle file = f.child(DEFAULT_FILENAME);
                SongHandle songFile = new SongHandle(file.path());
                if (songFile.exists()) {
                    songs.add(songFile);
                }
            }
        }
    }

    /**
     * builds a song map with song builder
     * @param sh song handle (map file)
     * @param dim background dim % [0, 1]
     * @param size ball size multiplier
     * @param vel velocity multiplier
     * @param diff AI difficulty [1, 10]
     * @return SongMap from map file
     * @throws FileLoadException if there is an issue creating the map
     */
    public SongMap buildSongMap(SongHandle sh, float dim, float size, float vel, float diff) throws FileLoadException {
        return builder.build(sh, dim, size, vel, diff);
    }

    public Texture getDefaultImage() {
        return builder.getDefaultImage();
    }

}
