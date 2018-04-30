package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ChatBot {
    private final Sound a1 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\alert\\watch_out.mp3"));

    private final Sound c1 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\catch\\catch_it.mp3"));

    private final Sound j1 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\jump\\jump.mp3"));
    private final Sound j2 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\jump\\jump_now.mp3"));
    private final Sound j3 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\jump\\jump_much.mp3"));

    private final Sound t1 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\time\\any_day_now.mp3"));
    private final Sound t2 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\time\\at_this_rate.mp3"));
    private final Sound t3 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\time\\clock.mp3"));
    private final Sound t4 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\time\\hurry_up.mp3"));
    private final Sound t5 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\time\\time.mp3"));
    private final Sound t6 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\be_careful.mp3"));
    private final Sound t7 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\not_too_fast_now.mp3"));



    private final Sound q1 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\quotes\\no_regrets.mp3"));
    private final Sound q2 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\quotes\\pain.mp3"));
    private final Sound q3 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\quotes\\suffer.mp3"));



    private final Sound s1 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\didn't_see_that.mp3"));
    private final Sound s2 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\don't_hate_player.mp3"));
    private final Sound s3 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\I_can_do_better.mp3"));
    private final Sound s4 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\in_the_end.mp3"));
    private final Sound s5 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\maybe_next_time.mp3"));
    private final Sound s6 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\mislead.mp3"));
    private final Sound s7 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\not_that.mp3"));
    private final Sound s8 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\sorry_not_sorry.mp3"));
    private final Sound s9 = Gdx.audio.newSound(Gdx.files.internal("VOICE OVER\\sarcastic\\ouch.mp3"));


    private final float COMMENTARY_TIMER = 6.5f;
    private float commentTimer;

    private boolean isAlive;
    private int level;
    private ArrayList<Sound> commands;
    private ArrayList<Sound> comments;
    private ArrayList<Sound> quotes;

    public ChatBot(boolean isAlive, int level) {
        this.isAlive = isAlive;
        this.level = level;
        commentTimer = 0f;

        this.commands = new ArrayList<>();
        commands.add(a1);
        commands.add(c1);
        commands.add(j1);
        commands.add(j2);
        commands.add(j3);
        commands.add(t1);
        commands.add(t2);
        commands.add(t3);
        commands.add(t4);
        commands.add(t5);
        commands.add(t6);
        commands.add(t7);

        this.quotes = new ArrayList<>();
        quotes.add(q1);
        quotes.add(q2);
        quotes.add(q3);

        this.comments = new ArrayList<>();
        comments.add(s1);
        comments.add(s2);
        comments.add(s3);
        comments.add(s4);
        comments.add(s5);
        comments.add(s6);
        comments.add(s7);
        comments.add(s8);
        comments.add(s9);
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void stopAudio() {
        for (Sound s : commands) {
            s.stop();
        }
        for (Sound s : comments) {
            s.stop();
        }
        for (Sound s : quotes) {
            s.stop();
        }
    }
    public void think(float delta, float x, float y) {
        commentTimer += delta;
        if (commentTimer > COMMENTARY_TIMER) {
            commentTimer = 0;
            if (isAlive) {
                //it matters where on the map the player is
                this.mapBrain(x, y);
            } else {
                this.gameOverBrain();
            }
        }
    }

    private void mapBrain(float x, float y) {
        if (x > Gdx.graphics.getWidth()) {
            commands.get(ThreadLocalRandom.current().nextInt(0, commands.size()/2)).play();
        } else {
            commands.get(ThreadLocalRandom.current().nextInt(0, commands.size())).play();
        }
    }

    private void gameOverBrain() {
        boolean choice = ThreadLocalRandom.current().nextBoolean();
        if (choice) {//quote
            quotes.get(ThreadLocalRandom.current().nextInt(0, quotes.size())).play();
        } else {//comment
            comments.get(ThreadLocalRandom.current().nextInt(0, comments.size())).play();
        }
    }
}
