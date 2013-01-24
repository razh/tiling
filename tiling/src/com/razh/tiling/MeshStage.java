package com.razh.tiling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;

public class MeshStage extends Stage {
	private MeshGroup mRoot;
	private ShaderProgram mShaderProgram;
	private ShaderProgram mPointLightShaderProgram;
	private Matrix3 mNormalMatrix;
	private Matrix4 mViewMatrix;

	private SnapshotArray<Light> mLights;
	private boolean mLightsNeedUpdate;

	// Allows us to set colors and stuff with actions.
	private Actor mColorActor;

	public MeshStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public MeshStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
		if (width != Gdx.graphics.getWidth() && height != Gdx.graphics.getHeight()) {
			setCamera(new OrthographicCamera());
			setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), stretch);
		}

		mRoot = new MeshGroup();
		mRoot.setStage(this);

		mLights = new SnapshotArray<Light>(Light.class);
		mLightsNeedUpdate = false;

		mColorActor = new Actor();
		mColorActor.setColor(Color.BLACK);

		mNormalMatrix = new Matrix3();
		mViewMatrix = new Matrix4();
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}

	public void setPointLightShaderProgram(ShaderProgram shaderProgram) {
		mPointLightShaderProgram = shaderProgram;
	}

	public void draw(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;

		draw();
	}

	@Override
	public void draw() {
		if (mShaderProgram == null) {
			return;
		}

		getCamera().update();

		mShaderProgram.begin();
		mShaderProgram.setUniformMatrix("projectionMatrix", getCamera().projection);
		mShaderProgram.setUniformMatrix("modelViewMatrix", getCamera().view);

		mViewMatrix.idt().translate(getCamera().position).inv();
		mShaderProgram.setUniformMatrix("viewMatrix", mViewMatrix);
		mNormalMatrix.set(getCamera().view.cpy().inv());
		mShaderProgram.setUniformMatrix("normalMatrix", mNormalMatrix.transpose());

		mRoot.draw(mShaderProgram, 1.0f);

		mShaderProgram.end();

		mPointLightShaderProgram.begin();
		mPointLightShaderProgram.setUniformMatrix("modelViewProjectionMatrix", getCamera().combined);
		Light[] lights = mLights.begin();
		for (int i = 0, n = mLights.size; i < n; i++) {
			Light light = lights[i];

			if (!light.isVisible()) {
				continue;
			}

			if (light instanceof PointLight) {
				light.draw(mPointLightShaderProgram, 1.0f);
			}
		}
		mLights.end();
		mPointLightShaderProgram.end();
	}

	@Override
	public void addActor(Actor actor) {
		mRoot.addActor(actor);
	}

	public void addLight(Light light) {
		mLights.add(light);
		mLightsNeedUpdate = true;
	}

	@Override
	public void act(float delta) {
		mRoot.act(delta);

		Light[] lights = mLights.begin();
		for (int i = 0, n = mLights.size; i < n; i++) {
			Light light = lights[i];

			light.act(delta);
		}
		mLights.end();

		mColorActor.act(delta);
	}

	@Override
	public MeshGroup getRoot() {
		return mRoot;
	}

	public Color getColor() {
		return mColorActor.getColor();
	}

	public void setColor(Color color) {
		mColorActor.setColor(color);
	}

	public SnapshotArray<Light> getLights() {
		return mLights;
	}

	public boolean lightsNeedUpdate() {
		return mLightsNeedUpdate;
	}

	public void setLightsNeedUpdate(boolean needsUpdate) {
		mLightsNeedUpdate = needsUpdate;
	}

	@Override
	public void addAction(Action action) {
		mColorActor.addAction(action);
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		Vector2 actorCoords = Vector2.tmp;
		getRoot().parentToLocalCoordinates(actorCoords.set(stageX, stageY));
		return getRoot().hit(actorCoords.x, actorCoords.y, touchable);
	}

	public void clearActors() {
		mRoot.clear();
	}
}
