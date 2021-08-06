package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gdx.jpong.ui.PongGame;


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
    private final InputMultiplexer multiInput;

    private String currentTitle = "Select a song";
    private String currentArtist = "";
    private float currentBPM;
    // image music.png by https://creativemarket.com/Becris
    private final Image defaultImage = new Image(new Texture(Gdx.files.internal("images/music.png")));
    private Image currentImage = defaultImage;

    // TODO load map based on json file

    public SongSelectScreen(PongGame game) {
        super(game);

        songs = new Array<>();
        getHandles(Gdx.files.internal("songs/"), songs);
        if (!songs.isEmpty())
            current = songs.get(0);

        stage = new Stage();
        songInfo = new Table();
        table = new Table();
        reader = new JsonReader();
        //songInfo.debug();
        //table.debug();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        reloadContent();
        stage.addActor(table);
        multiInput = new InputMultiplexer(stage, this);
    }

    private void reloadContent() {
        gatherSongInfo();
        table.clear();
        songInfo.clear();
        buildSongPanel();
        buildSongSelect();
        startButton();
        menuButton();
        buildTable();
    }

    private void gatherSongInfo() {
        if (current != null) {
            JsonValue json = reader.parse(current);
            JsonValue meta = json.get("metadata");
            currentTitle = meta.getString("title");
            currentArtist = meta.getString("artist");
            currentBPM = meta.getFloat("bpm");
            JsonValue files = json.get("files");
            FileHandle image = current.sibling(files.getString("background"));
            if (image.exists() && !image.isDirectory()) {
                Gdx.app.log("IMAGE", image.path());
                currentImage = new Image(new Texture(Gdx.files.internal(image.path())));
            } else {
                currentImage = defaultImage;
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
                game.play();
            }
        });
        play.pad(10);
    }

    private void menuButton() {
        menu = new TextButton("BACK", skin);
        menu.addListener(new ClickListener() {
            @Override
            public void touchUp (InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Back clicked");
                game.menu();
            }
        });
        menu.pad(10);
    }



    private void buildSongPanel() {
        Image songImage = currentImage;
        songInfo.add(songImage).height(200).width(200).pad(10).left();
        VerticalGroup info = buildSongInfo();
        songInfo.add(info).row();
        songInfo.add(new Label("AI Difficulty", skin)).pad(10);
        songInfo.add(new Slider(1, 10, 1, false, skin)).pad(10).row();
        songInfo.add(new Label("Ball Size", skin)).pad(10);
        songInfo.add(new Slider(5, 30, 1, false, skin)).pad(10).row();
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
        Gdx.input.setInputProcessor(multiInput);
    }

    @Override
    public void render(float delta) {
        clear();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
            FileHandle target = songList.getItemAt(y);
            Gdx.app.log("SELECT", target != null ? target.toString() : "none");
            if (target != null) {
                current = target;
                reloadContent();
                songSelect.setScrollY(y);
                songList.setSelected(current);
            }
        }
    }


}
