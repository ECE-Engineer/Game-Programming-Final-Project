package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Alex241Intro;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class SightModalityTest implements Screen {
    /*
    * Data is stored in the following format:
    * (TIME FRUIT OCCURRED AT),(THE NAME OF THE RANDOMLY SELECTED FRUIT)
    * ------later on you can now easily remove the data that is corrupted b/c it won't have a third column
    * when creating this -> insert commas at the beginning if not the first column
     */
    private final Texture fruit1 = new Texture("C:\\Users\\Kyle\\Desktop\\RESEARCH\\test_fruit\\Banana.jpg");
    private final Texture fruit2 = new Texture("C:\\Users\\Kyle\\Desktop\\RESEARCH\\test_fruit\\cherry.jpg");
    private final Texture fruit3 = new Texture("C:\\Users\\Kyle\\Desktop\\RESEARCH\\test_fruit\\grape.png");
    private final Texture fruit4 = new Texture("C:\\Users\\Kyle\\Desktop\\RESEARCH\\test_fruit\\kiwi.jpg");
    private final Texture fruit5 = new Texture("C:\\Users\\Kyle\\Desktop\\RESEARCH\\test_fruit\\orange.jpg");
    private final float IMAGE_TIMER = 1.0f;

    private Alex241Intro game;
    private Texture fruit;
    private File file;
    private File spaceBarFile;
    private String string;
    private String otherString;
    private float clock = 0.0f;
    private float timer = 0.0f;
    private boolean newFruit;
    private boolean run = true;
    private int previousFruit;
    private boolean oneFlag = true;
    private boolean runFlagOnce = true;

    public SightModalityTest(Alex241Intro game) {
        this.game = game;
        newFruit = true;

        int counter = 0;
        string = "C:\\Users\\Kyle\\Desktop\\RESEARCH\\DATA\\EEG_Test_" + counter + ".csv";
        file = new File(string);

        while (file.exists()) {
            counter++;
            string = "C:\\Users\\Kyle\\Desktop\\RESEARCH\\DATA\\EEG_Test_" + counter + ".csv";
            file = new File(string);
        }

        int counter2 = 0;
        otherString = "C:\\Users\\Kyle\\Desktop\\RESEARCH\\DATA\\space_bar_file_" + counter2 + ".csv";
        spaceBarFile = new File(otherString);

        while (spaceBarFile.exists()) {
            counter2++;
            otherString = "C:\\Users\\Kyle\\Desktop\\RESEARCH\\DATA\\space_bar_file_" + counter2 + ".csv";
            spaceBarFile = new File(otherString);
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (!run && runFlagOnce) {
            runFlagOnce = false;
            this.dispose();
            try {
                game.setScreen(new SaveScreen(game, string, otherString));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        if (run) {
            clock += delta;
            timer += delta;
            if (timer >= IMAGE_TIMER) {
                timer = 0.0f;
                newFruit = true;
            }

            boolean flag = false;
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                flag = true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                run = false;
            }

            if (flag) {
                FileWriter fw = null;
                BufferedWriter bw = null;
                try {
                    fw = new FileWriter(otherString, true);/*FORMAT OF THIS FILE IS: (TIME DIFFERENCE SINCE THE TIME THE FRUIT OCCURRED AT WHEN THE USER PRESSES THE SPACEBAR),(OVERALL TIME)*/
                    bw = new BufferedWriter(fw);
                    //write to file
                    bw.write("" + timer);
                    bw.write(",");
                    bw.write("" + clock);
                    bw.newLine();

                    bw.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            if (newFruit) {
                int fruitSelection = ThreadLocalRandom.current().nextInt(5);
                String fruit = "";
                if (fruitSelection == 0) {
                    fruit = "banana";
                    this.fruit = fruit1;
                } else if (fruitSelection == 1) {
                    fruit = "cherry";
                    this.fruit = fruit2;
                } else if (fruitSelection == 2) {
                    fruit = "grape";
                    this.fruit = fruit3;
                } else if (fruitSelection == 3) {
                    fruit = "kiwi";
                    this.fruit = fruit4;
                } else if (fruitSelection == 4) {
                    fruit = "orange";
                    this.fruit = fruit5;
                }

                if (oneFlag) {
                    previousFruit = fruitSelection;
                    oneFlag = false;
                } else {
                    while(previousFruit == fruitSelection) {
                        fruitSelection = ThreadLocalRandom.current().nextInt(5);
                        if (fruitSelection == 0) {
                            fruit = "banana";
                            this.fruit = fruit1;
                        } else if (fruitSelection == 1) {
                            fruit = "cherry";
                            this.fruit = fruit2;
                        } else if (fruitSelection == 2) {
                            fruit = "grape";
                            this.fruit = fruit3;
                        } else if (fruitSelection == 3) {
                            fruit = "kiwi";
                            this.fruit = fruit4;
                        } else if (fruitSelection == 4) {
                            fruit = "orange";
                            this.fruit = fruit5;
                        }
                    }
                    previousFruit = fruitSelection;
                }

                FileWriter fw = null;
                BufferedWriter bw = null;
                try {
                    fw = new FileWriter(string, true);
                    bw = new BufferedWriter(fw);
                    //write to file
                    bw.write("" + clock);
                    bw.write(",");
                    bw.write(fruit);
                    bw.newLine();

                    bw.close();
                } catch (IOException e){
                    e.printStackTrace();
                }

                newFruit = false;
            }

            game.batch.draw(this.fruit, 0, 0);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
//        dispose();
    }

    @Override
    public void dispose() {
//        world.dispose();
//        debugRenderer.dispose();
    }
}