package com.razh.tiling.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.tiling.AmbientLight;
import com.razh.tiling.Light;
import com.razh.tiling.PointLight;
import com.razh.tiling.Shader;
import com.razh.tiling.TilingGame;
import com.razh.tiling.TilingMeshStage;
import com.razh.tiling.Uniforms;
import com.razh.tiling.TilingGame.LightingModel;

public class GameScreen extends DefaultScreen {
	private ShaderProgram mShaderProgram;
	private ShaderProgram mColorShaderProgram;
	private Uniforms mUniforms;

	private boolean mShaderProgramNeedsUpdate;
	private boolean mLightUniformsNeedRefresh;

	public GameScreen() {
		setStage(new TilingMeshStage());

		getStage().getCamera().position.z = 10000.0f;
		getStage().getCamera().far = 15000.0f;

		// Set shader programs.
		if (TilingGame.lightingModel == LightingModel.PHONG) {
			mShaderProgram = Shader.createPhongShaderProgram();
			mColorShaderProgram = Shader.createColorPhongShaderProgram();
		} else if (TilingGame.lightingModel == LightingModel.LAMBERT) {
			mShaderProgram = Shader.createLambertShaderProgram();
			mColorShaderProgram = Shader.createColorLambertShaderProgram();
		}
		mUniforms = new Uniforms();

		getMeshStage().setShaderProgram(mShaderProgram);
		((TilingMeshStage) getStage()).setPointLightShaderProgram(Shader.createBillboardShaderProgram());
		((TilingMeshStage) getStage()).setColorShaderProgram(mColorShaderProgram);
		mShaderProgramNeedsUpdate = true;
	}

	@Override
	public void render(float delta) {
		update(delta);

		Color backgroundColor = getMeshStage().getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		getStage().draw();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}

	public void update(float delta) {
		getStage().act(delta);

		setupLights();

		if (mShaderProgramNeedsUpdate) {
			mShaderProgramNeedsUpdate = false;
			mShaderProgram.dispose();
			mColorShaderProgram.dispose();

			if (TilingGame.lightingModel == LightingModel.PHONG) {
				mShaderProgram = Shader.createPhongShaderProgram();
				mColorShaderProgram = Shader.createColorPhongShaderProgram();
			} else if (TilingGame.lightingModel == LightingModel.LAMBERT) {
				mShaderProgram = Shader.createLambertShaderProgram();
				mColorShaderProgram = Shader.createColorLambertShaderProgram();
			}

			getMeshStage().setShaderProgram(mShaderProgram);
			((TilingMeshStage) getStage()).setColorShaderProgram(mColorShaderProgram);
		}

		if (mLightUniformsNeedRefresh) {
			mLightUniformsNeedRefresh = false;
			mUniforms.setUniforms(mShaderProgram);
			mUniforms.setUniforms(mColorShaderProgram);
		}
	}

	public void setupLights() {
		Color ambientLightColor = new Color();
		ArrayList<Color> pointLightColors = new ArrayList<Color>();
		ArrayList<Float> pointLightPositions = new ArrayList<Float>();
		ArrayList<Float> pointLightDistances = new ArrayList<Float>();
		int pointLightCount = 0;

		SnapshotArray<Light> children = ((TilingMeshStage) getStage()).getLights();
		Light[] lights = children.begin();
		Vector3 position;
		for (int i = 0, n = children.size; i < n; i++) {
			Light light = lights[i];

			if (!light.isVisible()) {
				continue;
			}

			// Ambient light color is sum of all ambient lights.
			if (light instanceof AmbientLight) {
				ambientLightColor.add(light.getColor());
			} else if (light instanceof PointLight) {
				if (!light.isVisible()) {
					continue;
				}

				pointLightCount++;

				pointLightColors.add(light.getColor());

				position = light.getPosition();
				pointLightPositions.add(position.x);
				pointLightPositions.add(position.y);
				pointLightPositions.add(position.z);

				pointLightDistances.add(((PointLight) light).getDistance());
			}
		}
		children.end();

		if (Shader.MAX_POINT_LIGHTS != pointLightCount) {
			mShaderProgramNeedsUpdate = true;
			Shader.MAX_POINT_LIGHTS = pointLightCount;
		}

		mUniforms.setAmbientLightColor(ambientLightColor);
		if (pointLightCount > 0) {
			mUniforms.setPointLightColors(pointLightColors);
			mUniforms.setPointLightPositions(pointLightPositions);
			mUniforms.setPointLightDistances(pointLightDistances);
			mLightUniformsNeedRefresh = true;
		}
	}


	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		mShaderProgram.dispose();
		mColorShaderProgram.dispose();
	}
}
