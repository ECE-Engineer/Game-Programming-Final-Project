package com.mygdx.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Alex241Intro;
import java.io.FileNotFoundException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class ReadyEEGTestScreen implements Screen {

    private final int PLAY_BUTTON_WIDTH = 105;
    private final int PLAY_BUTTON_HEIGHT = 40;
    private final int PLAY_BUTTON_Y = Gdx.graphics.getHeight() - 150;

    private Alex241Intro game;
    private Texture playButtonActive;
    private Texture playButtonInactive;

    public ReadyEEGTestScreen(Alex241Intro game) {
        this.game = game;

        this.playButtonActive = new Texture("play_button_active.png");
        this.playButtonInactive = new Texture("play_button_inactive.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        int x = game.WIDTH/2 - PLAY_BUTTON_WIDTH/2;
        if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH*2 && Gdx.input.getX() > x && game.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT*2 && game.HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y) {
            game.batch.draw(playButtonActive,x,PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH*2,PLAY_BUTTON_HEIGHT*2);

            //check if the mouse was clicked start the game
            if (Gdx.input.justTouched()) {
                this.dispose();
                game.setScreen(new SightModalityTest(game));
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            this.dispose();
            game.setScreen(new SightModalityTest(game));
        } else {
            game.batch.draw(playButtonInactive,x,PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH*2,PLAY_BUTTON_HEIGHT*2);
        }

        game.batch.end();
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
//        dispose();
    }

    @Override
    public void dispose() {
//        world.dispose();
//        debugRenderer.dispose();
    }
}