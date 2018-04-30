package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Alex241Intro;
import com.mygdx.game.tools.CollisionRect;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BinaryTreeBot {
    private final float BINARY_TREE_LAYERS = 3;
    private final int ROOT_X = 1348;
    private final int ROOT_Y = Gdx.graphics.getHeight() - 196;
    private final float EXCEPTION_SIZE = 16f;


    private Alex241Intro game;
    private ArrayList<SmartException> smartExceptions;
    World world;

    private int exceptionCounter;
    private final float EXCEPTION_MOVE_TIMER = 0.01f;
    private float exceptionMoveTimer;


    private final float MOVE_TIMER = 0.1f;
    private float moveTimer;



    private float START_DELAY;
    private float startDelayTimer;
    private boolean renderConnections;
    private HashMap<SmartException, SmartException> smartExceptionHashMap;

    public BinaryTreeBot(Alex241Intro game, World world) {
        this.game = game;
        this.world = world;
        smartExceptionHashMap = new HashMap<>();

        //any amount of layers for a binary tree can be added here
        smartExceptions = new ArrayList<>();
        int parentSelector = 0;
        for (int i = 0; i < BINARY_TREE_LAYERS; i++) {
            for (int j = 0; j < (int)(Math.pow((double) 2, (double) i)); j++) {
                if (i > 0) {
                    SmartException parent = smartExceptions.get(((int)(Math.pow((double) 2, (double) (i-1)))) - 1 + parentSelector);
                    float width = (j%2==0 ? (parent.position.x) - (EXCEPTION_SIZE * 2) : (parent.position.x) + (EXCEPTION_SIZE * 2));
                    SmartException child = new SmartException(game, new Vector2(width, (parent.position.y) - (EXCEPTION_SIZE * 2)));
                    smartExceptions.add(child);

                    smartExceptionHashMap.put(child, parent);

                    if ((j+1) % 2 == 0) {
                        parentSelector++;
                    }
                } else {
                    //root
                    smartExceptions.add(new SmartException(game, new Vector2(ROOT_X, ROOT_Y)));
                }
            }
            parentSelector = 0;
        }

        START_DELAY = ThreadLocalRandom.current().nextFloat()*10 + 5f;
        startDelayTimer = 0;
        moveTimer = 0f;
        exceptionMoveTimer = 0;
        exceptionCounter = smartExceptions.size();
        renderConnections = true;
    }

    private int think(PC player, float delta) {
        startDelayTimer += delta;
        if (startDelayTimer > START_DELAY) {
            renderConnections = false;
            /**update the exceptions*/
            for (int i = 0; i < smartExceptions.size(); i++) {
                smartExceptions.get(i).chase(player);
            }
        }

        return exceptionCounter;
    }

    public int update(PC player, float delta) {
        //think
        return this.think(player, delta);
    }

    public ArrayList<SmartException> getExceptions() {
        return smartExceptions;
    }

    public void setExceptions(ArrayList<SmartException> exceptions) {
        this.smartExceptions = exceptions;
        exceptionCounter = this.smartExceptions.size();
    }

    public void renderAllExceptions() {
        for (int i = 0; i < smartExceptions.size(); i++) {
            smartExceptions.get(i).render(game.batch);
        }
    }

    public void printPositions() {
        for (int i = 0; i < smartExceptions.size(); i++) {
            System.out.println("X\t" + smartExceptions.get(i).position.x);
            System.out.println("Y\t" + smartExceptions.get(i).position.y);
        }
    }

    public void renderAllConnections() {
        if (renderConnections) {
            //root
            smartExceptions.get(0).renderConnection(true, 0f, 0f);

            Iterator iterator = smartExceptionHashMap.keySet().iterator();
            while (iterator.hasNext()) {
                SmartException key = (SmartException) iterator.next();
                SmartException parent = smartExceptionHashMap.get(key);
                key.renderConnection(false, parent.position.x, parent.position.y);
            }
        }
    }
}
