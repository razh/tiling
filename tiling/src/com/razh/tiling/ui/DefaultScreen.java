package com.razh.tiling.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.razh.tiling.MeshStage;

public abstract class DefaultScreen implements Screen {
	private Game mGame;
	private MeshStage mStage;

	public Game getGame() {
		return mGame;
	}

	public void setGame(Game game) {
		mGame = game;
	}

	public MeshStage getStage() {
		return mStage;
	}

	public void setStage(MeshStage stage) {
		mStage = stage;
	}

	@Override
	public void dispose() {
		mStage.dispose();
	}
}
