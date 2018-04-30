package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

import java.util.ArrayList;

public class NullBot {
    private final int BLOCK_SIZE = 96;
    private final int BLOCKS_H = 1;
    private final int BLOCKS_W = 2;
    private final float X = Gdx.graphics.getWidth() / 2 - (BLOCK_SIZE * BLOCKS_W) / 2;
    private final float Y = (13 - 1) * 16;//bottom wall rows * wall_size
    private final float ZONE1 = BLOCK_SIZE * 2;
    private final float BUFFER_ZONE = X - BLOCK_SIZE;
    private final float ZONE2 = X;
    private final float ZONE3 = X + BLOCK_SIZE + BLOCK_SIZE + BLOCK_SIZE + BLOCK_SIZE;
    private final float ZONE1_TIMER = 0.75f;
    private final float ZONE3_TIMER = 0.5f;
    private final Texture texture = new Texture("SPECIAL_BLOCK_96.png");
    private final float displacementValue = 5.0f;

    private Alex241Intro game;
    private Sprite[][] sprites;
    private BitmapFont bitmapFont;
    private GlyphLayout nullLayout;
    private float coolDownTimer1 = 0.0f;
    private float coolDownTimer3 = 0.0f;
    private ArrayList<Exceptions> exceptions1;
    private ArrayList<Exceptions> exceptions2;
    private ArrayList<Exceptions> exceptions3;
    private int counterZone2 = 1;
    private int counterZone3 = 3;
    private CollisionRect collisionRect;
    private final float PIXELS_IN_METER = 50f;
    World world;
    Body body1;
    Body body2;
    BodyDef bodyDef1;
    BodyDef bodyDef2;

    public NullBot(Alex241Intro game, BitmapFont bitmapFont, World world) {
        this.game = game;
        this.world = world;
        this.bitmapFont = bitmapFont;
        nullLayout = new GlyphLayout(bitmapFont, "NULL*");
        sprites = new Sprite[BLOCKS_H][BLOCKS_W];
        this.collisionRect = new CollisionRect(this.game, X, Y, BLOCKS_W*BLOCK_SIZE, BLOCKS_H*BLOCK_SIZE);

        for (int i = 0; i < sprites.length; i++) {
            for (int j = 0; j < sprites[i].length; j++) {
                sprites[i][j] = new Sprite(texture);
            }
        }

        sprites[0][0].setPosition(X, Y);
        sprites[0][1].setPosition(X + BLOCK_SIZE , Y);

        exceptions1 = new ArrayList<>();
        exceptions2 = new ArrayList<>();
        exceptions3 = new ArrayList<>();

        bodyDef1 = new BodyDef();
        PolygonShape polygonShape1 = new PolygonShape();
        FixtureDef Null1 = new FixtureDef();
        bodyDef1.position.set((X + BLOCK_SIZE / 2)/PIXELS_IN_METER, (Y + BLOCK_SIZE / 2)/PIXELS_IN_METER);
        bodyDef1.type = BodyDef.BodyType.StaticBody;
        bodyDef1.fixedRotation = true;
        body1 = world.createBody(bodyDef1);

        polygonShape1.setAsBox(BLOCK_SIZE/2/PIXELS_IN_METER, BLOCK_SIZE/2/PIXELS_IN_METER);
        Null1.shape = polygonShape1;
        Null1.density = 2.5f;
        Null1.friction = 1f;
        Null1.restitution = 0.0f;
        body1.createFixture(Null1);




        bodyDef2 = new BodyDef();
        PolygonShape polygonShape2 = new PolygonShape();
        FixtureDef Null2 = new FixtureDef();
        bodyDef2.position.set((X + BLOCK_SIZE + BLOCK_SIZE / 2)/PIXELS_IN_METER, (Y + BLOCK_SIZE / 2)/PIXELS_IN_METER);
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        bodyDef2.fixedRotation = true;
        body2 = world.createBody(bodyDef2);

        polygonShape2.setAsBox(BLOCK_SIZE/2/PIXELS_IN_METER, BLOCK_SIZE/2/PIXELS_IN_METER);
        Null2.shape = polygonShape2;
        Null2.density = 2.5f;
        Null2.friction = 0.25f;
        Null2.restitution = 0.0f;
        body2.createFixture(Null2);
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public void renderNullBot() {
        for (int i = 0; i < sprites.length; i++) {
            for (int j = 0; j < sprites[i].length; j++) {
                sprites[i][j].draw(game.batch);
            }
        }
        bitmapFont.draw(game.batch, nullLayout, X + (BLOCK_SIZE / 4), Y + (BLOCK_SIZE / 1.75f));
    }

    private void think(PC player, float delta) {
        if (player.getX() + player.getBLOCK_W() > ZONE1) {
            if (player.getX() + player.getBLOCK_W() > ZONE2 - 10 && player.getY() + player.getBLOCK_H() > Y + BLOCK_SIZE) {
                //THROW 1 exception up
                if (counterZone2 > 0) {
                    exceptions2.add(new Exceptions(this.game, X, Y + BLOCK_SIZE, 64));
                    counterZone2--;
                }
            } else if (player.getX() + player.getBLOCK_W() > ZONE2) {
                if (player.getX() + player.getBLOCK_W() > ZONE3) {
                    //THROW 3 exceptions right
                    coolDownTimer3 += delta;
                    if (coolDownTimer3 >= ZONE3_TIMER) {
                        coolDownTimer3 = 0.0f;
                        //THROW 3 exceptions right
                        if (counterZone3 > 0) {
                            exceptions3.add(new Exceptions(this.game, X + BLOCK_SIZE, Y, 64));
                            counterZone3--;
                        }
                    }
                }
            } else if (player.getX() + player.getBLOCK_W() < BUFFER_ZONE) {
                //THROW infinite exceptions left
                coolDownTimer1 += delta;
                if (coolDownTimer1 >= ZONE1_TIMER) {
                    coolDownTimer1 = 0.0f;
                    //THROW infinite exceptions left
                    exceptions1.add(new Exceptions(this.game, X, Y, 64));
                }
            }
        }
    }

    public void update(PC player, float delta) {
        //update the exceptions
        //left
        for (Exceptions e : exceptions1) {
            e.update(-displacementValue, 0);
        }
        //right
        for (Exceptions e : exceptions3) {
            e.update(displacementValue, 0);
        }

        //think
        this.think(player, delta);
    }

    public ArrayList<Exceptions> getExceptions1() {
        return exceptions1;
    }

    public void setExceptions1(ArrayList<Exceptions> exceptions) {
        this.exceptions1 = exceptions;
    }

    public ArrayList<Exceptions> getExceptions2() {
        return exceptions2;
    }

    public void setExceptions2(ArrayList<Exceptions> exceptions) {
        this.exceptions2 = exceptions;
    }

    public ArrayList<Exceptions> getExceptions3() {
        return exceptions3;
    }

    public void setExceptions3(ArrayList<Exceptions> exceptions) {
        this.exceptions3 = exceptions;
    }
}
