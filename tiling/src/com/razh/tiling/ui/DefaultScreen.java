package com.razh.tiling.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.razh.tiling.MeshStage;

public abstract class DefaultScreen implements Screen {
	private Game mGame;
	private Stage mStage;

	public Game getGame() {
		return mGame;
	}

	public void setGame(Game game) {
		mGame = game;
	}

	public Stage getStage() {
		return mStage;
	}

	public MeshStage getMeshStage() {
		if (getStage() instanceof MeshStage) {
			return (MeshStage) getStage();
		}

		return null;
	}

	public void setStage(Stage stage) {
		mStage = stage;
	}

	@Override
	public void dispose() {
		mStage.dispose();
	}
}
