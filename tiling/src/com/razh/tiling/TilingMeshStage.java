package com.razh.tiling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

public class TilingMeshStage extends MeshStage {
	private float mScale;
	private float mStroke;

	private MeshGroup mColorRoot;
	private ShaderProgram mColorShaderProgram;
	private ShaderProgram mPointLightShaderProgram;

	private SnapshotArray<Light> mLights;

	public TilingMeshStage() {
		super();

		setScale(1.0f);

		mColorRoot = new MeshGroup();
		mColorRoot.setStage(this);

		mLights = new SnapshotArray<Light>(Light.class);
	}

	@Override
	public void draw() {
		getCamera().update();

		// Render normal objects.
		if (getShaderProgram() != null) {
			getShaderProgram().begin();
			getShaderProgram().setUniformMatrix("projectionMatrix", getCamera().projection);
			getShaderProgram().setUniformMatrix("viewMatrix", getCamera().view);

			getRoot().draw(getShaderProgram(), getStroke());

			getShaderProgram().end();
		}

		// Render multi-color objects.
		if (mColorShaderProgram != null) {
			mColorShaderProgram.begin();

			mColorShaderProgram.setUniformMatrix("projectionMatrix", getCamera().projection);
			mColorShaderProgram.setUniformMatrix("viewMatrix", getCamera().view);

			mColorRoot.draw(mColorShaderProgram, getStroke());

			mColorShaderProgram.end();
		}

		// Render light positions.
		if (mPointLightShaderProgram != null) {
			mPointLightShaderProgram.begin();

			mPointLightShaderProgram.setUniformMatrix("modelViewProjectionMatrix", getCamera().combined);

			Light[] lights = mLights.begin();
			for (int i = 0, n = mLights.size; i < n; i++) {
				Light light = lights[i];

				if (!light.isVisible()) {
					continue;
				}

				if (light instanceof PointLight) {
					light.draw(mPointLightShaderProgram);
				}
			}
			mLights.end();

			mPointLightShaderProgram.end();
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		mColorRoot.act(delta);

		Light[] lights = mLights.begin();
		for (int i = 0, n = mLights.size; i < n; i++) {
			Light light = lights[i];

			light.act(delta);
		}
		mLights.end();
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		Vector2 actorCoords = new Vector2(stageX, stageY);
		getRoot().parentToLocalCoordinates(actorCoords);
		Actor hit = getRoot().hit(actorCoords.x, actorCoords.y, touchable);
		if (hit == null) {
			return getColorRoot().hit(actorCoords.x, actorCoords.y, touchable);
		} else {
			return hit;
		}
	}

	public float getScale() {
		return mScale;
	}

	public void setScale(float scale) {
		if ( mScale != scale ) {
			setViewport(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight() / scale, false);
			getCamera().position.z = 10000.0f;
		}

		mScale = scale;
	}

	public float getStroke() {
		return mStroke;
	}

	public void setStroke(float stroke) {
		mStroke = stroke;
	}

	public MeshGroup getColorRoot() {
		return mColorRoot;
	}

	public void addColorActor(MeshActor actor) {
		mColorRoot.addActor(actor);
	}

	public SnapshotArray<Light> getLights() {
		return mLights;
	}

	public void addLight(Light light) {
		mLights.add(light);
	}

	public void setColorShaderProgram(ShaderProgram shaderProgram) {
		mColorShaderProgram = shaderProgram;
	}

	public void setPointLightShaderProgram(ShaderProgram shaderProgram) {
		mPointLightShaderProgram = shaderProgram;
	}

	@Override
	public void clearActors() {
		super.clearActors();
		mColorRoot.clear();
		mLights.clear();
	}
}
