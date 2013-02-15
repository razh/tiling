package com.razh.tiling.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.razh.tiling.Light;
import com.razh.tiling.PointLight;
import com.razh.tiling.TilingGame;
import com.razh.tiling.TilingMeshStage;

public class DebugInputProcessor extends BasicInputProcessor {
	private int mLightIndex;
	private Vector2 mPosition;

	public DebugInputProcessor() {
		// By default, the ambient light is the first light, so we start the index of PointLights at 1.
		mLightIndex = -1;
		mPosition = new Vector2();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Input.Keys.NUM_0 <= keycode && keycode <= Input.Keys.NUM_9) {
			mLightIndex = keycode - 7;
			if (mLightIndex == 0) {
				mLightIndex = 10;
			}
		}

		if (keycode == Input.Keys.L) {
			TilingMeshStage stage = (TilingMeshStage) getStage();
			Light[] lights = stage.getLights().begin();
			for (int i = 0, n = stage.getLights().size; i < n; i++) {
				if (lights[i] instanceof PointLight) {
					System.out.println(i + ": " + lights[i].getPosition());
				}
			}
			stage.getLights().end();
		}

		if (keycode == Input.Keys.D) {
			TilingGame.DEBUG = !TilingGame.DEBUG;
		}

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
		mPosition.set(screenToStageCoordinates(screenX, screenY));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		TilingMeshStage stage = (TilingMeshStage) getStage();
		if (getStage() == null) {
			return false;
		}

		Vector2 point = screenToStageCoordinates(screenX, screenY);
		if (1 <= mLightIndex && mLightIndex < stage.getLights().size) {
			Light light = stage.getLights().get(mLightIndex);
			if (light instanceof PointLight) {
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
					light.translate(0, 0, point.x - mPosition.x);
				} else {
					light.translate(point.x - mPosition.x, point.y - mPosition.y);
				}
			}
		}

		mPosition.set(point);

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