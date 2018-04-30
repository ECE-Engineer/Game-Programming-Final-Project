package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

public class Exceptions {
    private Texture texture;
    private final Texture texture1 = new Texture("exception_64.png");
    private final Texture texture2 = new Texture("exception_32.png");
    private final Texture texture3 = new Texture("exception_16.png");

    private Alex241Intro game;
    private Sprite sprite;
    private float x;
    private float y;
    private CollisionRect collisionRect;

    private final float CHASE_NUM = 25f;
    private final float CHASE_TIMER = 0.075f;
    private float chaseTimer;

    public Exceptions(Alex241Intro game, float x, float y, int size) {
        if (size == 64) {
            texture = texture1;
        } else if (size == 32) {
            texture = texture2;
        } else if (size == 16) {
            texture = texture3;
        }
        this.game = game;
        this.x = x;
        this.y = y;
        sprite = new Sprite(texture);
        sprite.setPosition(this.x, this.y);
        collisionRect = new CollisionRect(this.game, this.x, this.y, this.texture.getWidth(), this.texture.getHeight());
        chaseTimer = 0f;
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public void renderException() {
        sprite.draw(game.batch);
    }

    public void update(float x, float y) {
        this.x += x;
        this.y += y;
        this.sprite.setPosition(this.x, this.y);
        this.collisionRect.move(this.x, this.y);
    }

    public void autoDestroy(PC player, float delta) {
        chaseTimer += delta;
        if (chaseTimer > CHASE_TIMER) {
            chaseTimer = 0f;
            if (x < player.getX()+player.getBLOCK_W()/2) {
                this.x += CHASE_NUM;
            } else {
                this.x -= CHASE_NUM;
            }

            if (y < player.getY()+player.getBLOCK_H()/2) {
                this.y += CHASE_NUM;
            } else {
                this.y -= CHASE_NUM;
            }
            this.sprite.setPosition(this.x, this.y);
            this.collisionRect.move(this.x, this.y);
        }
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
        return texture.getWidth();
    }

    public int getBLOCK_H() {
        return texture.getHeight();
    }
}
