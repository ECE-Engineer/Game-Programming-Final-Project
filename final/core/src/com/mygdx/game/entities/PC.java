package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

import java.util.ArrayList;

public class PC {
    private final int BLOCK_W = 88;
    private final int BLOCK_H = 64;

    private final Texture LSG;
    private final Texture LLG;
    private final Texture LRG;
    private final Texture RSG;
    private final Texture RLG;
    private final Texture RRG;

    private final Texture LSR;
    private final Texture LLR;
    private final Texture LRR;
    private final Texture RSR;
    private final Texture RLR;
    private final Texture RRR;

    private final Texture LSW;
    private final Texture LLW;
    private final Texture LRW;
    private final Texture RSW;
    private final Texture RLW;
    private final Texture RRW;

    private final int ANIMATION_UP_GREEN = 40;
    private final int ANIMATION_SIDE_UP_GREEN = 51;
    private final int ANIMATION_SIDE_DOWN_GREEN = 75;

    public int getANIMATION_UP_GREEN() {
        return ANIMATION_UP_GREEN;
    }

    public int getANIMATION_SIDE_UP_GREEN() {
        return ANIMATION_SIDE_UP_GREEN;
    }

    public int getANIMATION_SIDE_DOWN_GREEN() {
        return ANIMATION_SIDE_DOWN_GREEN;
    }

    private final Texture ALLDG;
    private final Texture ALLUG;
    private final Texture ARRDG;
    private final Texture ARRUG;
    private final Texture AUG;

    private final int ANIMATION_UP = 41;
    private final int ANIMATION_SIDE_UP = 51;
    private final int ANIMATION_SIDE_DOWN = 70;

    public int getANIMATION_UP() {
        return ANIMATION_UP;
    }

    public int getANIMATION_SIDE_UP() {
        return ANIMATION_SIDE_UP;
    }

    public int getANIMATION_SIDE_DOWN() {
        return ANIMATION_SIDE_DOWN;
    }

    private final Texture ALLDR;
    private final Texture ALLUR;
    private final Texture ARRDR;
    private final Texture ARRUR;
    private final Texture AUR;

    private final Texture ALLDW;
    private final Texture ALLUW;
    private final Texture ARRDW;
    private final Texture ARRUW;
    private final Texture AUW;


    private final float MOVEMENT_TIMER = 0.1f;
    private final float JUMP_TIMER = 1.75f;
    private final float CATCH_TIMER = 0.3f;
    private final float CATCH_ANIMATION_TIMER = 0.3f;
    //    private final int MOVE_SELECTION = 10;
    private final float DISPLACEMENT_VALUE = 10f;

    private Texture texture;
    private Sprite sprite;
    private CollisionRect collisionRect;
    private Alex241Intro game;
    private float x;
    private float y;
    private int previousMove;
    private int move;
    private boolean directionFacing;
    private float moveTimer;
    private float jumpTimer;
    private float catchTimer;
    private float catchAnimationTimer = 0.0f;
    private ArrayList<Catch> catches;

    private boolean moveFlag;
    private boolean jumpFlag;
    private boolean catchFlag;
    private boolean catchAnimationFlag;


    int state;
    char[] memory;


    private final float PIXELS_IN_METER = 50f;
    private final float JUMP_VEL = 85f;
    World world;
    Body body;
    BodyDef bodyDef;
    private Sound stepSound;
    private Sound jumpSound;

    private final float ANIMATION_TIMER = 1f;
    private float animationTimer;
    private int animationStateCounter;
    private String dirSlash;

    public PC(Alex241Intro game, float x, float y, World world, String dirSlash) {
        /*
         * 0 -> left still
         * 1 -> left right_foot
         * 2 -> left left_foot
         * 3 -> right still
         * 4 -> right right_foot
         * 5 -> right left_foot
         *
         * 6 -> jump
         * 7 -> malloc
         * 8 -> free
         * 9 -> catch
         * */
        this.game = game;
        this.dirSlash = dirSlash;
        this.world = world;
        this.x = x;
        this.y = y;
        collisionRect = new CollisionRect(this.game, this.x, this.y, this.BLOCK_W, this.BLOCK_H);
        LSG = new Texture("green" + dirSlash + "mouse_left_still_64.png");
        LLG = new Texture("green" + dirSlash + "mouse_left_left_64.png");
        LRG = new Texture("green" + dirSlash + "mouse_left_right_64.png");
        RSG = new Texture("green" + dirSlash + "mouse_right_still_64.png");
        RLG = new Texture("green" + dirSlash + "mouse_right_left_64.png");
        RRG = new Texture("green" + dirSlash + "mouse_right_right_64.png");
        LSR = new Texture("red" + dirSlash + "mouse_left_red_still_64.png");
        LLR = new Texture("red" + dirSlash + "mouse_left_red_left_64.png");
        LRR = new Texture("red" + dirSlash + "mouse_left_red_right_64.png");
        RSR = new Texture("red" + dirSlash + "mouse_right_red_still_64.png");
        RLR = new Texture("red" + dirSlash + "mouse_right_red_left_64.png");
        RRR = new Texture("red" + dirSlash + "mouse_right_red_right_64.png");
        LSW = new Texture("white" + dirSlash + "mouse_left_white_still_64.png");
        LLW = new Texture("white" + dirSlash + "mouse_left_white_left_64.png");
        LRW = new Texture("white" + dirSlash + "mouse_left_white_right_64.png");
        RSW = new Texture("white" + dirSlash + "mouse_right_white_still_64.png");
        RLW = new Texture("white" + dirSlash + "mouse_right_white_left_64.png");
        RRW = new Texture("white" + dirSlash + "mouse_right_white_right_64.png");
        ALLDG = new Texture("animation" + dirSlash + "green" + dirSlash + "left" + dirSlash + "left_left-down_64.png");
        ALLUG = new Texture("animation" + dirSlash + "green" + dirSlash + "left" + dirSlash + "left_left-up_64.png");
        ARRDG = new Texture("animation" + dirSlash + "green" + dirSlash + "right" + dirSlash + "right_right-down_64.png");
        ARRUG = new Texture("animation" + dirSlash + "green" + dirSlash + "right" + dirSlash + "right_right-up_64.png");
        AUG = new Texture("animation" + dirSlash + "green" + dirSlash + "up_64.png");
        ALLDR = new Texture("animation" + dirSlash + "red" + dirSlash + "left" + dirSlash + "left_left-down_64.png");
        ALLUR = new Texture("animation" + dirSlash + "red" + dirSlash + "left" + dirSlash + "left_left-up_64.png");
        ARRDR = new Texture("animation" + dirSlash + "red" + dirSlash + "right" + dirSlash + "right_right-down_64.png");
        ARRUR = new Texture("animation" + dirSlash + "red" + dirSlash + "right" + dirSlash + "right_right-up_64.png");
        AUR = new Texture("animation" + dirSlash + "red" + dirSlash + "up_64.png");
        ALLDW = new Texture("animation" + dirSlash + "white" + dirSlash + "left" + dirSlash + "left_left-down_64.png");
        ALLUW = new Texture("animation" + dirSlash + "white" + dirSlash + "left" + dirSlash + "left_left-up_64.png");
        ARRDW = new Texture("animation" + dirSlash + "white" + dirSlash + "right" + dirSlash + "right_right-down_64.png");
        ARRUW = new Texture("animation" + dirSlash + "white" + dirSlash + "right" + dirSlash + "right_right-up_64.png");
        AUW = new Texture("animation" + dirSlash + "white" + dirSlash + "up_64.png");

        state = 0;
        memory = new char[2];

        texture = RSG;
        sprite = new Sprite(texture);
        previousMove = 3;
        directionFacing = false;
        moveTimer = 0.0f;
        jumpTimer = 0.0f;
        catchTimer = 0.0f;
        sprite.setPosition(this.x, this.y);

        this.catches = new ArrayList<>();

        moveFlag = true;
        jumpFlag = true;
        catchFlag = true;
        catchAnimationFlag = false;

        bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef tileFixture = new FixtureDef();
        bodyDef.position.set((this.x + BLOCK_W/2)/PIXELS_IN_METER, (this.y + BLOCK_H/2)/PIXELS_IN_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        polygonShape.setAsBox(BLOCK_W/2/PIXELS_IN_METER, BLOCK_H/2/PIXELS_IN_METER);
        tileFixture.shape = polygonShape;
        tileFixture.density = 5f;
        tileFixture.friction = 1f;
        tileFixture.restitution = 0.0f;
        body.createFixture(tileFixture);

        stepSound = Gdx.audio.newSound(Gdx.files.internal("stepSound.mp3"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));

        animationTimer = 0.0f;
        animationStateCounter = 3;
    }

    public void update(float delta) {
        //timer updates
        moveTimer += delta;
        jumpTimer += delta;
        catchTimer += delta;

        if (moveTimer > MOVEMENT_TIMER) {
            moveFlag = true;
        }
        if (jumpTimer > JUMP_TIMER) {
            jumpFlag = true;
        }
        if (catchTimer > CATCH_TIMER) {
            catchFlag = true;
        }

        if (moveFlag) {
            moveTimer = 0;
            if (jumpFlag && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                jumpTimer = 0;

                body.applyLinearImpulse(new Vector2(0f, JUMP_VEL), body.getPosition(), true);
                this.move = 6;
                jumpFlag = false;
                jumpSound.play();
            }

            boolean didMove = false;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {//left
                if (body.getLinearVelocity().x > -2 && body.getLinearVelocity().x < 2) {
                    body.applyLinearImpulse(new Vector2(-30, 0), body.getWorldCenter(), true);
                    didMove = true;
                }

                this.directionFacing = true;

                if (this.previousMove == this.move && this.previousMove == 1) {
                    this.move = 2;
                } else {
                    this.move = 1;
                }
            }
            if (Gdx.input.isKeyPressed((Input.Keys.D))) {//right
                if (body.getLinearVelocity().x > -2 && body.getLinearVelocity().x < 2) {
                    body.applyLinearImpulse(new Vector2(30, 0), body.getWorldCenter(), true);
                    didMove = true;
                }

                this.directionFacing = false;

                if (this.previousMove == this.move && this.previousMove == 4) {
                    this.move = 5;
                } else {
                    this.move = 4;
                }
            }

            if (didMove) {
                stepSound.play(0.25f);
            }

            if (catchFlag && Gdx.input.isKeyPressed(Input.Keys.C)) {//catch
                this.catchTimer = 0;
                catchAnimationFlag = true;

                if (directionFacing) {//left
                    this.catches.add(new Catch(this.game, this.x, this.y, directionFacing));
                } else {//right
                    this.catches.add(new Catch(this.game, this.x + this.BLOCK_W, this.y, directionFacing));
                }
                this.moveTimer = 9;
                catchFlag = false;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                //malloc -> red
                for (int i = 0; i < memory.length; i++) {
                    if (memory[i] == 0) {
                        memory[i] = 'm';
                        break;
                    }
                }
                this.state = 1;
                this.moveTimer = 7;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                //free -> white
                for (int i = 0; i < memory.length; i++) {
                    if (memory[i] == 0) {
                        memory[i] = 'f';
                        break;
                    }
                }
                this.state = 2;
                this.moveTimer = 8;
            }

            //update color
            if (this.state == 0) {//green
                //update sprite
                if (move != 0 && move != 3) {//check for actual movement
                    if (directionFacing) {//left 1-2
                        if (this.previousMove == 1) {
                            //render left right
                            texture = LRG;
                        } else if (this.previousMove == 2) {
                            //render left left
                            texture = LLG;
                        }
                    } else {//right 4-5
                        if (this.previousMove == 4) {
                            //render right right
                            texture = RRG;
                        } else if (this.previousMove == 5) {
                            //render right left
                            texture = RLG;
                        }
                    }
                } else {
                    if (directionFacing) {
                        //render left still
                        texture = LSG;
                    } else {
                        //render right still
                        texture = RSG;
                    }
                }
            } else if (this.state == 1) {//red
                //update sprite
                if (move != 0 && move != 3) {//check for actual movement
                    if (directionFacing) {//left 1-2
                        if (this.previousMove == 1) {
                            //render left right
                            texture = LRR;
                        } else if (this.previousMove == 2) {
                            //render left left
                            texture = LLR;
                        }
                    } else {//right 4-5
                        if (this.previousMove == 4) {
                            //render right right
                            texture = RRR;
                        } else if (this.previousMove == 5) {
                            //render right left
                            texture = RLR;
                        }
                    }
                } else {
                    if (directionFacing) {
                        //render left still
                        texture = LSR;
                    } else {
                        //render right still
                        texture = RSR;
                    }
                }
            } else if (this.state == 2) {//white
                //update sprite
                if (move != 0 && move != 3) {//check for actual movement
                    if (directionFacing) {//left 1-2
                        if (this.previousMove == 1) {
                            //render left right
                            texture = LRW;
                        } else if (this.previousMove == 2) {
                            //render left left
                            texture = LLW;
                        }
                    } else {//right 4-5
                        if (this.previousMove == 4) {
                            //render right right
                            texture = RRW;
                        } else if (this.previousMove == 5) {
                            //render right left
                            texture = RLW;
                        }
                    }
                } else {
                    if (directionFacing) {
                        //render left still
                        texture = LSW;
                    } else {
                        //render right still
                        texture = RSW;
                    }
                }
            }

            //update position
            this.x = body.getPosition().x*PIXELS_IN_METER - BLOCK_W/2;
            this.y = body.getPosition().y*PIXELS_IN_METER - BLOCK_H/2;

            sprite.setPosition(this.x, this.y);
            this.previousMove = this.move;
            this.collisionRect.move(this.x, this.y);
            moveFlag = false;
        }

        if (catchAnimationFlag) {
            catchAnimationTimer += delta;
            if (catchAnimationTimer >= CATCH_ANIMATION_TIMER) {
                catchAnimationTimer = 0;
                catchAnimationFlag = false;
            }

            if (directionFacing) {//left
                if (this.state == 0) {//green
                    texture = ALLUG;
                } else if (this.state == 1) {//red
                    texture = ALLUR;
                } else if (this.state == 2) {//white
                    texture = ALLUW;
                }
            } else {//right
                if (this.state == 0) {//green
                    texture = ARRUG;
                } else if (this.state == 1) {//red
                    texture = ARRUR;
                } else if (this.state == 2) {//white
                    texture = ARRUW;
                }
            }
        }

        sprite.setTexture(texture);

        if (!catches.isEmpty()) {
            for (Catch c : catches) {
                c.update();
            }
        }
    }

    public boolean playEndAnimation(float delta, ArrayList<Float> globePosition) {
        animationTimer += delta;
        if (animationTimer > ANIMATION_TIMER) {
            animationTimer = 0;
            globePosition.clear();

            if (state == 0) {//green
                if (animationStateCounter == 3) {
                    this.texture = AUG;
                    globePosition.add(this.x + texture.getWidth()/2);
                    globePosition.add(this.y + BLOCK_H + 16);
                } else if (animationStateCounter == 2) {
                    this.texture = ALLUG;
                    globePosition.add(this.x - 8);
                    globePosition.add(this.y + BLOCK_H + 8);
                } else if (animationStateCounter == 1) {
                    this.texture = ALLDG;
                    globePosition.add(this.x - 8);
                    globePosition.add(this.y);
                }
            } else if (state == 1) {//red
                if (animationStateCounter == 3) {
                    this.texture = AUR;
                    globePosition.add(this.x + texture.getWidth()/2);
                    globePosition.add(this.y + BLOCK_H + 16);
                } else if (animationStateCounter == 2) {
                    this.texture = ALLUR;
                    globePosition.add(this.x - 8);
                    globePosition.add(this.y + BLOCK_H + 8);
                } else if (animationStateCounter == 1) {
                    this.texture = ALLDR;
                    globePosition.add(this.x - 8);
                    globePosition.add(this.y);
                }
            } else if (state == 2) {//white
                if (animationStateCounter == 3) {
                    this.texture = AUW;
                    globePosition.add(this.x + texture.getWidth()/2);
                    globePosition.add(this.y + BLOCK_H + 16);
                } else if (animationStateCounter == 2) {
                    this.texture = ALLUW;
                    globePosition.add(this.x - 8);
                    globePosition.add(this.y + BLOCK_H + 8);
                } else if (animationStateCounter == 1) {
                    this.texture = ALLDW;
                    globePosition.add(this.x - 8);
                    globePosition.add(this.y);
                }
            }

            sprite.setTexture(texture);
            animationStateCounter--;
        }

        return animationStateCounter <= 0;
    }

    public ArrayList<Catch> getCatches() {
        return catches;
    }

    public boolean isDirectionFacing() {
        return directionFacing;
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public boolean isValidEndState() {
        if (memory[0] != 0 && memory[1] != 0) {
            return (memory[0] == 'm' && memory[1] == 'f');
        } else {
            return false;
        }
    }

    public void renderPC() {
        sprite.draw(game.batch);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getBLOCK_H() {
        return BLOCK_H;
    }

    public int getBLOCK_W() {
        return BLOCK_W;
    }
}