package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

public class Level2 implements Screen {

    public final int WIDTH = 1800;
    public final int HEIGHT = 1000;
    private final int TILE_SIZE = 16;
    private final int GAME_WORLD_COLUMNS = 113;
    private final int GAME_WORLD_ROWS = 63;
    private final float X = 32;
    private final float Y = 224;
    private final Texture HELLO_WORLD = new Texture("globe_16.png");
    private final Texture INVISIBLE_BOX = new Texture("black_square_16.png");
    private final Texture EXCEPTION_32 = new Texture("exception_32.png");
    private final Texture EXCEPTION_16 = new Texture("exception_16.png");
    private final Texture SPIKES = new Texture("spikes.png");
    private final float SUCCESS_CONST = 6.488040f;
    private final float PIXELS_IN_METER = 50f;
    private final int VEL_ITER = 6;
    private final int POS_ITER = 2;
    private final float SQUEAK_TIMER = 0.520f;
    private final float ARRAY_START_X = 1424;
    private final float ARRAY_START_Y = Gdx.graphics.getHeight() - 776;
    private final float ZONE2_START = 1424;
    private final float ZONE2_END = 1632;
    private final float ZONE2_Y = Gdx.graphics.getHeight() - 776 + 16*2;
    private final float SPIKE_START_X = 368;
    private final float SPIKE_START_Y = Gdx.graphics.getHeight() - 776;
    private final float EXCEPTION_START_X = 912;
    private final float EXCEPTION_START_Y = Gdx.graphics.getHeight() - 648 - 32;
    private final float EXCEPTION_ZONE = 888;
    private final float EXCEPTION_TIMER = 0.1f;

    private Alex241Intro game;
    private Music theme;
    private Texture wall;
    private Texture grass;
    private PC player;
    private XMLBot xmlBot;
    private Portal portal;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera orthographicCamera;
    private Sound portalSound;
    private Sound squeak;
    private Sound success;
    private Exceptions exception;

    private ArrayList<Catch> catches;
    private Animation<TextureRegion> animation;
    private ArrayList<Float> globePosition;
    private ArrayList<Tile> wallTiles;

    private byte[][] gameWorld;
    private Tile[][] tiles;
    private TextureRegion[] textureRegions;

    private float elapsed;
    private float squeakTimer;
    private float successSoundTimer;
    private float BOX_X;
    private float BOX_Y;
    private float exceptionTimer;

    private boolean playAnimationOnceFlag = true;
    private boolean animationComplete = false;
    private boolean portalSoundComplete = false;
    private boolean squeakFlag = false;
    private boolean squeakOnce = true;
    private boolean dieFlag;
    private boolean stopPlayingMusic;
    private boolean successComplete;
    
    private int worldExceptionCounter;
    private ChatBot chatBot;

    public Level2(Alex241Intro game, ChatBot chatBot) throws FileNotFoundException {
        this.game = game;
        this.chatBot = chatBot;
        this.chatBot.setLevel(2);

        //create camera
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false,(Gdx.graphics.getWidth())/PIXELS_IN_METER, Gdx.graphics.getHeight()/PIXELS_IN_METER);
        wallTiles = new ArrayList<>();

        //setup the world
        world = new World(new Vector2(0f,-9.81f), true);
        this.wall = new Texture("wall.png");
        this.grass = new Texture("grass.png");
        this.gameWorld = new byte[GAME_WORLD_ROWS][GAME_WORLD_COLUMNS];
        File file = new File("tileMap2.txt");
        Scanner scanner = new Scanner(file);
        int counter = 0;
        while (scanner.hasNextLine()) {
            String[] cells = scanner.nextLine().split(" ");
            for (int i = 0; i < cells.length; i++) {
                this.gameWorld[counter][i] = Byte.parseByte(cells[i]);
            }
            counter++;
        }
        this.tiles = new Tile[GAME_WORLD_ROWS][GAME_WORLD_COLUMNS];
        for (byte i = (byte)(GAME_WORLD_ROWS-1); i >= 0; i--) {//up <-> down --- row
            for (byte j = 0; j < (byte)this.tiles[i].length; j++) {//left <-> right --- col
                if (this.gameWorld[i][j] == 1) {//impassable
                    boolean ground = false;
                    if (i < (GAME_WORLD_ROWS-1)) {
                        if (tiles[i+1][j].isImpassible() && ((GAME_WORLD_ROWS-1-i) * TILE_SIZE < Y)) {
                            ground = true;
                        }
                    }
                    tiles[i][j] = new Tile(game,j * TILE_SIZE, (GAME_WORLD_ROWS-1-i) * TILE_SIZE, grass, true, world, ground);
                    wallTiles.add(tiles[i][j]);
                } else {//passable
                    tiles[i][j] = new Tile(game,j * TILE_SIZE, (GAME_WORLD_ROWS-1-i) * TILE_SIZE, wall, false, world, false);
                }
            }
        }

        //set the positions
        BOX_X = tiles[(224/16) + 24][GAME_WORLD_COLUMNS-16].getX();
        BOX_Y = tiles[(224/16) + 24][GAME_WORLD_COLUMNS-16].getY();

        //create ground
        Body groundBody;
        BodyDef groundBodyDef = new BodyDef();
        ChainShape groundShape = new ChainShape();
        FixtureDef groundFixtureDef = new FixtureDef();

        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0,0);
        groundBody = world.createBody(groundBodyDef);

        groundShape.createChain(new Vector2[]{new Vector2((Gdx.graphics.getWidth()-WIDTH)/PIXELS_IN_METER, (192+16*2)/PIXELS_IN_METER), new Vector2((Gdx.graphics.getWidth())/PIXELS_IN_METER, (192+16*2)/PIXELS_IN_METER)});
        groundFixtureDef.shape = groundShape;
        groundBody.createFixture(groundFixtureDef);

        //create invisible box
        Body boxBody;
        BodyDef boxDef = new BodyDef();
        PolygonShape boxShape = new PolygonShape();
        FixtureDef boxFixture = new FixtureDef();
        boxDef.position.set((BOX_X + 16/2)/PIXELS_IN_METER, (BOX_Y + 16/2)/PIXELS_IN_METER);
        boxDef.type = BodyDef.BodyType.StaticBody;
        boxDef.fixedRotation = true;
        boxBody = world.createBody(boxDef);
        boxShape.setAsBox(16/2/PIXELS_IN_METER, 16/2/PIXELS_IN_METER);
        boxFixture.shape = boxShape;
        boxFixture.density = 2.5f;
        boxFixture.friction = 1f;
        boxFixture.restitution = 0.0f;
        boxBody.createFixture(boxFixture);

        //set audio
        this.theme = Gdx.audio.newMusic(Gdx.files.internal("level2.wav"));
//        theme.setVolume(0.25f);
        this.portalSound = Gdx.audio.newSound(Gdx.files.internal("portal2.wav"));
        this.squeak = Gdx.audio.newSound(Gdx.files.internal("squeak.mp3"));
        this.success = Gdx.audio.newSound(Gdx.files.internal("success.mp3"));

        //create animation for portal
        textureRegions = new TextureRegion[9];
        for (int i = 0; i < 9; i++) {
            this.textureRegions[i] = new TextureRegion(new Texture("level2_portal\\frame_" + i + "_delay-0.1s.png"));
        }
        animation = new Animation<TextureRegion>(0.1f, textureRegions);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        //create entities
        player = new PC(this.game, X, Y, world);
        xmlBot = new XMLBot(this.game, this.world);
        portal = new Portal(this.game, Gdx.graphics.getWidth() - (32+16), Y);
        exception = new Exceptions(game, EXCEPTION_START_X, EXCEPTION_START_Y, 32);

        //set timers
        squeakTimer = 0.0f;
        successSoundTimer = 0f;

        //set flags
        dieFlag = false;
        successComplete = false;
        stopPlayingMusic = false;

        //setup globe
        globePosition = new ArrayList<>();

        //setup debug renderer
        debugRenderer = new Box2DDebugRenderer();
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
        chatBot.think(delta, player.getX(), player.getY());

        if (squeakFlag && squeakOnce) {
            squeak.play(1f);
            squeakOnce = false;
        }

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

        if (playAnimationOnceFlag) {
            player.update(delta);
        }

        //COLLISION CODE HERE
        catches = this.player.getCatches();

        //remove list
        ArrayList<SmartException> removeExceptions = new ArrayList<>();
        ArrayList<Catch> removeCatch = new ArrayList<>();

        //check catch and wall collision
        if (!catches.isEmpty()) {
            OuterLoop:
                for (int i = 0; i < catches.size(); i++) {
                    for (int j = 0; j < wallTiles.size(); j++) {
                        if (wallTiles.get(j).getCollisionRect().collidesWith(catches.get(i).getCollisionRect())) {
                            removeCatch.add(catches.get(i));
                            break OuterLoop;
                        }
                    }
                }
        }

        worldExceptionCounter = xmlBot.update(player, delta);

        //update exception
        if (player.getX() + player.getBLOCK_W() > EXCEPTION_ZONE) {
            if (exception.getY() < EXCEPTION_START_Y + exception.getBLOCK_H()*2) {
                exceptionTimer += delta;
                if (exceptionTimer > EXCEPTION_TIMER) {
                    exceptionTimer = 0f;
                    exception.update(0f, 5f);
                }
            }
        }

        //PLAYER ARRAY EXCEPTION COLLISION
        if (player.getX() + player.getBLOCK_W() > ZONE2_START && player.getX() + player.getBLOCK_W() < ZONE2_END && player.getY() < ZONE2_Y) {
            dieFlag = true;
            squeakFlag = true;
        }

        //player fall collision
        if (player.getX() + player.getBLOCK_W() > xmlBot.getZONE1() && player.getX() + player.getBLOCK_W() < xmlBot.getZONE2() && player.getY() < SPIKE_START_Y + 16) {
            dieFlag = true;
            squeakFlag = true;
        }

        //player exception collision
        if (player.getCollisionRect().collidesWith(exception.getCollisionRect())) {
            dieFlag = true;
            squeakFlag = true;
        }

        //check for player portal collision
        if (player.getCollisionRect().collidesWith(portal.getCollisionRect())) {
            //invalid end state with worldExceptionCounter not equalling 0
            if (worldExceptionCounter > 0) {
                dieFlag = true;
                squeakFlag = true;
            }
            if (player.isValidEndState()) {
                //RUN ANIMATION
                if (portalSoundComplete) {
                    portalSoundComplete = false;
                    //advance to next level
                    theme.stop();
                    theme.dispose();
                    this.dispose();
                    try {
                        game.setScreen(new Level3(game, chatBot));
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

                if (animationComplete && successComplete){
                    //play portal sound
                    portalSound.play();

                    //set flags
                    animationComplete = false;
                    portalSoundComplete = true;
                }
            } else {
                dieFlag = true;
                squeakFlag = true;
            }
        }

        //ALSO PUT XML AND CATCH COLLISION HERE
        ArrayList<SmartException> smartExceptions = new ArrayList<>(xmlBot.getExceptions());
        for (int i = 0; i < smartExceptions.size(); i++) {
            for (int j = 0; j < catches.size(); j++) {
                if (smartExceptions.get(i).getRect().collidesWith(catches.get(j).getCollisionRect())) {
                    removeCatch.add(catches.get(j));
                    removeExceptions.add(smartExceptions.get(i));
                }
            }
        }
        
        //player smart exception collision
        for (int i = 0; i < smartExceptions.size(); i++) {
            if (smartExceptions.get(i).getRect().collidesWith(player.getCollisionRect())) {
                dieFlag = true;
                squeakFlag = true;
            }
        }
        



        /////////////////////////////////////////////////DEBUGGING HERE!
//        if (Gdx.input.justTouched()) {
//            System.out.println("X\t" + Gdx.input.getX());
//            System.out.println("Y\t" + Gdx.input.getY());
//        }



        //remove the catches & smart exceptions
        catches.removeAll(removeCatch);
        smartExceptions.removeAll(removeExceptions);

        //set the smart exceptions in the xml bot
        xmlBot.setExceptions(smartExceptions);
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

        //render the xml bot
        xmlBot.renderXMLBot();

        //render the exception
        exception.renderException();

        //render map
        for (byte i = 0; i < this.gameWorld.length; i++) {
            for (byte j = 0; j < this.gameWorld[0].length; j++) {
                //render the tiles
                if (tiles[i][j].isImpassible()) {
                    game.batch.draw(tiles[i][j].getTexture(), tiles[i][j].getX(), tiles[i][j].getY(), tiles[i][j].getGRID_PIXEL_SIZE(), tiles[i][j].getGRID_PIXEL_SIZE());
                }
            }
        }

        //render invisible box
        game.batch.draw(INVISIBLE_BOX, BOX_X, BOX_Y);

        //render portal
        game.batch.draw(animation.getKeyFrame(elapsed), Gdx.graphics.getWidth() - (64+16), Y+16);

        //render world if true
        if (!globePosition.isEmpty()) {
            game.batch.draw(HELLO_WORLD, globePosition.get(0), globePosition.get(1));
        }

        //RENDER CATCHES
        for (Catch c : catches) {
            c.renderCatch();
        }

        //render exceptions
        if (player.getX() + player.getBLOCK_W() > ZONE2_START && player.getX() + player.getBLOCK_W() < ZONE2_END && player.getY() < ZONE2_Y) {
            for (int i = 0; i < 14; i++) {
                game.batch.draw(EXCEPTION_16, ARRAY_START_X + (i * EXCEPTION_16.getWidth()), ARRAY_START_Y);
            }
        }

        //render spikes
        for (int i = 0; i < 58; i++) {
            game.batch.draw(SPIKES, SPIKE_START_X + (i * SPIKES.getWidth()), SPIKE_START_Y);
        }

        //render exceptions
        xmlBot.renderAllExceptions();

        //render player
        this.player.renderPC();

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