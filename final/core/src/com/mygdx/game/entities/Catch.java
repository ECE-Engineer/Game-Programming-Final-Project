package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

public class Catch {
    private final int BLOCK_W = 20;
    private final int BLOCK_H = 64;
    private final Texture left = new Texture("catch_left_64.png");
    private final Texture right = new Texture("catch_right_64.png");
    private final float DISPLACEMENT = 5f;

    private Texture texture;
    private Alex241Intro game;
    private Sprite sprite;
    private float x;
    private float y;
    private CollisionRect collisionRect;
    private boolean directionFacing;

    public Catch(Alex241Intro game, float x, float y, boolean directionFacing) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.directionFacing = directionFacing;
        if (directionFacing) {
            texture = left;
        } else {
            texture = right;
        }
        sprite = new Sprite(texture);
        sprite.setPosition(this.x, this.y);
        collisionRect = new CollisionRect(this.game, this.x, this.y, this.BLOCK_W, this.BLOCK_H);
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public void renderCatch() {
        sprite.draw(game.batch);
    }

    public void update() {
        if (directionFacing) {//left
            this.x -= DISPLACEMENT;
        } else {//right
            this.x += DISPLACEMENT;
        }

        this.sprite.setPosition(this.x, this.y);
        this.collisionRect.move(this.x, this.y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        sprite.setPosition(this.x, this.y);
    }

    public void setY(float y) {
        this.y = y;
        sprite.setPosition(this.x, this.y);
    }

    public int getBLOCK_W() {
        return BLOCK_W;
    }

    public int getBLOCK_H() {
        return BLOCK_H;
    }
}
