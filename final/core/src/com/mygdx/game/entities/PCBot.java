package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

public class PCBot extends Enemy {

    private final float SPEED = 2f;
    private float orientation = 90;
    private final float TIMER = 2f;
    private float rotateTimer = 0f;
    private boolean rotate = false;
    private boolean startRotation = true;
    private boolean flag_for_init_rotation = true;
    private boolean visibility;

    private final float ATTACK_ZONE = 400;
    private int healthBar = 3;

    public PCBot(Alex241Intro game, Vector2 initPos) {
        super.game = game;
        super.setOrigin(initPos);
        super.position = new Vector2(initPos.x, initPos.y);
        super.setSCREEN_WIDTH(Gdx.graphics.getWidth());
        super.setSCREEN_HEIGHT(Gdx.graphics.getHeight());
        super.setTexture(new Texture("PC.png"));
        super.setRect(new CollisionRect(game, initPos.x, initPos.y, super.getTexture().getWidth(), super.getTexture().getHeight()));
        super.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        super.setSprite(new Sprite(super.getTexture()));
        super.getSprite().setX(super.position.x);
        super.getSprite().setY(super.position.y);
        super.setMAX_SPEED(SPEED);
        super.velocity = new Vector2(0, 0);
        super.acceleration = new Vector2(0, 0);
        super.setState((byte)0);//guard state
        super.setTILE_PIXEL((byte)16);

        super.angleVector.rotate(45);
        visibility = false;
    }

    private void initialOrientation() {
        //point the orientation and sprite towards the top left of screen
        if (startRotation) {
            super.rotate(super.getFOV_DEGREES());
            startRotation = false;
        } else {
            super.velocity.set(0,0);
            super.acceleration.set(0,0);

            super.angleVector.setAngle(45);

            super.angleVector.rotate(45);
            super.rotate(super.getFOV_DEGREES());
        }
    }

    public int update(PC player, boolean wasColliison) {
        if (wasColliison) {
            --healthBar;
        }
        //think
        return this.think(player);
    }

    private int think(PC player) {
        if (player.getX()+player.getX() > ATTACK_ZONE) {
            visibility = true;
        }
        if (visibility) {
            this.chase(player);
        }

        return healthBar;
    }

    public void chase(PC player) {
        Vector2 playerTemp;
        playerTemp = new Vector2(player.getX(), player.getY());
        this.seek(playerTemp);

        velocity.add(acceleration);
        float chaseAngle = (float)((Math.atan2(velocity.y, velocity.x) * 180) / Math.PI);
        super.angleVector.setAngle(chaseAngle);

        if (velocity.len() > super.getMAX_SPEED()) {
            velocity.setLength(super.getMAX_SPEED());
        }
        position.add(velocity);

        super.getSprite().setX(this.position.x);
        super.getSprite().setY(this.position.y);

        //update the move
        super.getRect().move(this.position.x,this.position.y);
    }

    private void seek(Vector2 mpos) {
        Vector2 desired = (mpos.sub(super.position)).nor();
        desired.x *= SPEED;
        desired.y *= SPEED;

        Vector2 steer = new Vector2(desired.x-velocity.x, desired.y-velocity.y);
        acceleration.x = steer.x/super.getMASS();
        acceleration.y = steer.y/super.getMASS();
    }

    private void seekWithApproach(Vector2 mpos) {
        Vector2 desired = (mpos.sub(super.position));
        float distance = desired.len();
        desired.nor();
        if (distance < super.getAPPROACH_RADIUS()) {
            desired.x *= (distance / super.getAPPROACH_RADIUS() * SPEED);
            desired.y *= (distance / super.getAPPROACH_RADIUS() * SPEED);
        } else {
            desired.x *= SPEED;
            desired.y *= SPEED;
        }

        Vector2 steer = new Vector2(desired.x-velocity.x, desired.y-velocity.y);
        acceleration.x = steer.x/super.getMASS();
        acceleration.y = steer.y/super.getMASS();
    }
}

