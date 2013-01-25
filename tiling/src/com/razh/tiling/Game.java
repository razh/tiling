package com.razh.tiling;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.SnapshotArray;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Game implements ApplicationListener {
	private MeshStage mStage;
	private FPSLogger mFPSLogger;
	private boolean mGL20;

	private ShaderProgram mShaderProgram;
	private Uniforms mUniforms;

	private boolean mShaderProgramNeedsUpdate;
	private boolean mLightUniformsNeedRefresh;

	private PointLight pLight;
	private MeshActor meshActor;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		mStage = new MeshStage();
		mFPSLogger = new FPSLogger();

		mGL20 = Gdx.graphics.isGL20Available();
		if (!mGL20) {
			Gdx.app.exit();
		}

		mShaderProgramNeedsUpdate = false;
		mLightUniformsNeedRefresh = false;
		mShaderProgram = Shader.createShaderProgram();
		mUniforms = new Uniforms();

		mStage.setShaderProgram(mShaderProgram);
		mStage.setPointLightShaderProgram(Shader.createPointLightShaderProgram());

		meshActor = new MeshActor();
		meshActor.setWidth(100.0f);
		meshActor.setHeight(100.0f);
		meshActor.setDepth(10.0f);
		meshActor.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 200, -100);
		meshActor.setColor(new Color(Color.BLUE).add(new Color(0.25f,0.0f,0.0f,0.0f)));
		meshActor.setMesh(Geometry.createTriangularBipyramid());
		meshActor.addAction(
			forever(
				sequence(
					moveBy(200, 200, 2.0f, Interpolation.pow2),
					moveBy(-200, 200, 2.0f, Interpolation.pow2),
					moveBy(-200, -200, 2.0f, Interpolation.pow2),
					moveBy(200, -200, 2.0f, Interpolation.pow2)
				)
			)
		);
		mStage.addActor(meshActor);

		MeshActor meshActor2 = new MeshActor();
		meshActor2.setWidth(100.0f);
		meshActor2.setHeight(100.0f);
		meshActor2.setDepth(10.0f);
		meshActor2.setPosition(200, 200, -100);
		meshActor2.setColor(new Color(Color.RED));
		meshActor2.setMesh(Geometry.createOctagonalBipyramid());
		mStage.addActor(meshActor2);

		MeshActor meshActor3 = new MeshActor();
		meshActor3.setWidth(100.0f);
		meshActor3.setHeight(100.0f);
		meshActor3.setDepth(10.0f);
		meshActor3.setPosition(800, 200, -100);
		meshActor3.setColor(new Color(Color.WHITE));
		meshActor3.setMesh(Geometry.createOctagonalBipyramid());
		mStage.addActor(meshActor3);

		AmbientLight aLight = new AmbientLight();
		aLight.setColor(0.25f, 0.25f, 0.25f, 1.0f);
		mStage.addLight(aLight);

		pLight = new PointLight();
		pLight.setColor(new Color(Color.RED));
		pLight.setPosition(200, Gdx.graphics.getHeight() / 2 + 50, -10);
		pLight.addAction(
			forever(
				sequence(
					moveBy(600, 0, 3.0f, Interpolation.pow2),
					moveBy(-600, 0, 3.0f, Interpolation.pow2)
				)
			)
		);
		pLight.setDistance(600);
		mStage.addLight(pLight);

		PointLight pLight2 = new PointLight();
		pLight2 = new PointLight();
		pLight2.setColor(new Color(Color.BLUE));
		pLight2.setPosition(800, Gdx.graphics.getHeight() / 2, -10);
		pLight2.setDistance(800);
		mStage.addLight(pLight2);
		mShaderProgramNeedsUpdate = true;
//		setupLights();
	}

	public void setupLights() {
		Color ambientLightColor = new Color();
		ArrayList<Color> pointLightColors = new ArrayList<Color>();
		ArrayList<Float> pointLightPositions = new ArrayList<Float>();
		ArrayList<Float> pointLightDistances = new ArrayList<Float>();
		int pointLightCount = 0;

		SnapshotArray<Light> children = mStage.getLights();
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
	public void dispose() {
	}

	@Override
	public void render() {
		update();

		Color backgroundColor = mStage.getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mStage.draw();

		mFPSLogger.log();
	}

	public void update() {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f);
		mStage.act(delta);
//		pLight.setPosition(Gdx.input.getX(), -Gdx.input.getY() + Gdx.graphics.getHeight() + 100, -10);
//		pLight.setPosition(Gdx.input.getX(), -Gdx.input.getY() + Gdx.graphics.getHeight(), 0);

		if (mStage.lightsNeedUpdate()) {
			mStage.setLightsNeedUpdate(false);
			setupLights();
		}

		setupLights();

		if (mShaderProgramNeedsUpdate) {
			mShaderProgramNeedsUpdate = false;
			mShaderProgram.dispose();
			mShaderProgram = Shader.createShaderProgram();
			mStage.setShaderProgram(mShaderProgram);
		}

		if (mLightUniformsNeedRefresh) {
			mLightUniformsNeedRefresh = false;
			mUniforms.setUniforms(mShaderProgram);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
