package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.entities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Level1 implements Screen {


    public final int WIDTH = 1800;
    public final int HEIGHT = 1000;
    private final int TILE_SIZE = 16;
    private final int GAME_WORLD_COLUMNS = 113;
    private final int GAME_WORLD_ROWS = 63;
    private final float X = 32;
    private final float Y = (13 - 1) * 16 + 16;//bottom wall rows * wall_size

    private final Texture HELLO_WORLD = new Texture("globe_16.png");

    private Alex241Intro game;
    private Music theme;
    private BitmapFont gridNumberFont;

    private byte[][] gameWorld;
    private Tile[][] tiles;
    private Texture wall;
    private Texture grass;

    private NullBot nullBot;
    private PC player;
    private Portal portal;

    private ArrayList<Exceptions> exceptions1;
    private ArrayList<Exceptions> exceptions2;
    private ArrayList<Exceptions> exceptions3;

    private ArrayList<Catch> catches;

    private final float PIXELS_IN_METER = 50f;
    private final int VEL_ITER = 6;
    private final int POS_ITER = 2;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera orthographicCamera;


    private TextureRegion[] textureRegions;
    private Animation<TextureRegion> animation;
    private float elapsed;



    private Sound portalSound;
    private Sound squeak;
    private Sound success;
    private boolean playAnimationOnceFlag = true;
    private boolean animationComplete = false;
    private boolean portalSoundComplete = false;

    private boolean squeakFlag = false;
    private boolean squeakOnce = true;
    private float squeakTimer = 0.0f;
    private final float SQUEAK_TIMER = 0.520f;

    private boolean dieFlag;



    private final float SUCCESS_CONST = 6.488040f;
    private float successSoundTimer;
    private boolean successComplete;



    private ArrayList<Float> globePosition;
    private boolean stopPlayingMusic;

    private ChatBot chatBot;


    public Level1(Alex241Intro game) throws FileNotFoundException {
        this.game = game;
        //create camera
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false,(Gdx.graphics.getWidth())/PIXELS_IN_METER, Gdx.graphics.getHeight()/PIXELS_IN_METER);

        world = new World(new Vector2(0f,-9.81f), true);
        this.wall = new Texture("wall.png");
        this.grass = new Texture("grass.png");
        this.gameWorld = new byte[GAME_WORLD_COLUMNS][GAME_WORLD_ROWS];
        FileHandle file = Gdx.files.internal("tileMap1.txt");
        Scanner scanner = new Scanner(file.readString());
        int counter = 0;
        while (scanner.hasNextLine()) {
            String[] cells = scanner.nextLine().split(" ");
            for (int i = 0; i < cells.length; i++) {
                this.gameWorld[i][counter] = Byte.parseByte(cells[i]);
            }
            counter++;
        }
        this.tiles = new Tile[this.gameWorld.length][this.gameWorld[0].length];
        for (byte i = 0; i < (byte)this.tiles.length; i++) {
            for (byte j = 0; j < (byte)this.tiles[0].length; j++) {
                if (this.gameWorld[i][j] == 1) {//impassable
                    boolean ground = false;
                    if (i > 0) {
                        if (tiles[i-1][j].isImpassible() && (j * TILE_SIZE < Y)) {
                            ground = true;
                        }
                    }
                    tiles[i][j] = new Tile(game,i * TILE_SIZE, j * TILE_SIZE, grass, true, world, ground);
                } else {//passable
                    tiles[i][j] = new Tile(game,i * TILE_SIZE, j * TILE_SIZE, wall, false, world, false);
                }
            }
        }

        //create ground
        Body groundBody;
        BodyDef groundBodyDef = new BodyDef();
        ChainShape groundShape = new ChainShape();
        FixtureDef groundFixtureDef = new FixtureDef();

        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0,0);
        groundBody = world.createBody(groundBodyDef);

        groundShape.createChain(new Vector2[]{new Vector2((Gdx.graphics.getWidth()-WIDTH)/PIXELS_IN_METER, (192)/PIXELS_IN_METER), new Vector2((Gdx.graphics.getWidth())/PIXELS_IN_METER, (192)/PIXELS_IN_METER)});
        groundFixtureDef.shape = groundShape;
        groundBody.createFixture(groundFixtureDef);



        this.theme = Gdx.audio.newMusic(Gdx.files.internal("level1_1.mp3"));
        theme.setVolume(0.1f);
        this.portalSound = Gdx.audio.newSound(Gdx.files.internal("portal2.mp3"));
        this.squeak = Gdx.audio.newSound(Gdx.files.internal("squeak.mp3"));
        this.success = Gdx.audio.newSound(Gdx.files.internal("success.mp3"));


        textureRegions = new TextureRegion[240];
        for (int i = 0; i < 240; i++) {
            String formatted = String.format("%03d", i);
            this.textureRegions[i] = new TextureRegion(new Texture("level_1\\frame_" + formatted + "_delay-0.03s.png"));
        }
        animation = new Animation<TextureRegion>(0.03f, textureRegions);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        this.gridNumberFont = new BitmapFont(Gdx.files.internal("fonts\\score2.fnt"));

        player = new PC(this.game, X, Y, world);
        nullBot = new NullBot(this.game, this.gridNumberFont, world);
        portal = new Portal(this.game, Gdx.graphics.getWidth() - (16), Y);

        debugRenderer = new Box2DDebugRenderer();


        dieFlag = false;
        successSoundTimer = 0f;
        globePosition = new ArrayList<>();
        successComplete = false;
        stopPlayingMusic = false;

        chatBot = new ChatBot(true, 1);
    }

    public void update() {
        //handle user input

        //update world
        world.step(Gdx.graphics.getDeltaTime(), VEL_ITER, POS_ITER);

        //update gamecam with correct coordinates after changes

        //tailor the renderer to draw only what the camera views in the game world
    }

    @Override
    public void show() {
        if (!stopPlayingMusic) {
            if (!theme.isPlaying()) {
                theme.play();
                theme.setLooping(true);
            }
        }
    }

    private void update(float delta) {
        //update chat bot
        chatBot.think(delta, player.getX(), player.getY());

    if (dieFlag) {
        squeakTimer += delta;
        chatBot.setAlive(false);
        chatBot.stopAudio();
        if (squeakTimer > SQUEAK_TIMER) {
            theme.stop();
            theme.dispose();
            this.dispose();
            game.setScreen(new GameOverScreen(game, chatBot));
        }
    }



    if (squeakFlag && squeakOnce) {
        squeak.play(1f);
        squeakOnce = false;
    }

    if (playAnimationOnceFlag) {
        player.update(delta);
    }

        nullBot.update(player, delta);

        //COLLISION CODE HERE
        exceptions1 = nullBot.getExceptions1();
        exceptions2 = nullBot.getExceptions2();
        exceptions3 = nullBot.getExceptions3();
        catches = this.player.getCatches();

        //remove list
        ArrayList<Exceptions> removeExceptions = new ArrayList<>();
        ArrayList<Catch> removeCatch = new ArrayList<>();

        //check for player exception collision & check for wall exception collision & COLLISION FOR CATCHES AND EXCEPTIONS
        //left
        for (Exceptions e : exceptions1) {
            if (e.getCollisionRect().collidesWith(player.getCollisionRect()) || e.getX() <= TILE_SIZE*1 || (e.getX() + e.getBLOCK_W()) >= TILE_SIZE*(GAME_WORLD_COLUMNS - 1)) {
                dieFlag = true;
                squeakFlag = true;
            }
            for (Catch c : catches) {
                if (c.getCollisionRect().collidesWith(e.getCollisionRect())) {
                    removeExceptions.add(e);
                    removeCatch.add(c);
                }
            }
        }
        exceptions1.removeAll(removeExceptions);
        catches.removeAll(removeCatch);

        //up
        for (Exceptions e : exceptions2) {
            if (e.getCollisionRect().collidesWith(player.getCollisionRect()) || e.getX() <= TILE_SIZE*1 || (e.getX() + e.getBLOCK_W()) >= TILE_SIZE*(GAME_WORLD_COLUMNS - 1)) {
                dieFlag = true;
                squeakFlag = true;
            }
            for (Catch c : catches) {
                if (c.getCollisionRect().collidesWith(e.getCollisionRect())) {
                    removeExceptions.add(e);
                    removeCatch.add(c);
                }
            }
        }
        exceptions2.removeAll(removeExceptions);
        catches.removeAll(removeCatch);

        //right
        for (Exceptions e : exceptions3) {
            if (e.getCollisionRect().collidesWith(player.getCollisionRect()) || e.getX() <= TILE_SIZE*1 || (e.getX() + e.getBLOCK_W()) >= TILE_SIZE*(GAME_WORLD_COLUMNS - 1)) {
                dieFlag = true;
                squeakFlag = true;
            }
            for (Catch c : catches) {
                if (c.getCollisionRect().collidesWith(e.getCollisionRect())) {
                    removeExceptions.add(e);
                    removeCatch.add(c);
                }
            }
        }
        exceptions3.removeAll(removeExceptions);
        catches.removeAll(removeCatch);

        //check for player portal collision
        if (player.getCollisionRect().collidesWith(portal.getCollisionRect())) {
            if (player.isValidEndState()) {
                //RUN ANIMATION
                if (portalSoundComplete) {
                    portalSoundComplete = false;
                    //advance to next level
                    theme.stop();
                    theme.dispose();
                    this.dispose();
                    try {
                        game.setScreen(new Level2(game, chatBot));
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (playAnimationOnceFlag) {
                        if (theme.isPlaying()) {
                            theme.stop();
                            theme.setLooping(false);
                        }

                        stopPlayingMusic = true;
                        //play success sound
                        success.play();
                        playAnimationOnceFlag = false;
                        successSoundTimer += delta;
                    } else {
                        successSoundTimer += delta;
                        if (successSoundTimer > SUCCESS_CONST) {
                            successComplete = true;
                        }
                    }

                    animationComplete = player.playEndAnimation(delta, globePosition);
                }


//                System.out.println("animaiton\t" + animationComplete);
//                System.out.println("sucess sound\t" + successComplete);

                if (animationComplete && successComplete){
                    //play portal sound
                    portalSound.play();

//                    System.out.println("ANIMATION COMPLETE!!!!!!!!!!!! and sound is also complete!");

                    //set flags
                    animationComplete = false;
                    portalSoundComplete = true;
                }
            } else {
                dieFlag = true;
                squeakFlag = true;
            }
        }

        //ALSO PUT NULLBOT AND CATCH COLLISION HERE
        for (Catch c : catches) {
            if (c.getCollisionRect().collidesWith(nullBot.getCollisionRect())) {
                removeCatch.add(c);
            }
        }
        catches.removeAll(removeCatch);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();

        //update world
        if (world != null) {
            this.update(delta);
            this.update();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        //render background
        game.batch.draw(animation.getKeyFrame(elapsed), 16f, 192);

        //render map
        for (byte i = 0; i < this.gameWorld.length; i++) {
            for (byte j = 0; j < this.gameWorld[0].length; j++) {
                //render the tiles
                if (tiles[i][j].isImpassible()) {
                    game.batch.draw(tiles[i][j].getTexture(), tiles[i][j].getX(), tiles[i][j].getY(), tiles[i][j].getGRID_PIXEL_SIZE(), tiles[i][j].getGRID_PIXEL_SIZE());
                }
            }
        }

        //render bot
        this.nullBot.renderNullBot();

        //render player
        this.player.renderPC();

        //render world if true
        if (!globePosition.isEmpty()) {
            game.batch.draw(HELLO_WORLD, globePosition.get(0), globePosition.get(1));
        }

        //RENDER CATCHES
        for (Catch c : catches) {
            c.renderCatch();
        }

        //render exceptions
        //left
        for (Exceptions e : exceptions1) {
            e.renderException();
        }
        //up
        for (Exceptions e : exceptions2) {
            e.renderException();
        }
        //right
        for (Exceptions e : exceptions3) {
            e.renderException();
        }

        //render portal
        portal.renderPortal();

        //render BOX2D
//        debugRenderer.render(world, orthographicCamera.combined);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.update();
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
        debugRenderer.dispose();
        if (theme.isPlaying()) {
            theme.stop();
            theme.setLooping(false);
        }
    }
}