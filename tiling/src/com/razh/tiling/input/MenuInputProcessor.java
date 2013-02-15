package com.razh.tiling.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.razh.tiling.MeshActor;

public class MenuInputProcessor extends BasicInputProcessor {
	// Offsets from touch position to object position.
	private Vector2 mOffset;

	public MenuInputProcessor() {
		mOffset = new Vector2();
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
		if (getStage() == null || getPlayer() == null) {
			return false;
		}

		Vector2 point = screenToStageCoordinates(screenX, screenY);

		Actor hit = getStage().hit(point.x, point.y, true);
		getPlayer().setSelected(hit);

		if (hit != null) {
			mOffset.set(point).sub(hit.getX(), hit.getY());
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (getPlayer() == null) {
			return false;
		}

		if (getPlayer().getSelected() != null) {
			getPlayer().setSelected(null);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (getStage() == null || getPlayer() == null) {
			return false;
		}

		Vector2 point = screenToStageCoordinates(screenX, screenY);

		Actor selected = getPlayer().getSelected();
		if (selected != null) {
			selected.setPosition(point.x - mOffset.x, point.y - mOffset.y);
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

}
