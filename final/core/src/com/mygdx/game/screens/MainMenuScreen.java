package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.entities.ChatBot;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MainMenuScreen implements Screen {

    public final int WIDTH = 1800;
    public final int HEIGHT = 1000;
    private final int PLAY_BUTTON_WIDTH = 105;
    private final int PLAY_BUTTON_HEIGHT = 40;
    private final int PLAY_BUTTON_Y = Gdx.graphics.getHeight() - 150;
    private final float MOVEMENT_TIMER = 0.1f;
    private final float PIXELS_IN_METER = 50f;
    private final int CHARACTER_WIDTH = 88;
    private final int CHARACTER_HEIGHT = 64;
    private final float GROUND_POS = -0.06f;
    private final float JUMP_TIMER = 2.0f;
    private final float JUMP_VEL = 25f;

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

    private Alex241Intro game;
    private Music theme;
    private Texture playButtonActive;
    private Texture playButtonInactive;
    private Sprite spriteR;
    private Texture mouseTextureR;
    private Sprite spriteG;
    private Texture mouseTextureG;
    private Sprite spriteW;
    private Texture mouseTextureW;
    private Texture background;
    private BitmapFont gridNumberFont;
    private GlyphLayout movementLabels;
    private GlyphLayout abilityLabels;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera orthographicCamera;
    private final int VEL_ITER = 6;
    private final int POS_ITER = 2;

    private Body mouseBodyG;
    private Body mouseBodyR;
    private Body mouseBodyW;

    private float timerJumpG;
    private float timerJumpR;
    private float timerJumpW;

    private BodyDef mouseBG;
    private BodyDef mouseBR;
    private BodyDef mouseBW;

    public MainMenuScreen(Alex241Intro game) {
        this.game = game;

        this.LOOK_RED = ThreadLocalRandom.current().nextBoolean();
        this.LOOK_GREEN = ThreadLocalRandom.current().nextBoolean();
        this.LOOK_WHITE = ThreadLocalRandom.current().nextBoolean();
        this.theme = (ThreadLocalRandom.current().nextBoolean()) ? Gdx.audio.newMusic(Gdx.files.internal("theme.wav")) : Gdx.audio.newMusic(Gdx.files.internal("alt_main_screen.mp3"));
        this.background = new Texture("MainMenuScreen.png");
        this.playButtonActive = new Texture("play_button_active.png");
        this.playButtonInactive = new Texture("play_button_inactive.png");
        this.gridNumberFont = new BitmapFont(Gdx.files.internal("fonts\\score2.fnt"));
        this.movementLabels = new GlyphLayout(gridNumberFont, "A -> Left\nD -> Right\nSpaceBar -> Jump");
        this.abilityLabels = new GlyphLayout(gridNumberFont, "F -> Free\nM -> Malloc\nC -> Catch");

        this.mouseTextureR = (LOOK_RED) ? LSR : RSR;
        this.mouseTextureG = (LOOK_GREEN) ? LSG : RSG;
        this.mouseTextureW = (LOOK_WHITE) ? LSW : RSW;

        this.spriteR = new Sprite(this.mouseTextureR);
        this.spriteR.setPosition((Gdx.graphics.getWidth()/2)/PIXELS_IN_METER, (Gdx.graphics.getHeight() - 600)/PIXELS_IN_METER);
        this.spriteG = new Sprite(this.mouseTextureG);
        this.spriteG.setPosition((Gdx.graphics.getWidth()/2)/PIXELS_IN_METER, (Gdx.graphics.getHeight() - 600)/PIXELS_IN_METER);
        this.spriteW = new Sprite(this.mouseTextureW);
        this.spriteW.setPosition((Gdx.graphics.getWidth()/2)/PIXELS_IN_METER, (Gdx.graphics.getHeight() - 600)/PIXELS_IN_METER);

        this.GREEN_TIMER = 0.0f;
        this.RED_TIMER = 0.0f;
        this.WHITE_TIMER = 0.0f;

        previousMoveGreen = (LOOK_GREEN) ? 0 : 3;
        previousMoveRed = (LOOK_RED) ? 0 : 3;
        previousMoveWhite = (LOOK_WHITE) ? 0 : 3;

        //create world
        world = new World(new Vector2(0f,-9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        //create camera
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false,Gdx.graphics.getWidth()/PIXELS_IN_METER, Gdx.graphics.getHeight()/PIXELS_IN_METER);

        this.START_GREEN = (ThreadLocalRandom.current().nextInt(500, Gdx.graphics.getWidth() - mouseTextureG.getWidth() - 501));
        this.START_RED = (ThreadLocalRandom.current().nextInt(500, Gdx.graphics.getWidth() - mouseTextureG.getWidth() - 501));
        this.START_WHITE = ThreadLocalRandom.current().nextInt(500, Gdx.graphics.getWidth() - mouseTextureW.getWidth() - 501);

        /*CREATE BODIES AND FIXTURES*/
        /**create ground*/
        Body groundBody;
        BodyDef groundBodyDef = new BodyDef();
        ChainShape groundShape = new ChainShape();
        FixtureDef groundFixtureDef = new FixtureDef();

        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0,0);
        groundBody = world.createBody(groundBodyDef);

        groundShape.createChain(new Vector2[]{new Vector2((Gdx.graphics.getWidth()-WIDTH)/PIXELS_IN_METER, (Gdx.graphics.getHeight() - 600)/PIXELS_IN_METER), new Vector2((Gdx.graphics.getWidth())/PIXELS_IN_METER, (Gdx.graphics.getHeight()-600)/PIXELS_IN_METER)});//SET-AS-BOX---rect.width/2, rect.height/2
        groundFixtureDef.shape = groundShape;
        groundBody.createFixture(groundFixtureDef);
        /**create green mouse*/
        mouseBG = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef mouseFG = new FixtureDef();
        mouseBG.position.set((START_GREEN/PIXELS_IN_METER), (Gdx.graphics.getHeight() - 564)/PIXELS_IN_METER);
        mouseBG.type = BodyDef.BodyType.DynamicBody;
        mouseBG.fixedRotation = true;
        mouseBodyG = world.createBody(mouseBG);

        polygonShape.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
        mouseFG.shape = polygonShape;
        mouseFG.density = 2.5f;
        mouseFG.friction = 0.25f;
        mouseFG.restitution = 0.0f;
        mouseBodyG.createFixture(mouseFG);
        /**create red mouse*/
        mouseBR = new BodyDef();
        PolygonShape polygonShape1 = new PolygonShape();
        FixtureDef mouseFR = new FixtureDef();
        mouseBR.position.set((START_RED/PIXELS_IN_METER), (Gdx.graphics.getHeight() - 564)/PIXELS_IN_METER);
        mouseBR.type = BodyDef.BodyType.DynamicBody;
        mouseBR.fixedRotation = true;
        mouseBodyR = world.createBody(mouseBR);

        polygonShape1.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
        mouseFR.shape = polygonShape1;
        mouseFR.density = 2.5f;
        mouseFR.friction = 0.25f;
        mouseFR.restitution = 0.0f;
        mouseBodyR.createFixture(mouseFR);
        /**create white mouse*/
        mouseBW = new BodyDef();
        PolygonShape polygonShape2 = new PolygonShape();
        FixtureDef mouseFW = new FixtureDef();
        mouseBW.position.set((START_WHITE/PIXELS_IN_METER), (Gdx.graphics.getHeight() - 564)/PIXELS_IN_METER);
        mouseBW.type = BodyDef.BodyType.DynamicBody;
        mouseBW.fixedRotation = true;
        mouseBodyW = world.createBody(mouseBW);

        polygonShape2.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
        mouseFW.shape = polygonShape2;
        mouseFW.density = 2.5f;
        mouseFW.friction = 0.25f;
        mouseFW.restitution = 0.0f;
        mouseBodyW.createFixture(mouseFW);
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
        if (!theme.isPlaying()) {
            theme.play();
            theme.setLooping(true);
        }
//        theme.setOnCompletionListener(new Music.OnCompletionListener() {
//            @Override
//            public void onCompletion(Music music) {
//                music.play();
//            }
//        });
    }



    public void entityUpdate(float delta) {
        /**ADD THE NPC MOVEMENT HERE*/
        /**MOVE GREEN*/
        GREEN_TIMER += delta;
        timerJumpG += delta;

        if (GREEN_TIMER >= MOVEMENT_TIMER) {
            GREEN_TIMER = 0;
            //choose move randomly
            int move = ThreadLocalRandom.current().nextInt(7);
            boolean directionFacing = move < 3;
            boolean jump = false;
            while ((move == previousMoveGreen && !(move == 0 || move == 3)) || (move == 6 && timerJumpG < JUMP_TIMER)) {//check for duplicate moves unless they are standing still
                move = ThreadLocalRandom.current().nextInt(7);
                directionFacing = move < 3;
            }

            if (move != 0 && move != 3) {//check for actual movement
                if (move == 6 && timerJumpG >= JUMP_TIMER) {
                    jump = true;
                    timerJumpG = 0.0f;
                } else {
                    if (directionFacing) {//left 1-2
                        if (previousMoveGreen == 1) {
                            //render left right
                            mouseTextureG = LRG;
                        } else {
                            //render left left
                            mouseTextureG = LLG;
                        }
                        mouseBodyG.applyLinearImpulse(new Vector2(-7, 0), mouseBodyG.getWorldCenter(), true);
                    } else {//right 4-5
                        if (previousMoveGreen == 4) {
                            //render right right
                            mouseTextureG = RRG;
                        } else {
                            //render right left
                            mouseTextureG = RLG;
                        }
                        mouseBodyG.applyLinearImpulse(new Vector2(7, 0), mouseBodyG.getWorldCenter(), true);
                    }
                }
            } else {
                if (directionFacing) {
                    //render left still
                    mouseTextureG = LSG;
                } else {
                    //render right still
                    mouseTextureG = RSG;
                }
            }
            previousMoveGreen = move;

            this.START_GREEN = mouseBodyG.getPosition().x*PIXELS_IN_METER - CHARACTER_WIDTH/2;
            float y_position = mouseBodyG.getPosition().y;

            //movement check for screen wrapping here
            if (START_GREEN < 500) {
                START_GREEN = Gdx.graphics.getWidth() - mouseTextureG.getWidth()- 500;

                world.destroyBody(mouseBodyG);

                mouseBG = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef mouseFG = new FixtureDef();
                mouseBG.position.set((START_GREEN/PIXELS_IN_METER), y_position);
                mouseBG.type = BodyDef.BodyType.DynamicBody;
                mouseBG.fixedRotation = true;
                mouseBodyG = world.createBody(mouseBG);

                polygonShape.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
                mouseFG.shape = polygonShape;
                mouseFG.density = 2.5f;
                mouseFG.friction = 0.25f;
                mouseFG.restitution = 0.0f;
                mouseBodyG.createFixture(mouseFG);
            }
            if (START_GREEN > Gdx.graphics.getWidth() + mouseTextureG.getWidth() - 500) {
                START_GREEN = 500;

                world.destroyBody(mouseBodyG);

                mouseBG = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef mouseFG = new FixtureDef();
                mouseBG.position.set((START_GREEN/PIXELS_IN_METER), y_position);
                mouseBG.type = BodyDef.BodyType.DynamicBody;
                mouseBG.fixedRotation = true;
                mouseBodyG = world.createBody(mouseBG);

                polygonShape.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
                mouseFG.shape = polygonShape;
                mouseFG.density = 2.5f;
                mouseFG.friction = 0.25f;
                mouseFG.restitution = 0.0f;
                mouseBodyG.createFixture(mouseFG);
            }

            /**UPDATE green mouse*/
            if (jump) {
                mouseBodyG.applyLinearImpulse(new Vector2(0f, JUMP_VEL), mouseBodyG.getPosition(), true);
            }
        }

        /**MOVE RED*/
        RED_TIMER += delta;
        timerJumpR += delta;

        if (RED_TIMER >= MOVEMENT_TIMER) {
            RED_TIMER = 0;
            //choose move randomly
            int move = ThreadLocalRandom.current().nextInt(7);
            boolean directionFacing = move < 3;
            boolean jump = false;
            while ((move == previousMoveRed && !(move == 0 || move == 3)) || (move == 6 && timerJumpR < JUMP_TIMER)) {//check for duplicate moves unless they are standing still
                move = ThreadLocalRandom.current().nextInt(7);
                directionFacing = move < 3;
            }

            if (move != 0 && move != 3) {//check for actual movement
                if (move == 6 && timerJumpR >= JUMP_TIMER) {
                    jump = true;
                    timerJumpR = 0.0f;
                } else {
                    if (directionFacing) {//left 1-2
                        if (previousMoveRed == 1) {
                            //render left right
                            mouseTextureR = LRR;
                        } else {
                            //render left left
                            mouseTextureR = LLR;
                        }
                        mouseBodyR.applyLinearImpulse(new Vector2(-7, 0), mouseBodyR.getWorldCenter(), true);
                    } else {//right 4-5
                        if (previousMoveRed == 4) {
                            //render right right
                            mouseTextureR = RRR;
                        } else {
                            //render right left
                            mouseTextureR = RLR;
                        }
                        mouseBodyR.applyLinearImpulse(new Vector2(7, 0), mouseBodyR.getWorldCenter(), true);
                    }
                }
            } else {
                if (directionFacing) {
                    //render left still
                    mouseTextureR = LSR;
                } else {
                    //render right still
                    mouseTextureR = RSR;
                }
            }
            previousMoveRed = move;

            this.START_RED = mouseBodyR.getPosition().x*PIXELS_IN_METER - CHARACTER_WIDTH/2;
            float y_position = mouseBodyR.getPosition().y;

            //movement check for screen wrapping here
            if (START_RED < 500) {
                START_RED = Gdx.graphics.getWidth() - mouseTextureR.getWidth()- 500;

                world.destroyBody(mouseBodyR);

                mouseBR = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef mouseFR = new FixtureDef();
                mouseBR.position.set((START_RED/PIXELS_IN_METER), y_position);
                mouseBR.type = BodyDef.BodyType.DynamicBody;
                mouseBR.fixedRotation = true;
                mouseBodyR = world.createBody(mouseBR);

                polygonShape.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
                mouseFR.shape = polygonShape;
                mouseFR.density = 2.5f;
                mouseFR.friction = 0.25f;
                mouseFR.restitution = 0.0f;
                mouseBodyR.createFixture(mouseFR);
            }
            if (START_RED > Gdx.graphics.getWidth() + mouseTextureR.getWidth() - 500) {
                START_RED = 500;

                world.destroyBody(mouseBodyR);

                mouseBR = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef mouseFR = new FixtureDef();
                mouseBR.position.set((START_RED/PIXELS_IN_METER), y_position);
                mouseBR.type = BodyDef.BodyType.DynamicBody;
                mouseBR.fixedRotation = true;
                mouseBodyR = world.createBody(mouseBR);

                polygonShape.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
                mouseFR.shape = polygonShape;
                mouseFR.density = 2.5f;
                mouseFR.friction = 0.25f;
                mouseFR.restitution = 0.0f;
                mouseBodyR.createFixture(mouseFR);
            }

            /**UPDATE green mouse*/
            if (jump) {
                mouseBodyR.applyLinearImpulse(new Vector2(0f, JUMP_VEL), mouseBodyR.getPosition(), true);
            }
        }

        /**MOVE WHITE*/
        WHITE_TIMER += delta;
        timerJumpW += delta;

        if (WHITE_TIMER >= MOVEMENT_TIMER) {
            WHITE_TIMER = 0;
            //choose move randomly
            int move = ThreadLocalRandom.current().nextInt(7);
            boolean directionFacing = move < 3;
            boolean jump = false;
            while ((move == previousMoveWhite && !(move == 0 || move == 3)) || (move == 6 && timerJumpW < JUMP_TIMER)) {//check for duplicate moves unless they are standing still
                move = ThreadLocalRandom.current().nextInt(7);
                directionFacing = move < 3;
            }

            if (move != 0 && move != 3) {//check for actual movement
                if (move == 6 && timerJumpW >= JUMP_TIMER) {
                    jump = true;
                    timerJumpW = 0.0f;
                } else {
                    if (directionFacing) {//left 1-2
                        if (previousMoveWhite == 1) {
                            //render left right
                            mouseTextureW = LRW;
                        } else {
                            //render left left
                            mouseTextureW = LLW;
                        }
                        mouseBodyW.applyLinearImpulse(new Vector2(-7, 0), mouseBodyW.getWorldCenter(), true);
                    } else {//right 4-5
                        if (previousMoveWhite == 4) {
                            //render right right
                            mouseTextureW = RRW;
                        } else {
                            //render right left
                            mouseTextureW = RLW;
                        }
                        mouseBodyW.applyLinearImpulse(new Vector2(7, 0), mouseBodyW.getWorldCenter(), true);
                    }
                }
            } else {
                if (directionFacing) {
                    //render left still
                    mouseTextureW = LSW;
                } else {
                    //render right still
                    mouseTextureW = RSW;
                }
            }
            previousMoveWhite = move;

            this.START_WHITE = mouseBodyW.getPosition().x*PIXELS_IN_METER - CHARACTER_WIDTH/2;
            float y_position = mouseBodyW.getPosition().y;

            //movement check for screen wrapping here
            if (START_WHITE < 500) {
                START_WHITE = Gdx.graphics.getWidth() - mouseTextureW.getWidth()- 500;

                world.destroyBody(mouseBodyW);

                mouseBW = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef mouseFW = new FixtureDef();
                mouseBW.position.set((START_WHITE/PIXELS_IN_METER), y_position);
                mouseBW.type = BodyDef.BodyType.DynamicBody;
                mouseBW.fixedRotation = true;
                mouseBodyW = world.createBody(mouseBW);

                polygonShape.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
                mouseFW.shape = polygonShape;
                mouseFW.density = 2.5f;
                mouseFW.friction = 0.25f;
                mouseFW.restitution = 0.0f;
                mouseBodyW.createFixture(mouseFW);
            }
            if (START_WHITE > Gdx.graphics.getWidth() + mouseTextureW.getWidth() - 500) {
                START_WHITE = 500;

                world.destroyBody(mouseBodyW);

                mouseBW = new BodyDef();
                PolygonShape polygonShape = new PolygonShape();
                FixtureDef mouseFW = new FixtureDef();
                mouseBW.position.set((START_WHITE/PIXELS_IN_METER), y_position);
                mouseBW.type = BodyDef.BodyType.DynamicBody;
                mouseBW.fixedRotation = true;
                mouseBodyW = world.createBody(mouseBW);

                polygonShape.setAsBox(CHARACTER_WIDTH/2/PIXELS_IN_METER, CHARACTER_HEIGHT/2/PIXELS_IN_METER);
                mouseFW.shape = polygonShape;
                mouseFW.density = 2.5f;
                mouseFW.friction = 0.25f;
                mouseFW.restitution = 0.0f;
                mouseBodyW.createFixture(mouseFW);
            }

            /**UPDATE green mouse*/
            if (jump) {
                mouseBodyW.applyLinearImpulse(new Vector2(0f, JUMP_VEL), mouseBodyW.getPosition(), true);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update world
        if (world != null) {
            this.update();
        }

        game.batch.begin();
        game.batch.draw(background, 0, 0);

        int x = game.WIDTH/2 - PLAY_BUTTON_WIDTH/2;
        if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH*2 && Gdx.input.getX() > x && game.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT*2 && game.HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y) {
            game.batch.draw(playButtonActive,x,PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH*2,PLAY_BUTTON_HEIGHT*2);

            //check if the mouse was clicked start the game
            if (Gdx.input.justTouched()) {
                this.dispose();

                try {
                    game.setScreen(new Level1(game));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            game.batch.draw(playButtonInactive,x,PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH*2,PLAY_BUTTON_HEIGHT*2);
        }

        //update all entities in game world
        if (world != null) {
            this.entityUpdate(delta);

            this.spriteR.setTexture(this.mouseTextureR);
            this.spriteG.setTexture(this.mouseTextureG);
            this.spriteW.setTexture(this.mouseTextureW);
            this.spriteG.setPosition(START_GREEN, mouseBodyG.getPosition().y*PIXELS_IN_METER - CHARACTER_HEIGHT/2);
            this.spriteR.setPosition(START_RED, mouseBodyR.getPosition().y*PIXELS_IN_METER - CHARACTER_HEIGHT/2);
            this.spriteW.setPosition(START_WHITE, mouseBodyW.getPosition().y*PIXELS_IN_METER - CHARACTER_HEIGHT/2);
        }

        this.spriteG.draw(game.batch);
        this.spriteR.draw(game.batch);
        this.spriteW.draw(game.batch);

        gridNumberFont.draw(game.batch, movementLabels, 150, Gdx.graphics.getHeight()/2 - movementLabels.height - 100);
        gridNumberFont.draw(game.batch, abilityLabels, Gdx.graphics.getWidth() - abilityLabels.width - 100, Gdx.graphics.getHeight()/2 - abilityLabels.height - 100);

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