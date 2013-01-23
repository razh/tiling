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

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		mStage = new MeshStage();
		mFPSLogger = new FPSLogger();

		mGL20 = Gdx.graphics.isGL20Available();
		if (!mGL20) {
			Gdx.app.exit();
		}

		mShaderProgram = new ShaderProgram(mVertexShader, mFragmentShader);
		System.out.println("Compiled: " + mShaderProgram.isCompiled());
		mUniforms = new Uniforms();

		mStage.setShaderProgram(mShaderProgram);
		
		MeshActor meshActor = new MeshActor();
		meshActor.setWidth(100.0f);
		meshActor.setHeight(100.0f);
		meshActor.setDepth(20.0f);
		meshActor.setPosition(0.0f, 0.0f, -meshActor.getDepth());
		meshActor.setColor(Color.BLUE);
		meshActor.setMesh(Geometry.createTriangularBipyramid());
		meshActor.addAction(
			sequence(
				moveBy(200, 100, 2.0f, Interpolation.pow2),
				moveBy(-100, 100, 2.0f, Interpolation.pow2)
			)
		);
		mStage.addActor(meshActor);
	}

	public void setupLights() {
		if (mShaderProgram == null) {
			return;
		}
		
		Color ambientLightColor = Color.CLEAR;
		ArrayList<Color> pointLightColors = new ArrayList<Color>();
		ArrayList<Vector3> pointLightPositions = new ArrayList<Vector3>();
		ArrayList<Float> pointLightDistances = new ArrayList<Float>();
		int pointLightCount = 0;
		
		SnapshotArray<Light> children = mStage.getLights();
		Light[] lights = (Light[]) children.begin();
		for (int i = 0, n = lights.length; i < n; i++) {
			Light light = lights[i];

			if (!light.isVisible()) {
				continue;
			}

			// Ambient light color is sum of all ambient lights.
			if (light instanceof AmbientLight) {
				ambientLightColor.add(light.getColor());
			} else if (light instanceof PointLight) {
				pointLightCount++;
				
				if (!light.isVisible()) {
					continue;
				}

				pointLightColors.add(light.getColor());
				pointLightPositions.add(light.getPosition());
				pointLightDistances.add(((PointLight) light).getDistance());
			}
		}
		children.end();

		mUniforms.ambientLightColor = ambientLightColor;
		
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
