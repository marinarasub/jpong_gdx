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
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.gdx.jpong.exception.FileLoadException;
import com.gdx.jpong.model.map.SongHandle;
import com.gdx.jpong.model.map.SongReader;
import com.gdx.jpong.ui.PongGame;

/**
 * Screen for song/level select of maps that exist in internal directory for non-play information
 * , passes a playable map to game screen when ready.
 * @see GameScreen
 */
public class SongSelectScreen extends GameScreen implements Screen, InputProcessor {

    private final Skin skin;
    private final SongReader reader;

    private List<SongHandle> songList; // list UI maintained here
    private ScrollPane songSelect;
    private ScrollPanelListener songSelectListener;
    private final Table songInfo;
    private final Table table;
    private TextButton menu, play;
    private final Stage stage;
    private InputMultiplexer multiInput;

    // SLIDERS
    private Slider diffSlider;
    private Slider sizeSlider;
    private Slider velSlider;
    private Slider dimSlider;
    private Slider volSlider;

    public SongSelectScreen(PongGame game) {
        super(game);
        reader = new SongReader(game.getSize());
        stage = new Stage();
        songInfo = new Table();
        table = new Table();
        skin = new Skin(Gdx.files.internal("uiskin.json")); // TODO extract to super
        songList = new List<>(skin);
        reloadContent();
        previewSelected();
        stage.setViewport(viewport);
        stage.addActor(table);
    }

    // Rebuild content with current song
    public void reloadContent() {
        try {
            reader.clear();
            reader.getHandles(Gdx.files.internal("songs/"));
            songList.setItems(reader.getSongs());
        } catch (Exception e) {
            Gdx.app.error("FILE LOAD", "error reading song folder: " + e.getMessage());
        }
        clearUI();
        buildUI();
    }

    /**
     * Tries to play music of selected song file
     */
    private void previewSelected() {
        try {
            SongHandle sh = getSelectedSong();
            if (music != null) music.dispose();
            if (sh != null) music = sh.getMusic();
            assert music != null;
            music.play();
            music.setOnCompletionListener(e -> songList.setSelected(reader.getRandomFile()));
        } catch (FileLoadException e) {
            Gdx.app.error("SONG", e.getMessage());
        }
    }

    /**
     * @return the selected song (highlighted) in the song select pane
     *          unless none, then return random file
     *          (can be null)
     */
    private SongHandle getSelectedSong() {
        SongHandle s = songList.getSelected();
        if (s == null) s = reader.getRandomFile();
        songList.setSelected(s);
        return s;
    }

    /**
     * Builds all UI components, including song info panel, song select pane and buttons.
     */
    private void buildUI() {
        buildSongPanel();
        buildSongSelect();
        startButton();
        menuButton();
        buildTable();
    }

    /**
     * Clears the UI of any previously set state
     */
    private void clearUI() {
        table.clear();
        songInfo.clear();
        songList.clear();
    }

    /**
     * Builds the song select pane for choosing different song files/levels
     */
    private void buildSongSelect() {
        songList = new List<>(skin);
        songList.setItems(reader.getSongs());
        songList.addListener(new SongSelectListener());

        songSelect = new ScrollPane(songList, skin);
        songSelectListener = new ScrollPanelListener();
        songSelect.addListener(songSelectListener);
        songSelect.setScrollingDisabled(true, false);
    }

    /**
     * Adds a button to attempt to create a song map and start the current map file
     * If successful, map modification values will be taken from slider values
     */
    private void startButton() {
        play = new TextButton("START", skin);
        play.addListener(new ClickListener() {
            @Override
            public void touchUp (InputEvent ev, float x, float y, int point, int button) {
                super.touchUp(ev, x, y, point, button);
                Gdx.app.log("BUTTON", "Start clicked");
                Gdx.app.log("SELECT", "Select" + songSelect.getActor());
                SongHandle file = getSelectedSong();
                try {
                    game.play(reader.buildSongMap(file,
                            dimSlider.getVisualPercent(),
                            sizeSlider.getValue(),
                            velSlider.getValue(),
                            diffSlider.getValue()));
                    dispose();
                } catch (FileLoadException ex) {
                    Gdx.app.error("START", ex.getMessage());
                }
            }
        });
        play.pad(10);
    }

    /**
     * Creates a button for returning to the main menu
     */
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

    /**
     * Creates an info panel with album art and song info (title, artist etc.)
     */
    private void buildSongPanel() {
        int width = 200;
        int height = 200;
        SongHandle sh = getSelectedSong();
        VerticalGroup infoBox = null;
        if (sh != null) {
            try {
                Image songImage;
                Texture t = sh.getImage();
                if (t != null) songImage = new Image(t);
                else songImage = new Image(reader.getDefaultImage());
                songInfo.add(songImage).width(width).height(height).pad(10).left();
                infoBox = buildSongInfo();
            } catch (FileLoadException e) {
                Gdx.app.error("SONG INFO", e.getMessage());
            }
        }
        songInfo.add(infoBox).row();
        addSliders();
    }

    /**
     * Adds all options/modification sliders to song select
     */
    // TODO common method for slider creation
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

    /**
     * Creates a column for displaying song info
     * @throws FileLoadException if song metadata cannot be read
     * @return a Vertical Group (1 column table) of song info
     */
    private VerticalGroup buildSongInfo() throws FileLoadException {
        VerticalGroup info = new VerticalGroup();
        SongHandle sh = getSelectedSong();
        Label title = new Label(sh.getTitle(), skin);
        info.addActor(title);
        info.space(10);
        info.addActor(new Label("by " + sh.getArtist(), skin));
        info.addActor(new Label("BPM " + sh.getBPM(), skin));
        info.pad(10);info.columnLeft();
        return info;
    }

    /**
     * Builds the entire song menu consisting of a song info panel (left) and selection panel (right)
     */
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
            SongHandle target = getSelectedSong();
            Gdx.app.log("SELECT", target.toString());
            reloadContent();
            songList.setSelected(target);
            songSelect.setScrollY(y);
            previewSelected();
        }
    }


}
