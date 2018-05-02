package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.entities.ChatBot;

import java.io.FileNotFoundException;

public class GameOverScreen implements Screen {



    Alex241Intro game;
    BitmapFont scoreFont;
    private Texture background;
    private Music theme;
    private ChatBot chatBot;
    private String dirSlash;

    public GameOverScreen(Alex241Intro game, ChatBot chatBot, String dirSlash) {
        this.game = game;
        this.dirSlash = dirSlash;
        this.chatBot = chatBot;

        scoreFont = new BitmapFont(Gdx.files.internal("fonts" + dirSlash + "score.fnt"));
        this.background = new Texture("rubber_duck.png");

        this.theme = Gdx.audio.newMusic(Gdx.files.internal("game_over.mp3"));
        theme.setVolume(0.1f);
    }

    @Override
    public void show() {
        if (!theme.isPlaying()) {
            theme.play();
        }
    }

    @Override
    public void render(float delta) {
        chatBot.think(delta, 0, 0);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(background, Gdx.graphics.getWidth()/2 - background.getWidth()/2, 0);

        GlyphLayout Title = new GlyphLayout(scoreFont, "GAME OVER");
        scoreFont.draw(game.batch, Title, Gdx.graphics.getWidth() / 2 - Title.width / 2, Gdx.graphics.getHeight() - Title.height - 15);

        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "TRY AGAIN");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "MAIN MENU");

        float tryAgainX = Gdx.graphics.getWidth() / 2 - tryAgainLayout.width / 2;
        float tryAgainY = Gdx.graphics.getHeight() / 2 - tryAgainLayout.height / 2;
        float mainMenuX = Gdx.graphics.getWidth() / 2 - mainMenuLayout.width / 2;
        float mainMenuY = Gdx.graphics.getHeight() / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        //if try again
        if (Gdx.input.justTouched()) {
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                this.dispose();
                game.batch.end();
                try {
                    chatBot.stopAudio();
                    game.setScreen(new Level1(game, dirSlash));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        //if main menu
        if (Gdx.input.justTouched()) {
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                chatBot.stopAudio();
                game.setScreen(new MainMenuScreen(game, dirSlash));
                return;
            }
        }

        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

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

    }

    @Override
    public void dispose() {
        if (theme.isPlaying()) {
            theme.stop();
            theme.setLooping(false);
        }
    }
}
