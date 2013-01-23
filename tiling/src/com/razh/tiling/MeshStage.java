package com.razh.tiling;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;

public class MeshStage extends Stage {
	private MeshGroup mRoot;
	private ShaderProgram mShaderProgram;
	private Uniforms mUniforms;
	
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
		
		mLights = new SnapshotArray(Light.class);
		mLightsNeedUpdate = false;
		
		mColorActor = new Actor();
		mColorActor.setColor(Color.BLACK);
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}

	public void draw(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;

		draw();
	}

	public boolean done = false;
	@Override
	public void draw() {
		if (mShaderProgram == null) {
			return;
		}
	
		getCamera().update();
	
		mShaderProgram.begin();
		if (!done) {
		mShaderProgram.setUniformMatrix("projection", getCamera().combined);
		done = true;
		}

		mRoot.draw(mShaderProgram, 1.0f);
	
		mShaderProgram.end();
	}

	@Override
	public void addActor(Actor actor) {
		mRoot.addActor(actor);
	}
	
	public void addLight(Light light) {
		mLights.add(light);
		mLightsNeedUpdate = true;
	}

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
