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

public class Level3 implements Screen {

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
    private final Texture STACK_592 = new Texture("STACKOVERFLOW_289.png");
    private final Texture STACK_3 = new Texture("STACKOVERFLOW_3.png");
    private final Texture STACK_2 = new Texture("STACKOVERFLOW_2.png");
    private final Texture STACK_1 = new Texture("STACKOVERFLOW_1.png");
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
    private final float DOOR_TIMER = 3f;
    private final float BACK_PORTAL_W;
    private final float BACK_PORTAL_H;
    private final float LIMIT_Y = Gdx.graphics.getHeight() - 464;

    private Alex241Intro game;
    private Music theme;
    private BitmapFont gridNumberFont;
    private Texture wall;
    private Texture grass;
    private PC player;
    private Portal portal;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera orthographicCamera;
    private Sound portalSound;
    private Sound squeak;
    private Sound success;
    private Exceptions exception;
    private Texture background1;
    private Texture background2;
    private Texture background3;
    private BinaryTreeBot binaryTreeBot;
    private AnimalBot animalBot;
    private NPCBot npcBot;
    private PCBot pcBot;
    private Creature creature;
    private Texture stacktexture;
    private Body stackBody;
    private BodyDef stackBodyDef;
    private Exceptions trapException;

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
    private float doorUpX;
    private float doorUpY;
    private float doorDownX;
    private float doorDownY;
    private float doorTimer = 0.0f;
    private float doorUpFinalY;
    private float doorDownFinalY;
    private float stackX;
    private float trapTimer;

    private boolean playAnimationOnceFlag = true;
    private boolean animationComplete = false;
    private boolean portalSoundComplete = false;
    private boolean squeakFlag = false;
    private boolean squeakOnce = true;
    private boolean dieFlag;
    private boolean stopPlayingMusic;
    private boolean successComplete;
    private boolean b1;
    private boolean b2;
    private boolean b3;
    private boolean b4;
    private boolean r1;
    private boolean r2;
    private boolean r3;
    private boolean r4;
    private boolean trapAnimation;
    private boolean exceptionTrapFlag;
    private boolean createTrapException;
    private boolean youWonFlag;

    private int worldExceptionCounter;
    private int stackPiecesToBuild;

    private final float INITIAL_CREATURE_X = 1364;
    private final float INITIAL_CREATURE_y = Gdx.graphics.getHeight() - 776;
    private final float TRAP_TIMER = 1f;
    private final float MAX_NEWTONS = 100f;
    private final float TRAP_END_X = 118f;

    private int creatureHealth;
    private int animalHealth;
    private int PCHealth;
    private int NPCHealth;

    private ChatBot chatBot;
    private String dirSlash;

    public Level3(Alex241Intro game, ChatBot chatBot, String dirSlash) throws FileNotFoundException {
        this.game = game;
        this.dirSlash = dirSlash;
        this.chatBot = chatBot;
        this.chatBot.setLevel(3);
        //create camera
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false,(Gdx.graphics.getWidth())/PIXELS_IN_METER, Gdx.graphics.getHeight()/PIXELS_IN_METER);
        wallTiles = new ArrayList<>();

        //setup the world
        world = new World(new Vector2(0f,-9.81f), true);
        this.wall = new Texture("wall.png");
        this.grass = new Texture("grass.png");
        this.gameWorld = new byte[GAME_WORLD_ROWS][GAME_WORLD_COLUMNS];
        FileHandle file = Gdx.files.internal("tileMap3.txt");
        Scanner scanner = new Scanner(file.readString());
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



        //set audio
        this.theme = Gdx.audio.newMusic(Gdx.files.internal("final_level.mp3"));
        theme.setVolume(0.1f);
        this.portalSound = Gdx.audio.newSound(Gdx.files.internal("portal2.mp3"));
        this.squeak = Gdx.audio.newSound(Gdx.files.internal("squeak.mp3"));
        this.success = Gdx.audio.newSound(Gdx.files.internal("success.mp3"));

        //set entity health
        creatureHealth = 5;
        animalHealth = 3;
        PCHealth = 3;
        NPCHealth = 3;

        //create the animation for background
        this.background1 = new Texture("final_door.gif");
        this.background2 = new Texture("top_door.png");
        this.background3 = new Texture("door_bottom.png");
        textureRegions = new TextureRegion[82];
        for (int i = 0; i < 82; i++) {
            String formatted = String.format("%02d", i);
            this.textureRegions[i] = new TextureRegion(new Texture("final_background_portal" + dirSlash + "frame_" + formatted + "_delay-0.05s.png"));
        }
        BACK_PORTAL_W = textureRegions[0].getRegionWidth();
        BACK_PORTAL_H = textureRegions[0].getRegionHeight();
        animation = new Animation<TextureRegion>(0.05f, textureRegions);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        //create entities
        player = new PC(this.game, X, Y, world, dirSlash);
        portal = new Portal(this.game, Gdx.graphics.getWidth() - (16), Y+16*8);
        binaryTreeBot = new BinaryTreeBot(this.game, world);
        creature = new Creature(this.game, new Vector2(INITIAL_CREATURE_X, INITIAL_CREATURE_y));
        pcBot = new PCBot(this.game, new Vector2(INITIAL_CREATURE_X, INITIAL_CREATURE_y + 64));
        npcBot = new NPCBot(this.game, new Vector2(INITIAL_CREATURE_X + 64, INITIAL_CREATURE_y + 64));
        animalBot = new AnimalBot(this.game, new Vector2(INITIAL_CREATURE_X + 64*2, INITIAL_CREATURE_y + 64));


        //set timers
        squeakTimer = 0.0f;
        successSoundTimer = 0f;
        trapTimer = 0f;

        //set flags
        dieFlag = false;
        successComplete = false;
        stopPlayingMusic = false;
        b1 = true;
        b2 = true;
        b3 = true;
        b4 = true;
        r1 = true;
        r2 = true;
        r3 = true;
        r4 = true;
        trapAnimation = false;
        exceptionTrapFlag = false;
        createTrapException = true;
        youWonFlag = false;

        //setup globe
        globePosition = new ArrayList<>();

        //setup debug renderer
        debugRenderer = new Box2DDebugRenderer();

        doorUpX = 420;
        doorDownX = 412;
        doorUpY = Gdx.graphics.getHeight() - 449;
        doorDownY = Gdx.graphics.getHeight() - 449 - 306;
        doorUpFinalY = doorUpY + 306;
        doorDownFinalY = doorDownY;
        stackX = 1598f;
        stackPiecesToBuild = 0;

//        binaryTreeBot.printPositions();
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

        if (squeakFlag && squeakOnce && !youWonFlag) {
            squeak.play(1f);
            squeakOnce = false;
        }

        if (dieFlag && !youWonFlag) {
            squeakTimer += delta;
            chatBot.setAlive(false);
            chatBot.stopAudio();
            if (squeakTimer > SQUEAK_TIMER) {
                theme.stop();
                theme.dispose();
                this.dispose();
                game.setScreen(new GameOverScreen(game, chatBot, dirSlash));
            }
        }

        if (exceptionTrapFlag) {
            //create
            if (createTrapException) {
                trapException = new Exceptions(game, player.getX()+player.getBLOCK_W()/2, Gdx.graphics.getHeight(), 32);
                createTrapException = false;
            } else {//move
                trapException.autoDestroy(player, delta);
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
        //remove the catches & smart exceptions
        catches.removeAll(removeCatch);

        //player and trap collision
        if (trapException != null) {
            if (player.getCollisionRect().collidesWith(trapException.getCollisionRect())) {
                dieFlag = true;
                squeakFlag = true;
            }
        }
        
        //check for entity collision w/ catch
        boolean wasCollisionFlag1 = false;
        boolean wasCollisionFlag2 = false;
        boolean wasCollisionFlag3 = false;
        boolean wasCollisionFlag4 = false;
        for (int i = 0; i < catches.size(); i++) {
            if (creatureHealth > 0) {
                if (catches.get(i).getCollisionRect().collidesWith(creature.getRect())) {
                    removeCatch.add(catches.get(i));
                    wasCollisionFlag1 = true;
                }
            }
            if (PCHealth > 0) {
                if (catches.get(i).getCollisionRect().collidesWith(pcBot.getRect())) {
                    removeCatch.add(catches.get(i));
                    wasCollisionFlag2 = true;
                }
            }
            if (NPCHealth > 0) {
                if (catches.get(i).getCollisionRect().collidesWith(npcBot.getRect())) {
                    removeCatch.add(catches.get(i));
                    wasCollisionFlag3 = true;
                }
            }
            if (animalHealth > 0) {
                if (catches.get(i).getCollisionRect().collidesWith(animalBot.getRect())) {
                    removeCatch.add(catches.get(i));
                    wasCollisionFlag4 = true;
                }
            }
        }
        //remove the catches & smart exceptions
        catches.removeAll(removeCatch);

        //check for entity collision w/ player
        if (creatureHealth > 0) {
            if (player.getCollisionRect().collidesWith(creature.getRect())) {
                dieFlag = true;
                squeakFlag = true;
            }
        }
        if (PCHealth > 0) {
            if (player.getCollisionRect().collidesWith(pcBot.getRect())) {
                dieFlag = true;
                squeakFlag = true;
            }
        }
        if (NPCHealth > 0) {
            if (player.getCollisionRect().collidesWith(npcBot.getRect())) {
                dieFlag = true;
                squeakFlag = true;
            }
        }
        if (animalHealth > 0) {
            if (player.getCollisionRect().collidesWith(animalBot.getRect())) {
                dieFlag = true;
                squeakFlag = true;
            }
        }
        //PUT THE ENTITY UPDATE CODE HERE
        worldExceptionCounter = binaryTreeBot.update(player, delta);

        //check if any of the entities have been destroyed
        if (creatureHealth <= 0) {
            if (b1) {
                ++stackPiecesToBuild;
                b1 = false;
            }
        } else {
            creatureHealth = creature.update(player, wasCollisionFlag1);
        }
        if (PCHealth <= 0) {
            if (b2) {
                ++stackPiecesToBuild;
                b2 = false;
            }
        } else {
            PCHealth = pcBot.update(player, wasCollisionFlag2);
        }
        if (NPCHealth <= 0) {
            if (b3) {
                ++stackPiecesToBuild;
                b3 = false;
            }
        } else {
            NPCHealth = npcBot.update(player, wasCollisionFlag3);
        }
        if (animalHealth <= 0) {
            if (b4) {
                ++stackPiecesToBuild;
                b4 = false;
            }
        } else {
            animalHealth = animalBot.update(player, wasCollisionFlag4);
        }
        //build the stack
        if (stackPiecesToBuild == 1) {
            if (r1) {
                r1 = false;
                stacktexture = STACK_1;

                stackBodyDef = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef stackFixture = new FixtureDef();
                stackBodyDef.position.set(stackX/PIXELS_IN_METER, (Gdx.graphics.getHeight()-776)/PIXELS_IN_METER);
                stackBodyDef.type = BodyDef.BodyType.StaticBody;
                stackBodyDef.fixedRotation = true;
                stackBody = world.createBody(stackBodyDef);

                polygonShape.setAsBox(stacktexture.getWidth()/2/PIXELS_IN_METER, stacktexture.getHeight()/2/PIXELS_IN_METER);
                stackFixture.shape = polygonShape;
                stackFixture.density = 10f;
                stackFixture.friction = 0.25f;
                stackFixture.restitution = 0f;
                stackBody.createFixture(stackFixture);
            }
            //player dies if trying to pass
            if (player.getX()+player.getBLOCK_W() > stackX) {
                dieFlag = true;
                squeakFlag = true;
            }
        } else if (stackPiecesToBuild == 2) {
            if (r2) {
                r2 = false;
                stacktexture = STACK_2;

                world.destroyBody(stackBody);

                stackBodyDef = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef stackFixture = new FixtureDef();
                stackBodyDef.position.set(stackX/PIXELS_IN_METER, (Gdx.graphics.getHeight()-776)/PIXELS_IN_METER);
                stackBodyDef.type = BodyDef.BodyType.StaticBody;
                stackBodyDef.fixedRotation = true;
                stackBody = world.createBody(stackBodyDef);

                polygonShape.setAsBox(stacktexture.getWidth()/2/PIXELS_IN_METER, stacktexture.getHeight()/2/PIXELS_IN_METER);
                stackFixture.shape = polygonShape;
                stackFixture.density = 10f;
                stackFixture.friction = 0.25f;
                stackFixture.restitution = 0f;
                stackBody.createFixture(stackFixture);
            }
            //player dies if trying to pass
            if (player.getX()+player.getBLOCK_W() > stackX) {
                dieFlag = true;
                squeakFlag = true;
            }
        } else if (stackPiecesToBuild == 3) {
            if (r3) {
                r3 = false;
                stacktexture = STACK_3;

                world.destroyBody(stackBody);

                stackBodyDef = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef stackFixture = new FixtureDef();
                stackBodyDef.position.set(stackX/PIXELS_IN_METER, (Gdx.graphics.getHeight()-776)/PIXELS_IN_METER);
                stackBodyDef.type = BodyDef.BodyType.StaticBody;
                stackBodyDef.fixedRotation = true;
                stackBody = world.createBody(stackBodyDef);

                polygonShape.setAsBox(stacktexture.getWidth()/2/PIXELS_IN_METER, stacktexture.getHeight()/2/PIXELS_IN_METER);
                stackFixture.shape = polygonShape;
                stackFixture.density = 10f;
                stackFixture.friction = 0.25f;
                stackFixture.restitution = 0f;
                stackBody.createFixture(stackFixture);
            }
            //player dies if trying to pass
            if (player.getX()+player.getBLOCK_W() > stackX) {
                dieFlag = true;
                squeakFlag = true;
            }
        } else if (stackPiecesToBuild == 4) {
            if (r4) {
                r4 = false;
                stacktexture = STACK_592;

                world.destroyBody(stackBody);

                stackBodyDef = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef stackFixture = new FixtureDef();
                stackBodyDef.position.set(stackX/PIXELS_IN_METER, (Gdx.graphics.getHeight()-776 + 10)/PIXELS_IN_METER);
                stackBodyDef.type = BodyDef.BodyType.DynamicBody;
                stackBodyDef.fixedRotation = true;
                stackBody = world.createBody(stackBodyDef);

                polygonShape.setAsBox(stacktexture.getWidth()/2/PIXELS_IN_METER, stacktexture.getHeight()/2/PIXELS_IN_METER);
                stackFixture.shape = polygonShape;
                stackFixture.density = 2.5f;
                stackFixture.friction = 0.25f;
                stackFixture.restitution = 0f;
                stackBody.createFixture(stackFixture);
            }
            //PLAYER TRIGGERS TRAP ANIMATION TO CRUSH THE PLAYER
            trapAnimation = true;
        }

        //play trap animation
        if (trapAnimation) {
            stackX = stackBody.getPosition().x*PIXELS_IN_METER - stacktexture.getWidth()/2;
            if (stackX <= TRAP_END_X && player.getY() < LIMIT_Y) {
                dieFlag = true;
                squeakFlag = true;
            } else if (stackX <= TRAP_END_X && player.getY() >= LIMIT_Y) {
                exceptionTrapFlag = true;
            }
            trapTimer += delta;
            if (trapTimer > TRAP_TIMER) {
                trapTimer = 0;
                stackBody.applyLinearImpulse(new Vector2(-MAX_NEWTONS, 0), stackBody.getWorldCenter(), true);
            }
        }

        //check for catch exception collision
        ArrayList<SmartException> smartExceptions = new ArrayList<>(binaryTreeBot.getExceptions());
        for (int i = 0; i < smartExceptions.size(); i++) {
            for (int j = 0; j < catches.size(); j++) {
                if (smartExceptions.get(i).getRect().collidesWith(catches.get(j).getCollisionRect())) {
                    removeCatch.add(catches.get(j));
                    removeExceptions.add(smartExceptions.get(i));
                }
            }
        }
        //remove the catches & smart exceptions
        catches.removeAll(removeCatch);

        //check for player exception collision
        //player smart exception collision
        for (int i = 0; i < smartExceptions.size(); i++) {
            if (smartExceptions.get(i).getRect().collidesWith(player.getCollisionRect())) {
                dieFlag = true;
                squeakFlag = true;
            }
        }


        //check for player portal collision
        if (player.getCollisionRect().collidesWith(portal.getCollisionRect())) {
            //invalid end state with worldExceptionCounter not equalling 0
            if (worldExceptionCounter > 0) {
                dieFlag = true;
                squeakFlag = true;
            }
            if (player.isValidEndState()) {
                youWonFlag = true;
                //RUN ANIMATION
                if (portalSoundComplete) {
                    portalSoundComplete = false;
                    //advance to next level
                    theme.stop();
                    theme.dispose();
                    this.dispose();
                    game.setScreen(new GameWonScreen(game, dirSlash));
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

        //door game over state
        doorTimer += delta;
        if (doorTimer > DOOR_TIMER) {
            doorTimer = 0.0f;
            boolean portalOpen1 = false;
            boolean portalOpen2 = false;
            if (doorUpY < doorUpFinalY) {
                //update
                doorUpY += 10;
            } else {
                portalOpen1 = true;
            }
            if (doorDownY + background3.getHeight() > doorDownFinalY) {
                //update
                doorDownY -= 10;
            } else {
                portalOpen2 = true;
            }

            if (portalOpen1 && portalOpen2) {
                dieFlag = true;
                squeakFlag = true;
            }
        }


        /////////////////////////////////////////////////DEBUGGING HERE!
//        if (Gdx.input.justTouched()) {
//            System.out.println("X\t" + Gdx.input.getX());
//            System.out.println("Y\t" + Gdx.input.getY());
//        }

        //remove the smart exceptions
        smartExceptions.removeAll(removeExceptions);

        //set the smart exceptions in the binarytree bot
        binaryTreeBot.setExceptions(smartExceptions);
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();

        //update world
        if (world != null) {
            this.update(delta);
            this.update();
        }

//        //render the connections if needed
//        binaryTreeBot.renderAllConnections();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();


        //render background
        game.batch.draw(animation.getKeyFrame(elapsed), Gdx.graphics.getWidth()/2 - BACK_PORTAL_W/2 + 64, Gdx.graphics.getHeight()/2 - BACK_PORTAL_H/2);

//        //render exceptions
//        binaryTreeBot.renderAllExceptions();

        //render background
        game.batch.draw(background2, doorUpX, doorUpY);//top
        game.batch.draw(background3,  doorDownX, doorDownY);//bottom
        game.batch.draw(background1, 0, 0);

        //render map
        for (byte i = 0; i < this.gameWorld.length; i++) {
            for (byte j = 0; j < this.gameWorld[0].length; j++) {
                //render the tiles
                if (tiles[i][j].isImpassible()) {
                    game.batch.draw(tiles[i][j].getTexture(), tiles[i][j].getX(), tiles[i][j].getY(), tiles[i][j].getGRID_PIXEL_SIZE(), tiles[i][j].getGRID_PIXEL_SIZE());
                }
            }
        }

        //render world if true
        if (!globePosition.isEmpty()) {
            game.batch.draw(HELLO_WORLD, globePosition.get(0), globePosition.get(1));
        }

        //RENDER CATCHES
        for (Catch c : catches) {
            c.renderCatch();
        }

        //render portal
        portal.renderPortal();

        //render stack
        if (stackPiecesToBuild == 1) {
            game.batch.draw(stacktexture, stackX, (Gdx.graphics.getHeight()-776));
        } else if (stackPiecesToBuild == 2) {
            game.batch.draw(stacktexture, stackX, (Gdx.graphics.getHeight()-776));
        } else if (stackPiecesToBuild == 3) {
            game.batch.draw(stacktexture, stackX, (Gdx.graphics.getHeight()-776));
        } else if (stackPiecesToBuild == 4) {
            game.batch.draw(stacktexture, stackX, stackBody.getPosition().y*PIXELS_IN_METER - stacktexture.getHeight()/2);
        }

        //render entities
//        System.out.println("CREATURE =\t" + creatureHealth);
//        System.out.println("PC =\t" + PCHealth);
//        System.out.println("NPC =\t" + NPCHealth);
//        System.out.println("ANIMAL =\t" + animalHealth);
        if (creatureHealth > 0) {
            creature.render(game.batch);
        }
        if (PCHealth > 0) {
            pcBot.render(game.batch);
        }
        if (NPCHealth > 0) {
            npcBot.render(game.batch);
        }
        if (animalHealth > 0) {
            animalBot.render(game.batch);
        }

        if (trapException != null) {
            trapException.renderException();
        }

        //render player
        this.player.renderPC();

        //render BOX2D
//        debugRenderer.render(world, orthographicCamera.combined);

        //render exceptions
        binaryTreeBot.renderAllExceptions();

        game.batch.end();

        //render the connections if needed
        binaryTreeBot.renderAllConnections();
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