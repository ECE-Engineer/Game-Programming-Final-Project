package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.MainMenuScreen;

public class Alex241Intro extends Game {
	public SpriteBatch batch;
//	public Texture img;
	public final int WIDTH = 1800;
	public final int HEIGHT = 1000;
	public boolean IS_MOBILE = false;
	private String dirSlash;

	public Alex241Intro(String dirSlash) {
		this.dirSlash = dirSlash;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();

		if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
			IS_MOBILE = true;
		}

		this.setScreen(new MainMenuScreen(this, dirSlash));//(new ReadyEEGTestScreen(this));//

//		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		super.render();
	}
	
//	@Override
//	public void dispose () {
//		batch.dispose();
//		img.dispose();
//	}
}
