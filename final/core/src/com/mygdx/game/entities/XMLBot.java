package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

import java.util.ArrayList;

public class XMLBot {
    private final int BLOCK_SIZE = 64;
    private final float START_Y = (63 - 12) * 16;
    private final float X = Gdx.graphics.getWidth()/2 - 6*16;
    private float Y = START_Y;
    private final float ZONE1 = 368;
    private final float ZONE2 = 1296;
    private final float ZONE3 = X + BLOCK_SIZE + BLOCK_SIZE + BLOCK_SIZE + BLOCK_SIZE;
    private final float ZONE1_TIMER = 0.75f;
    private final float ZONE3_TIMER = 0.5f;
    private final Texture texture = new Texture("XML_64.png");
    private final float displacementValue = 5.0f;


    private Alex241Intro game;
    private Sprite sprite;
    private float coolDownTimer1 = 0.0f;
    private float coolDownTimer3 = 0.0f;
    private ArrayList<SmartException> exceptions;
    private int counterZone2 = 1;
    private int counterZone3 = 3;
    private CollisionRect collisionRect;
    private final float PIXELS_IN_METER = 50f;
    World world;
    Body body;
    BodyDef bodyDef;

    private int exceptionCounter;
//    private final float EXCEPTION_MOVE_TIMER = 0.01f;
    private final float EXCEPTION_CREATE_TIMER = 0.45f;
//    private float exceptionMoveTimer;
    private float exceptionCreateTimer;
    private boolean create;


    private final float MOVE_TIMER = 0.1f;
    private float moveTimer;

    public XMLBot(Alex241Intro game, World world) {
        this.game = game;
        this.world = world;
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(X, Y);
        this.collisionRect = new CollisionRect(this.game, X, Y, BLOCK_SIZE, BLOCK_SIZE);
        this.exceptions = new ArrayList<>();

        this.bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef xmlFixture = new FixtureDef();
        this.bodyDef.position.set((X + BLOCK_SIZE / 2)/PIXELS_IN_METER, (Y + BLOCK_SIZE / 2)/PIXELS_IN_METER);
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
        this.bodyDef.fixedRotation = true;
        this.body = world.createBody(bodyDef);

        polygonShape.setAsBox(BLOCK_SIZE/2/PIXELS_IN_METER, BLOCK_SIZE/2/PIXELS_IN_METER);
        xmlFixture.shape = polygonShape;
        xmlFixture.density = 2.5f;
        xmlFixture.friction = 1f;
        xmlFixture.restitution = 0.0f;
        this.body.createFixture(xmlFixture);

        moveTimer = 0f;
//        exceptionMoveTimer = 0;
        exceptionCreateTimer = 0;
        exceptionCounter = 10;
        create = true;
    }

    public float getZONE1() {
        return ZONE1;
    }

    public float getZONE2() {
        return ZONE2;
    }

    public CollisionRect getCollisionRect() {
        return this.collisionRect;
    }

    public void renderXMLBot() {
        this.sprite.draw(game.batch);
    }

    private int think(PC player, float delta) {
        if (player.getX() + player.getBLOCK_W() > ZONE1 && player.getX() + player.getBLOCK_W() < ZONE2) {
            if (this.Y + BLOCK_SIZE > START_Y) {
                moveTimer += delta;
                if (moveTimer > MOVE_TIMER) {
                    moveTimer = 0f;
                    this.Y -= 5f;
                    this.sprite.setPosition(this.X, this.Y);
                }
            } else {
                //create exceptions
                if (create && exceptions.size() < exceptionCounter) {
                    exceptionCreateTimer += delta;
                    if (exceptionCreateTimer > EXCEPTION_CREATE_TIMER) {
                        exceptionCreateTimer = 0;
                        exceptions.add(new SmartException(game, new Vector2(X+32 - 16/2, START_Y - 64 - 16)));
                    }
                    if (exceptions.size() >= 10) {
                        create = false;
                        exceptionCounter = exceptions.size();
                    }
                }
                if (exceptions.size() > 0) {//update the exceptions
//                    exceptionMoveTimer += delta;
//                    if (exceptionMoveTimer > EXCEPTION_MOVE_TIMER) {
//                        exceptionMoveTimer = 0;
                        for (int i = 0; i < exceptions.size(); i++) {
                            //update
                            exceptions.get(i).chase(player);
                        }
//                    }
                }
                if (player.getX() + player.getBLOCK_W() > X+32 - 16/2 && !create) {
                    create = true;
                    exceptionCounter = 10;
                }
            }
        }
        if (player.getX() + player.getBLOCK_W() > ZONE2) {
            create = false;
        }
        return exceptionCounter;
    }

    public int update(PC player, float delta) {
        //think
        return this.think(player, delta);
    }

    public ArrayList<SmartException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(ArrayList<SmartException> exceptions) {
        this.exceptions = exceptions;
        if (!create) {
            exceptionCounter = this.exceptions.size();
        }
    }

    public void renderAllExceptions() {
        for (int i = 0; i < exceptions.size(); i++) {
            exceptions.get(i).render(game.batch);
        }
    }
}
