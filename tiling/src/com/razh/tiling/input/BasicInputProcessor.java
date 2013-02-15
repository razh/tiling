package com.razh.tiling.input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.razh.tiling.MeshStage;
import com.razh.tiling.Player;

public abstract class BasicInputProcessor implements InputProcessor {
	private Game mGame;
	private MeshStage mStage;
	private Player mPlayer;

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

	public Player getPlayer() {
		return mPlayer;
	}

	public void setPlayer(Player player) {
		mPlayer = player;
	}

	public Vector2 screenToStageCoordinates(int screenX, int screenY) {
		return new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
	}
}
