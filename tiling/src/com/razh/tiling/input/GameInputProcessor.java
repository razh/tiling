package com.razh.tiling.input;

import com.badlogic.gdx.math.Vector2;
import com.razh.tiling.MeshActor;
import com.razh.tiling.logic.TilingEntity;

public class GameInputProcessor extends BasicInputProcessor {

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

		MeshActor hit = (MeshActor) getStage().hit(point.x, point.y, true);
		if (hit != null) {
			getPlayer().setSelected(hit);
			if (hit.hasEntity()) {
				((TilingEntity) hit.getEntity()).touch();
			}
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
