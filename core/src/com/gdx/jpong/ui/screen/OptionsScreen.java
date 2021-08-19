package com.gdx.jpong.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.gdx.jpong.ui.PongGame;

import java.util.*;
import java.util.List;

public class OptionsScreen extends GameScreen implements Screen {

    private Skin skin;
    private Table table;
    private ScrollPane options;
    private Stage stage;

    public OptionsScreen(PongGame game) {
        super(game);
        table = new Table();
        //table.debug();
        options = new ScrollPane(table);
        options.setFillParent(true);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        buildTable();
        stage = new Stage();
        stage.setViewport(viewport);
        stage.addActor(options);
    }

    private void buildTable() {
        displayOptions();
        graphicsOptions();
        audioOptions();
        inputOptions();
        // add back button to menu
        TextButton menu = new TextButton("BACK", skin);
        menu.addListener(new ClickListener() {
            @Override
            public void touchUp (InputEvent e, float x, float y, int point, int button) {
                super.touchUp(e, x, y, point, button);
                Gdx.app.log("BUTTON", "Back clicked");
                dispose();
                game.menu();
            }
        });
        table.add(menu).left().row();
    }

    private void inputOptions() {
        table.add(new Label("INPUT", skin)).left().colspan(2).space(10).row(); // TODO sensitivity, Keybinds
    }

    private void displayOptions() {
        table.add(new Label("DISPLAY", skin)).left().colspan(2).space(10).row();
        resolutionOption();
        displayModeOption();
        refreshRateOption();
    }

    private void refreshRateOption() {
        SelectBox<Integer> fps = new SelectBox<>(skin);
        Graphics.DisplayMode nativeDisplay = Gdx.graphics.getDisplayMode();
        int nativeFPS = nativeDisplay.refreshRate;
        fps.setItems(nativeFPS, 144, 120, 60, 30); // TODO sort by hz, remove duplicate, unlimited
        fps.setSelected(game.getTargetFPS());
        fps.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setTargetFPS(fps.getSelected());
            }
        });
        table.add(new Label("Refresh Rate", skin)).left().space(10);
        table.add(fps).width(100).right().space(10).row();
    }

    private void displayModeOption() {
        SelectBox<String> screenMode = new SelectBox<>(skin);
        screenMode.setItems("Windowed", "Fullscreen");
        screenMode.setSelected(game.isFullscreen() ? "Fullscreen" : "Windowed");
        screenMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (screenMode.getSelected().equals("Windowed")) {
                    game.changeDisplay(false, 1024, 768); // TODO resolution list based on monitor
                } else if (screenMode.getSelected().equals("Fullscreen")) {
                    game.changeDisplay(true, 0,0);
                }
            }
        });
        table.add(new Label("Display Mode", skin)).left().space(10);
        table.add(screenMode).width(200).height(30).space(10).row();
    }

    private void resolutionOption() {
        SelectBox<Vector2> resolution = new SelectBox<>(skin);
        Vector2[] supportedResolutions = getSupportedResolutions(); // TODO sorted by size
        resolution.setItems(supportedResolutions);
        resolution.setSelected(game.getSize());
        resolution.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Vector2 size = resolution.getSelected();
                game.changeDisplay(Gdx.graphics.isFullscreen(), (int)size.x, (int)size.y);
            }
        });
        table.add(new Label("Resolution", skin)).left().space(10);
        table.add(resolution).width(200).height(30).space(10).row();
    }

    private Vector2[] getSupportedResolutions() {
        Set<Vector2> desiredResolutions = new HashSet<>(); // TODO read from file, sorted
        Graphics.DisplayMode nativeDisplay = Gdx.graphics.getDisplayMode();
        desiredResolutions.add(new Vector2(nativeDisplay.width, nativeDisplay.height)); // native
        desiredResolutions.add(new Vector2(3840, 2160)); // 4K
        desiredResolutions.add(new Vector2(2560, 1440)); // 1440p
        desiredResolutions.add(new Vector2(1920, 1080)); // 1080p
        desiredResolutions.add(new Vector2(1280, 720)); // 720p
        desiredResolutions.add(new Vector2(1024, 768)); // 720p
        desiredResolutions.add(new Vector2(640, 480)); // 480p
        desiredResolutions.add(new Vector2(-1, -1)); // TEST ILLEGAL

        desiredResolutions.removeIf(size -> size.x > nativeDisplay.width || size.y > nativeDisplay.height);
        return desiredResolutions.toArray(new Vector2[0]);
    }

    private void graphicsOptions() {
        table.add(new Label("GRAPHICS", skin)).left().colspan(2).space(10).row();
        CheckBox vSync = new CheckBox("VSync", skin);
        vSync.setChecked(game.isVSyncEnabled());
        vSync.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setVSync(vSync.isChecked());
            }
        });
        table.add(vSync).left().colspan(2).space(10).row();
    }

    private void audioOptions() {
        table.add(new Label("AUDIO", skin)).left().colspan(2).space(10).row();
        table.add(new Label("Volume", skin)).space(10).left();
        Slider volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setVisualPercent(game.getVolume());
        volumeSlider.addListener(new DragListener() {
            @Override
            public void touchDragged (InputEvent e, float x, float y, int point) {
                super.touchDragged(e, x, y, point);
                game.setVolume(volumeSlider.getVisualPercent());
            }
        });
        table.add(volumeSlider).width(200).right().space(10).row();
    }

    /* CONTROL */

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
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

    }

    // TODO fix screens disposal - blank, etc.
    @Override
    public void dispose() {
        //stage.dispose();
    }
}
