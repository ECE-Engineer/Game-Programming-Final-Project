package com.mygdx.game.screens;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Texture;
        import com.mygdx.game.Alex241Intro;

        import java.io.*;
        import java.util.ArrayList;
        import java.util.Scanner;
        import java.util.concurrent.ThreadLocalRandom;

public class SaveScreen implements Screen {
    /*
     * Data is stored in the following format:
     * ------later on you can now easily remove the data that is corrupted b/c it won't have a third column
     * when creating this -> insert commas at the beginning if not the first column
     */
    /**(TIME FRUIT OCCURRED AT),(THE NAME OF THE RANDOMLY SELECTED FRUIT),(TIME DIFFERENCE SINCE THE TIME THE FRUIT OCCURRED AT WHEN THE USER PRESSES THE SPACEBAR)*/

    private Alex241Intro game;

    public SaveScreen(Alex241Intro game, String s1, String s2) throws FileNotFoundException {
        this.game = game;

        File file;
        File spaceBarFile;
        File finalFile;

        file = new File(s1);
        spaceBarFile = new File(s2);

        int counter = 0;
        finalFile = new File("C:\\Users\\Kyle\\Desktop\\RESEARCH\\DATA\\final\\Data_" + counter + ".csv");
        while (finalFile.exists()) {
            counter++;
            finalFile = new File("C:\\Users\\Kyle\\Desktop\\RESEARCH\\DATA\\final\\Data_" + counter + ".csv");
        }


        //from the space bar file, write to a new file the final data
        ArrayList<String[]> strings = new ArrayList<>();
        Scanner fileScanner = new Scanner(spaceBarFile);
        while(fileScanner.hasNextLine()) {
            String currentLine = fileScanner.nextLine();
            String[] line = currentLine.split(",");
            if (line.length == 2) {
                strings.add(line);
            }
        }

        ArrayList<String[]> allFruitData = new ArrayList<>();
        Scanner fileScanner1 = new Scanner(file);
        while(fileScanner1.hasNextLine()) {
            String currentLine = fileScanner1.nextLine();
            String[] line = currentLine.split(",");
            if (line.length == 2) {
                allFruitData.add(line);
            }
        }

        int clickIndex = 0;
        ArrayList<Integer> markers = new ArrayList<>();
        ArrayList<String> clicks = new ArrayList<>();

        for (int j = 0; j < strings.size(); j++) {
            if (j == clickIndex) {
                float overallTime = Float.parseFloat(strings.get(j)[1]);//CLICK overall time

                for (int i = 0; i < allFruitData.size(); i++) {
                    float imageOverallSwitchTime = Float.parseFloat(allFruitData.get(i)[0]);

                    if (i < allFruitData.size() - 1) {
                        if (j < strings.size() - 1) {
                            float nextOverallTime = Float.parseFloat(strings.get((j+1))[1]);//CLICK overall time
                            if ((overallTime >= imageOverallSwitchTime) && (overallTime < Float.parseFloat(allFruitData.get((i + 1))[0])) && (nextOverallTime >= imageOverallSwitchTime) && (nextOverallTime < Float.parseFloat(allFruitData.get((i + 1))[0]))) {
                                markers.add(i);
                                String temp = "";
                                if (Float.parseFloat(strings.get(j)[0]) > 0) {
                                    temp = strings.get(clickIndex)[0] + "," + strings.get(clickIndex)[1];
                                }
                                clicks.add(temp);
                                clickIndex += 2;
                                System.out.println("click time/s\t" + overallTime + "\t" + nextOverallTime);
                                System.out.println("overall time/s\t" + imageOverallSwitchTime + "\t" + Float.parseFloat(allFruitData.get((i + 1))[0]));
                                break;
                            } else if ((overallTime >= imageOverallSwitchTime) && (overallTime < Float.parseFloat(allFruitData.get((i + 1))[0]))) {
                                markers.add(i);
                                String temp = "";
                                if (Float.parseFloat(strings.get(j)[0]) > 0) {
                                    temp = strings.get(clickIndex)[0] + "," + strings.get(clickIndex)[1];
                                }
                                clicks.add(temp);
                                clickIndex += 1;
                                break;
                            }
                        } else {
                            if ((overallTime >= imageOverallSwitchTime) && (overallTime < Float.parseFloat(allFruitData.get((i + 1))[0]))) {
                                markers.add(i);
                                String temp = "";
                                if (Float.parseFloat(strings.get(j)[0]) > 0) {
                                    temp = strings.get(clickIndex)[0] + "," + strings.get(clickIndex)[1];
                                }
                                clicks.add(temp);
                                clickIndex += 1;
                                break;
                            }
                        }
                    } else {
                        if (overallTime >= imageOverallSwitchTime) {
                            markers.add(i);
                            String temp = "";
                            if (Float.parseFloat(strings.get(j)[0]) > 0) {
                                temp = strings.get(clickIndex)[0] + "," + strings.get(clickIndex)[1];
                            }
                            clicks.add(temp);
                            clickIndex += 1;
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < allFruitData.size(); i++) {
            StringBuilder s = new StringBuilder(allFruitData.get(i)[0] + "," + allFruitData.get(i)[1]);
            if (markers.get(0) == i) {
                if (clicks.get(0).length() > 1) {
                    s.append(",").append(clicks.get(0));
                } else {
                    s.append(clicks.get(0));
                }
                markers.remove(0);
                clicks.remove(0);
            }
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter("C:\\Users\\Kyle\\Desktop\\RESEARCH\\DATA\\final\\Data_" + counter + ".csv", true);
                bw = new BufferedWriter(fw);
                //write to file
                bw.write(s.toString());
                bw.newLine();

                bw.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

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