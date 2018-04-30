package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.Alex241Intro;

public class CollisionRect {
    float x, y;
    int width, height;
    Alex241Intro game;

    public CollisionRect(Alex241Intro game, float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.game = game;
    }

    public void move (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean collidesWith(CollisionRect rect) {
        //gets rid of the area to the right of the furthest right rect
        //gets rid of the area to the top of the top most rect
        //gets rid of the area to the left of the right most rect
        //gets rid of the area to the bottom of the top most rect
        return x < rect.x + rect.width && y < rect.y + rect.height && x + width > rect.x && y + height > rect.y;
        //line 1: is this.x pos inside of the distance from rect.x to rect.width
        //line 2: is this.y pos inside of the distance from rect.y to rect.height
        //ABOVE COMBINES TO is this(x,y) lie within rect(x,y)
        //line 3: is rect.x pos inside of this.x pos to this.width
        //line 4: is rect.y pos inside of this.y pos to this.height
        //ABOVE COMBINES TO is rect(x,y) lie within this(x,y)
    }

    public boolean isHoveringOver(float x, float y) {
        return x < this.x + this.width && x > this.x && game.HEIGHT - y < this.y + this.height && game.HEIGHT - y > this.y;
    }
}