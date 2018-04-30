package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

public abstract class Enemy {

    private byte TILE_PIXEL;
    private final float OFFSET = 60;
    private final float RADIUS = 200;
    private final float FOV_DEGREES = 120;
    private final float APPROACH_RADIUS = 5f;
    private float MASS = 50;
    private Vector2 origin;
    private float MAX_SPEED;
    private float SCREEN_WIDTH;
    private float SCREEN_HEIGHT;
    private CollisionRect rect;
    private byte state;
    private Texture texture;
    private Sprite sprite;
    private ShapeRenderer fovRenderer = new ShapeRenderer();
    Alex241Intro game;
    Vector2 angleVector = new Vector2(1,1).nor();
    Vector2 position;
    Vector2 velocity;
    Vector2 acceleration;

    public float getFOV_DEGREES() {
        return FOV_DEGREES;
    }

    public float getOFFSET() {
        return OFFSET;
    }

    public float getMASS() {
        return MASS;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public double euclideanDist(float pointAI, float pointAJ, float pointBI, float pointBJ) {
        return Math.sqrt((Math.pow((pointAJ - pointBJ), 2)) + (Math.pow((pointAI - pointBI), 2)));
    }

    public ShapeRenderer getFOV() {
        return fovRenderer;
    }

    public float getRADIUS() {
        return RADIUS;
    }

    public float getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public float getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAPPROACH_RADIUS() {
        return APPROACH_RADIUS;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public byte getState() {
        return state;
    }

    public CollisionRect getRect() {
        return rect;
    }

    public float getAcceleration() {
        return acceleration.y;
    }

    public float getMAX_SPEED() {
        return MAX_SPEED;
    }

    public short getTILE_PIXEL() {
        return TILE_PIXEL;
    }

    public Texture getTexture() {
        return texture;
    }

    public void render (SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void renderConnection(boolean isTop, float endX, float endY) {
        //fovRenderer.setProjectionMatrix(game.camera.combined);
        fovRenderer.begin(ShapeRenderer.ShapeType.Line);
        fovRenderer.setColor(1, 1, 0, 1);
        if (isTop) {
            //this is the root node so the end connection is straight up
            fovRenderer.line(this.position.x+TILE_PIXEL/2, this.position.y+TILE_PIXEL/2, this.position.x+TILE_PIXEL/2, this.position.y+TILE_PIXEL/2);
        } else {
            //this is a child node
            fovRenderer.line(this.position.x+TILE_PIXEL/2, this.position.y+TILE_PIXEL/2, endX+TILE_PIXEL/2, endY);
        }
        fovRenderer.end();
    }

    public void update(Vector2 position) {
        this.position = position;

        sprite.setX(this.position.x);
        sprite.setY(this.position.y);

        //update the move
        rect.move(this.position.x, this.position.y);
    }

    public void rotate(float angle) {
//        sprite.rotate(angle);
        angleVector.rotate(angle);
    }

    public void setTILE_PIXEL(byte TILE_PIXEL) {
        this.TILE_PIXEL = TILE_PIXEL;
    }

    public void changeAcceleration(float f) {
        acceleration.y += f;
    }

    public void setRect(CollisionRect rect) {
        this.rect = rect;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setMAX_SPEED(float MAX_SPEED) {
        this.MAX_SPEED = MAX_SPEED;
    }

    public void setSCREEN_WIDTH(float SCREEN_WIDTH) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
    }

    public void setSCREEN_HEIGHT(float SCREEN_HEIGHT) {
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }
}
