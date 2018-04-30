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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Alex241Intro;

import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;

public class GameWonScreen implements Screen {
    private final Texture AIR_G = new Texture("animation\\green\\up_64.png");
    private final Texture AIR_R = new Texture("animation\\red\\up_64.png");
    private final Texture AIR_W = new Texture("animation\\white\\up_64.png");

    private final Texture LSG = new Texture("green\\mouse_left_still_64.png");
    private final Texture LLG = new Texture("green\\mouse_left_left_64.png");
    private final Texture LRG = new Texture("green\\mouse_left_right_64.png");
    private final Texture RSG = new Texture("green\\mouse_right_still_64.png");
    private final Texture RLG = new Texture("green\\mouse_right_left_64.png");
    private final Texture RRG = new Texture("green\\mouse_right_right_64.png");

    private final Texture LSR = new Texture("red\\mouse_left_red_still_64.png");
    private final Texture LLR = new Texture("red\\mouse_left_red_left_64.png");
    private final Texture LRR = new Texture("red\\mouse_left_red_right_64.png");
    private final Texture RSR = new Texture("red\\mouse_right_red_still_64.png");
    private final Texture RLR = new Texture("red\\mouse_right_red_left_64.png");
    private final Texture RRR = new Texture("red\\mouse_right_red_right_64.png");

    private final Texture LSW = new Texture("white\\mouse_left_white_still_64.png");
    private final Texture LLW = new Texture("white\\mouse_left_white_left_64.png");
    private final Texture LRW = new Texture("white\\mouse_left_white_right_64.png");
    private final Texture RSW = new Texture("white\\mouse_right_white_still_64.png");
    private final Texture RLW = new Texture("white\\mouse_right_white_left_64.png");
    private final Texture RRW = new Texture("white\\mouse_right_white_right_64.png");

    private float START_GREEN;
    private float START_RED;
    private float START_WHITE;
    private boolean LOOK_GREEN;
    private boolean LOOK_RED;
    private boolean LOOK_WHITE;
    private float GREEN_TIMER;
    private float RED_TIMER;
    private float WHITE_TIMER;

    private int previousMoveGreen;
    private int previousMoveRed;
    private int previousMoveWhite;

    private Sprite spriteR;
    private Texture mouseTextureR;
    private Sprite spriteG;
    private Texture mouseTextureG;
    private Sprite spriteW;
    private Texture mouseTextureW;

    private final float DISPLACEMENT_VALUE = 10f;

    Alex241Intro game;
    BitmapFont scoreFont;
    private Texture background;
    private World world;
    private final int PLAY_BUTTON_WIDTH = 366;
    private final int PLAY_BUTTON_HEIGHT = 247;
    private final Texture playSoundTexture = new Texture("play_sound.png");
    private Music theme;

    private boolean playFlag;
    private int state;//0 state do nothing, 1 state walk right to mid, 2 state random arm raise, 3 state all raise arms, 4 state walk right off the screen
    private final float MUSIC_TIMER = 104.75f;
    private final float MOVEMENT_TIMER = 0.15f;
    private final float STATE3_TIMER = 5f;
    private boolean state3FlagOneTime = true;
    private boolean b1G;
    private boolean b1R;
    private boolean b1W;

    private boolean runAtBeginning = false;

    private Music theme2;
    private boolean playtheme2Once = true;

    public GameWonScreen(Alex241Intro game) {
        this.game = game;
        world = new World(new Vector2(0f,-9.81f), true);
        playFlag = false;
        theme = Gdx.audio.newMusic(Gdx.files.internal("Bulgarian-National-Anthem-Mila-Rodino.mp3"));
        theme2 = Gdx.audio.newMusic(Gdx.files.internal("game_complete.mp3"));
        scoreFont = new BitmapFont(Gdx.files.internal("fonts\\score2.fnt"));
        background = new Texture("level_1.png");


        //look right
        this.LOOK_RED = false;
        this.LOOK_GREEN = false;
        this.LOOK_WHITE = false;
        this.mouseTextureR = (LOOK_RED) ? LSR : RSR;
        this.mouseTextureG = (LOOK_GREEN) ? LSG : RSG;
        this.mouseTextureW = (LOOK_WHITE) ? LSW : RSW;
        previousMoveGreen = (LOOK_GREEN) ? 0 : 3;
        previousMoveRed = (LOOK_RED) ? 0 : 3;
        previousMoveWhite = (LOOK_WHITE) ? 0 : 3;
        //set START position
        this.START_GREEN = (-mouseTextureR.getWidth()*3);
        this.START_RED = (-mouseTextureG.getWidth());
        this.START_WHITE = (-mouseTextureW.getWidth()*5);
        this.spriteR = new Sprite(this.mouseTextureR);
        this.spriteR.setPosition(START_RED, 0);
        this.spriteG = new Sprite(this.mouseTextureG);
        this.spriteG.setPosition(START_GREEN, 0);
        this.spriteW = new Sprite(this.mouseTextureW);
        this.spriteW.setPosition(START_WHITE, 0);
        //init timers
        this.GREEN_TIMER = 0.0f;
        this.RED_TIMER = 0.0f;
        this.WHITE_TIMER = 0.0f;

        //init state
        state = 0;

        b1G = false;
        b1R = false;
        b1W = false;
    }

    @Override
    public void show() {

    }

    private void runAudio() {
        if (runAtBeginning) {
            state = 1;
            runAtBeginning = false;
        }

        if (!theme.isPlaying() && playFlag) {
            theme2.stop();
            theme2.dispose();
            background = new Texture("bulgarian_flag_1800.png");
            theme.play();
        } else {
            if (!theme2.isPlaying() && playtheme2Once) {
                theme2.play();
                playtheme2Once = false;
            }
        }
        theme.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                music.stop();
                if (theme.isPlaying()) {
                    theme.stop();
                }
                playFlag = false;
                state = 4;
                state3FlagOneTime = true;
            }
        });
        if (playFlag && theme.getPosition() >= MUSIC_TIMER && state3FlagOneTime) {
            state = 3;
            state3FlagOneTime = false;
        }
    }

    public void entityUpdate(float delta) {
        if (b1G && b1R && b1W) {
            if (state == 1) {
                state = 2;
            } else if (state == 4) {
                state = 0;
                this.START_GREEN = (-mouseTextureR.getWidth()*3);
                this.START_RED = (-mouseTextureG.getWidth());
                this.START_WHITE = (-mouseTextureW.getWidth()*5);
            }
            GREEN_TIMER = 0;
            RED_TIMER = 0;
            WHITE_TIMER = 0;
            b1G = false;
            b1R = false;
            b1W = false;
        }

        if (state == 1) {//walk to mid
            /**MOVE GREEN*/
            GREEN_TIMER += delta;
            if (GREEN_TIMER >= MOVEMENT_TIMER) {
                //choose move
                int move = ThreadLocalRandom.current().nextInt(4, 6);
                if (Math.abs(START_GREEN - (Gdx.graphics.getWidth()/2)) < 15f) {
                    mouseTextureG = RSG;
                    b1G = true;
                } else {
                    while (move == previousMoveGreen) {//check for duplicate moves
                        move = ThreadLocalRandom.current().nextInt(4, 6);
                    }
                    //choose alternate footing
                    if (previousMoveGreen == 4) {
                        //render right right
                        mouseTextureG = RRG;
                    } else {
                        //render right left
                        mouseTextureG = RLG;
                    }
                    START_GREEN += DISPLACEMENT_VALUE;
                }
                GREEN_TIMER = 0;
                //set the previous move
                previousMoveGreen = move;
            }
            /**MOVE RED*/
            RED_TIMER += delta;
            if (RED_TIMER >= MOVEMENT_TIMER) {
                //choose move
                int move = ThreadLocalRandom.current().nextInt(4, 6);
                if (Math.abs(START_RED - (Gdx.graphics.getWidth()/2 + mouseTextureR.getWidth()*3)) < 15f) {
                    mouseTextureR = RSR;
                    b1R = true;
                } else {
                    while (move == previousMoveRed) {//check for duplicate moves
                        move = ThreadLocalRandom.current().nextInt(4, 6);
                    }
                    //choose alternate footing
                    if (previousMoveRed == 4) {
                        //render right right
                        mouseTextureR = RRR;
                    } else {
                        //render right left
                        mouseTextureR = RLR;
                    }
                    START_RED += DISPLACEMENT_VALUE;
                }
                RED_TIMER = 0;
                //set the previous move
                previousMoveRed = move;
            }
            /**MOVE WHITE*/
            WHITE_TIMER += delta;
            if (WHITE_TIMER >= MOVEMENT_TIMER) {
                //choose move
                int move = ThreadLocalRandom.current().nextInt(4, 6);
                if (Math.abs(START_WHITE - (Gdx.graphics.getWidth()/2 - mouseTextureW.getWidth()*3)) < 15f) {
                    mouseTextureW = RSW;
                    b1W = true;
                } else {
                    while (move == previousMoveWhite) {//check for duplicate moves
                        move = ThreadLocalRandom.current().nextInt(4, 6);
                    }
                    //choose alternate footing
                    if (previousMoveWhite == 4) {
                        //render right right
                        mouseTextureW = RRW;
                    } else {
                        //render right left
                        mouseTextureW = RLW;
                    }
                    START_WHITE += DISPLACEMENT_VALUE;
                }
                WHITE_TIMER = 0;
                //set the previous move
                previousMoveWhite = move;
            }
        } else if (state == 2) {//raise arms randomly
            /**MOVE GREEN*/
            GREEN_TIMER += delta;
            if (GREEN_TIMER >= STATE3_TIMER) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    mouseTextureG = AIR_G;
                } else {
                    mouseTextureG = RSG;
                }
                GREEN_TIMER = 0;
            }
            /**MOVE RED*/
            RED_TIMER += delta;
            if (RED_TIMER >= STATE3_TIMER) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    mouseTextureR = AIR_R;
                } else {
                    mouseTextureR = RSR;
                }
                RED_TIMER = 0;
            }
            /**MOVE WHITE*/
            WHITE_TIMER += delta;
            if (WHITE_TIMER >= STATE3_TIMER) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    mouseTextureW = AIR_W;
                } else {
                    mouseTextureW = RSW;
                }
                WHITE_TIMER = 0;
            }
        } else if (state == 3) {//ALL ARMS UP
            mouseTextureG = AIR_G;
            mouseTextureR = AIR_R;
            mouseTextureW = AIR_W;
        } else if (state == 4) {//walk off the scree
            /**MOVE GREEN*/
            GREEN_TIMER += delta;
            if (GREEN_TIMER >= MOVEMENT_TIMER) {
                //choose move
                int move = ThreadLocalRandom.current().nextInt(4, 6);
                if (START_GREEN > (Gdx.graphics.getWidth() + mouseTextureG.getWidth())) {
                    mouseTextureG = RSG;
                    b1G = true;
                } else {
                    while (move == previousMoveGreen) {//check for duplicate moves
                        move = ThreadLocalRandom.current().nextInt(4, 6);
                    }
                    //choose alternate footing
                    if (previousMoveGreen == 4) {
                        //render right right
                        mouseTextureG = RRG;
                    } else {
                        //render right left
                        mouseTextureG = RLG;
                    }
                    START_GREEN += DISPLACEMENT_VALUE;
                }
                GREEN_TIMER = 0;
                //set the previous move
                previousMoveGreen = move;
            }
            /**MOVE RED*/
            RED_TIMER += delta;
            if (RED_TIMER >= MOVEMENT_TIMER) {
                //choose move
                int move = ThreadLocalRandom.current().nextInt(4, 6);
                if (START_RED > (Gdx.graphics.getWidth() + mouseTextureR.getWidth())) {
                    mouseTextureR = RSR;
                    b1R = true;
                } else {
                    while (move == previousMoveRed) {//check for duplicate moves
                        move = ThreadLocalRandom.current().nextInt(4, 6);
                    }
                    //choose alternate footing
                    if (previousMoveRed == 4) {
                        //render right right
                        mouseTextureR = RRR;
                    } else {
                        //render right left
                        mouseTextureR = RLR;
                    }
                    START_RED += DISPLACEMENT_VALUE;
                }
                RED_TIMER = 0;
                //set the previous move
                previousMoveRed = move;
            }
            /**MOVE WHITE*/
            WHITE_TIMER += delta;
            if (WHITE_TIMER >= MOVEMENT_TIMER) {
                //choose move
                int move = ThreadLocalRandom.current().nextInt(4, 6);
                if (START_WHITE > (Gdx.graphics.getWidth() + mouseTextureW.getWidth())) {
                    mouseTextureW = RSW;
                    b1W = true;
                } else {
                    while (move == previousMoveWhite) {//check for duplicate moves
                        move = ThreadLocalRandom.current().nextInt(4, 6);
                    }
                    //choose alternate footing
                    if (previousMoveWhite == 4) {
                        //render right right
                        mouseTextureW = RRW;
                    } else {
                        //render right left
                        mouseTextureW = RLW;
                    }
                    START_WHITE += DISPLACEMENT_VALUE;
                }
                WHITE_TIMER = 0;
                //set the previous move
                previousMoveWhite = move;
            }
        }
    }

    @Override
    public void render(float delta) {
        this.runAudio();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(background, 0, 0);

        GlyphLayout title = new GlyphLayout(scoreFont, "YOU WON!");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "MAIN MENU");
        GlyphLayout creditsLayout = new GlyphLayout(scoreFont, "CREDITS");
        float mainMenuX = Gdx.graphics.getWidth() / 2 - mainMenuLayout.width / 2;
        float mainMenuY = Gdx.graphics.getHeight() / 1.5f - mainMenuLayout.height / 2;
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
        float creditsX = Gdx.graphics.getWidth() / 2 - creditsLayout.width / 2;
        float creditsY = Gdx.graphics.getHeight() / 1.75f - creditsLayout.height / 2;

        //if main menu
        if (Gdx.input.justTouched()) {
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
        //if credits
        if (Gdx.input.justTouched()) {
            if (touchX > creditsX && touchX < creditsX + creditsLayout.width && touchY > creditsY - creditsLayout.height && touchY < creditsY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new CreditScreen(game));
                return;
            }
        }

        int x = Gdx.graphics.getWidth() - PLAY_BUTTON_WIDTH/4 - 32;
        int y = Gdx.graphics.getHeight() - PLAY_BUTTON_HEIGHT/4 - 8;
        if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH/4 && Gdx.input.getX() > x && game.HEIGHT - Gdx.input.getY() < y + PLAY_BUTTON_HEIGHT/4 && game.HEIGHT - Gdx.input.getY() > y) {
            //check if the mouse was clicked start the game
            if (Gdx.input.justTouched()) {
                playFlag = true;
                runAtBeginning = true;
            }
        }
        game.batch.draw(playSoundTexture,x,y, PLAY_BUTTON_WIDTH/4,PLAY_BUTTON_HEIGHT/4);
        scoreFont.draw(game.batch, title, Gdx.graphics.getWidth() / 2 - title.width / 2, Gdx.graphics.getHeight() - title.height - 15);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);
        scoreFont.draw(game.batch, creditsLayout, creditsX, creditsY);

        this.entityUpdate(delta);

        game.batch.draw(mouseTextureG, START_GREEN, 0);
        game.batch.draw(mouseTextureR, START_RED, 0);
        game.batch.draw(mouseTextureW, START_WHITE, 0);

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

        if (theme2.isPlaying()) {
            theme2.stop();
            theme2.setLooping(false);
        }
    }
}
