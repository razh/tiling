package com.razh.tiling.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.razh.tiling.Player;
import com.razh.tiling.TilingMeshStage;

public abstract class BasicInputProcessor implements InputProcessor {
	private TilingMeshStage mStage;
	private Player mPlayer;

	public TilingMeshStage getStage() {
		return mStage;
	}

	public void setStage(TilingMeshStage stage) {
		mStage = stage;
	}

	public Player getPlayer() {
		return mPlayer;
	}

	public void setPlayer(Player player) {
		mPlayer = player;
	}

	public Vector2 screenToStageCoordinates(int screenX, int screenY) {
		if (mStage != null && mStage.getScale() != 1.0f) {
			float scaleInverse = 1.0f / mStage.getScale();
			return new Vector2(screenX * scaleInverse, (Gdx.graphics.getHeight() - screenY) * scaleInverse);
		}

		return new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
	}
}
