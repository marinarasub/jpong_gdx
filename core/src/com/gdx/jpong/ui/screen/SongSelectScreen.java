package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.gdx.jpong.model.Ball;
import com.gdx.jpong.model.SongMap;
import com.gdx.jpong.ui.PongGame;

import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;


public class SongSelectScreen extends GameScreen implements Screen, InputProcessor {

    private FileHandle current;
    private final JsonReader reader;

    private final Skin skin;
    private final Array<FileHandle> songs;
    private List<FileHandle> songList;
    private ScrollPane songSelect;
    private ScrollPanelListener songSelectListener;
    private final Table songInfo;
    private final Table table;
    private TextButton menu, play;
    private final Stage stage;
    private InputMultiplexer multiInput;

    // CURRENT SONG
    private String currentTitle = "Select a song";
    private String currentArtist = "";
    private float currentBPM;
    //private Music music;
    // image music.png by https://creativemarket.com/Becris
    private final Texture defaultImage = new Texture(Gdx.files.internal("images/music.png"));
    private Texture currentImage = defaultImage;

    // SLIDERS
    private Slider diffSlider;
    private Slider sizeSlider;
    private Slider velSlider;
    private Slider dimSlider;

    public SongSelectScreen(PongGame game) {
        super(game);

        songs = new Array<>();
        getHandles(Gdx.files.internal("songs/"), songs);
        if (!songs.isEmpty())
            current = getRandomFile();

        stage = new Stage();
        songInfo = new Table();
        table = new Table();
        reader = new JsonReader();
        //songInfo.debug();
        //table.debug();
        skin = new Skin(Gdx.files.internal("uiskin.json")); // TODO extract to super

        reloadContent();
        stage.setViewport(viewport);
        stage.addActor(table);
    }

    private FileHandle getRandomFile() {
        Random r = new Random();
        return songs.get(r.nextInt(songs.size));
    }

    // Rebuild content with current song
    private void reloadContent() {
        try {
            gatherSongInfo();
        } catch (Exception e) {
            Gdx.app.error("FILE", "error reading song folder");
            current = getRandomFile();
        }
        table.clear();
        songInfo.clear();
        buildSongPanel();
        buildSongSelect();
        startButton();
        menuButton();
        buildTable();
        if (music != null)
            music.play();
        songList.setSelected(current);
    }

    private void gatherSongInfo() throws GdxRuntimeException {
        if (current != null) {
            JsonValue json = reader.parse(current);
            JsonValue meta = json.get("metadata");
            currentTitle = meta.getString("title");
            currentArtist = meta.getString("artist");
            JsonValue time = json.get("timing");
            currentBPM = time.getFloat("bpm");
            JsonValue files = json.get("files");
            FileHandle image = current.sibling(files.getString("background"));
            if (image.exists() && !image.isDirectory()) {
                Gdx.app.log("IMAGE", image.path());
                currentImage = new Texture(Gdx.files.internal(image.path()));
                currentImage.setAnisotropicFilter(16);
            } else {
                currentImage = defaultImage;
            }
            FileHandle song = current.sibling(files.getString("audio"));
            if (song.exists() && !song.isDirectory()) {
                Gdx.app.log("SONG", song.path());
                if (music != null) music.dispose();
                music = Gdx.audio.newMusic(song);
            }
        }
    }

    private void getHandles(FileHandle root, Array<FileHandle> handles) {
        FileHandle[] folders = root.list();
        for (FileHandle f : folders) {
            if (f.isDirectory()) {
                FileHandle songFile = f.child("song.json");
                if (songFile.exists()) {
                    handles.add(songFile);
                }
            }
        }
    }

    private void buildSongSelect() {
        songList = new List<>(skin);
        songList.setItems(songs);
        songList.addListener(new SongSelectListener());

        songSelect = new ScrollPane(songList, skin);
        songSelectListener = new ScrollPanelListener();
        songSelect.addListener(songSelectListener);
        songSelect.setScrollingDisabled(true, false);
    }

    private void startButton() {
        play = new TextButton("START", skin);
        play.addListener(new ClickListener() {
            @Override
            public void touchUp (InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Start clicked");
                Gdx.app.log("SELECT", "Select" + songSelect.getActor());
                dispose();
                game.play(mapFromCurrent()); // TODO
            }
        });
        play.pad(10);
    }

    private SongMap mapFromCurrent() {
        JsonValue json = reader.parse(current);

        TreeMap<Float, java.util.List<Ball>> balls = new TreeMap<>();
        JsonValue spawns = json.get("objects");
        for (JsonValue b : spawns) {
            Float time = b.getFloat("time");
            float x = b.getFloat("spawnX");
            float y = b.getFloat("spawnY");
            float radius = b.getFloat("radius");
            float velX = b.getFloat("velX");
            float velY = b.getFloat("velY");
            float accX = b.getFloat("accX");
            float accY= b.getFloat("accY");
            Ball ball = new Ball(x, y, radius, velX, velY, accX, accY);
            java.util.List<Ball> timeList = balls.get(time);
            if (timeList == null) {
                java.util.List<Ball> list = new LinkedList<>();
                list.add(ball);
                balls.put(time, list);
            }
            else {
                timeList.add(ball);
            }
        }
        float offset = json.get("timing").getFloat("startOffset");
        float dim = dimSlider.getVisualValue() / 100;
        float size = sizeSlider.getVisualValue() / 100;
        float vel = velSlider.getVisualValue() / 100;
        float diff = diffSlider.getVisualValue();
        return new SongMap(balls, music, currentImage, game.getSize(), offset, dim, size, vel, diff);
    }

    private void menuButton() {
        menu = new TextButton("BACK", skin);
        menu.addListener(new ClickListener() {
            @Override
            public void touchUp (InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Back clicked");
                dispose();
                game.menu();
            }
        });
        menu.pad(10);
    }

    private void buildSongPanel() {
        int width = 200;
        int height = 200;
        Image songImage = new Image(currentImage);
        songInfo.add(songImage).width(width).height(height).pad(10).left();

        VerticalGroup info = buildSongInfo();
        songInfo.add(info).row();
        addSliders();
    }

    private void addSliders() {
        float half = 0.5f;
        // song map will calculate.
        songInfo.add(new Label("AI Difficulty", skin)).pad(10);
        diffSlider = new Slider(0, 10, 1, false, skin);
        diffSlider.setVisualPercent(half);
        songInfo.add(diffSlider).row();
        // radius multiplier 50 - 150 %
        songInfo.add(new Label("Ball Size", skin)).pad(10);
        sizeSlider = new Slider(50, 150, 10, false, skin);
        sizeSlider.setVisualPercent(half);
        songInfo.add(sizeSlider).row();
        // velocity multiplier 50 - 150%
        songInfo.add(new Label("Velocity Multiplier", skin)).pad(10);
        velSlider = new Slider(50, 150, 5, false, skin);
        velSlider.setVisualPercent(half);
        songInfo.add(velSlider).row();
        // 0 - 100%
        songInfo.add(new Label("Background Dim", skin)).pad(10);
        dimSlider = new Slider(0, 100, 1, false, skin);
        dimSlider.setVisualPercent(half);
        songInfo.add(dimSlider).row();

        songInfo.add(new Label("Volume", skin)).pad(10);
        volSlider = new Slider(0, 100, 1, false, skin);
        volSlider.setVisualPercent(half);
        volSlider.addListener(new DragListener() {
            @Override
            public void touchDragged (InputEvent e, float x, float y, int point) {
                super.touchDragged(e, x, y, point);
                game.setVolume(volSlider.getVisualPercent());
            }
        });
        songInfo.add(volSlider).row();
    }

    private VerticalGroup buildSongInfo() {
        VerticalGroup info = new VerticalGroup();
        Label title = new Label(currentTitle, skin);
        info.addActor(title);
        info.space(10);
        info.addActor(new Label("by " + currentArtist, skin));
        info.addActor(new Label("BPM " + currentBPM, skin));

        info.pad(10);
        info.columnLeft();
        return info;
    }

    private void buildTable() {
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.add(songInfo).pad(10);
        table.add(songSelect).pad(10).height(Gdx.graphics.getHeight()/2).width(200).row();
        table.add(menu).pad(10).bottom().left();
        table.add(play).pad(10).bottom().right().row();
        table.setFillParent(true);
    }

    @Override
    public void show() {
        multiInput = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(multiInput);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        music.dispose();
        stage.dispose();
    }

    /* INPUT */

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    // COPIED TODO cite
    private class ScrollPanelListener extends InputListener {
        @Override
        public void enter(InputEvent e, float x, float y, int pointer,
                          Actor fromActor) {
            if (e.getTarget() instanceof ScrollPane) {
                ScrollPane scrollPane = (ScrollPane) e.getTarget();
                //if the scroll pane is scrollable
                if (!Float.isNaN(scrollPane.getScrollPercentY())) {
                    stage.setScrollFocus(scrollPane);
                }
            }
        }
    }

    private class SongSelectListener extends ClickListener {
        @Override
        public void touchUp(InputEvent e, float x, float y, int point, int button) {
            super.touchUp(e, x, y, point, button);
            FileHandle target = songList.getSelected();
            Gdx.app.log("SELECT", target.toString());
            current = target;
            reloadContent();
            songSelect.setScrollY(y);
        }
    }


}
