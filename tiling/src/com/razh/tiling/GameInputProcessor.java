package com.razh.tiling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameInputProcessor implements InputProcessor {
	private MeshStage mStage;
	private Player mPlayer;

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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (mStage == null || mPlayer == null) {
			return false;
		}

		Vector2 point = screenToStageCoordinates(screenX, screenY);

		Actor hit = mStage.getRoot().hit(point.x, point.y, true);
		if (hit != null) {
			mPlayer.setSelected(hit);
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (mPlayer == null) {
			return false;
		}

		if (mPlayer.getSelected() != null) {
			mPlayer.setSelected(null);
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (mStage == null || mPlayer == null) {
			return false;
		}

		Vector2 point = screenToStageCoordinates(screenX, screenY);

		Actor selected = mPlayer.getSelected();
		if (selected != null) {
			selected.setPosition(point.x, point.y);
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


	private Vector2 screenToStageCoordinates(int screenX, int screenY) {
		return new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
	}
}
