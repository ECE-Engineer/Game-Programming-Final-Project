package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

public class Tile {
    private final short GRID_PIXEL_SIZE = 16;
    float x,y;
    Texture texture;
    CollisionRect collisionRect;
    Alex241Intro game;
    boolean impassible;
    boolean isGround;
//    Vector2 normalUp;
//    Vector2 normalDown;
//    Vector2 normalLeft;
//    Vector2 normalRight;
//    boolean[] types;
private final float PIXELS_IN_METER = 50f;
    World world;
    Body body;
    BodyDef bodyDef;

    public Tile(Alex241Intro game, float x, float y, Texture texture, boolean impassible, World world, boolean isGround) {//, boolean[] types
//        this.types = types;
        this.world = world;
        this.game = game;
        this.x = x;
        this.y = y;
        this.collisionRect = new CollisionRect(game,x,y,this.getGRID_PIXEL_SIZE(), this.getGRID_PIXEL_SIZE());
        this.texture = texture;
        this.impassible = impassible;
        this.isGround = isGround;

        if (impassible && !isGround) {
            bodyDef = new BodyDef();
            PolygonShape polygonShape = new PolygonShape();
            FixtureDef tileFixture = new FixtureDef();
            bodyDef.position.set((this.x + GRID_PIXEL_SIZE/2)/PIXELS_IN_METER, (this.y + GRID_PIXEL_SIZE/2)/PIXELS_IN_METER);
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.fixedRotation = true;
            body = world.createBody(bodyDef);

            polygonShape.setAsBox(GRID_PIXEL_SIZE/2/PIXELS_IN_METER, GRID_PIXEL_SIZE/2/PIXELS_IN_METER);
            tileFixture.shape = polygonShape;
            tileFixture.density = 2.5f;
            tileFixture.friction = 1f;
            tileFixture.restitution = 0.0f;
            body.createFixture(tileFixture);
        }

//        if (impassible) {
//            if (types[0]) {
//                normalUp = new Vector2(0, 25);
//            }
//            if (types[1]) {
//                normalDown = new Vector2(0, -25);
//            }
//            if (types[2]) {
//                normalLeft = new Vector2(-25, 0);
//            }
//            if (types[3]) {
//                normalRight = new Vector2(25, 0);
//            }
//        }
    }

//    public boolean isUp() {
//        return types[0];
//    }
//
//    public boolean isDown() {
//        return types[1];
//    }
//
//    public boolean isLeft() {
//        return types[2];
//    }
//
//    public boolean isRight() {
//        return types[3];
//    }

//    public void displayArray() {
//        for (int i = 0; i < types.length; i++) {
//            System.out.print(types[i] + " ");
//        }
//        System.out.println();
//    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public int getGRID_PIXEL_SIZE() {
        return GRID_PIXEL_SIZE;
    }

    public Texture getTexture() {
        return texture;
    }

    public Alex241Intro getGame() {
        return game;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isImpassible() {
        return impassible;
    }

    public boolean isGround() {
        return isGround;
    }

    //    public Vector2 getNormalUp() {
//        return normalUp;
//    }
//
//    public Vector2 getNormalDown() {
//        return normalDown;
//    }
//
//    public Vector2 getNormalLeft() {
//        return normalLeft;
//    }
//
//    public Vector2 getNormalRight() {
//        return normalRight;
//    }
}