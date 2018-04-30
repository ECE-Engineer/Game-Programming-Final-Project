package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

import java.util.ArrayList;

public class Portal {
    private final int BLOCK_W = 15;
    private final int BLOCK_H = 64;
    private final Texture texture = new Texture("portal_64.png");

    private Alex241Intro game;
    private Sprite sprite;
    private CollisionRect collisionRect;
    private float x;
    private float y;

    public Portal(Alex241Intro game, float x, float y) {
        this.game = game;
        this.x = (x - BLOCK_W);
        this.y = y;
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(this.x, this.y);
        this.sprite.setScale(2f);
        this.collisionRect = new CollisionRect(this.game, this.x, this.y, this.BLOCK_W, this.BLOCK_H);
    }

    public void renderPortal() {
        sprite.draw(game.batch);
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }
}
