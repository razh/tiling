package com.razh.tiling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MeshStage extends Stage {
	private MeshGroup mRoot;
	private ShaderProgram mShaderProgram;

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

		mColorActor = new Actor();
		mColorActor.setColor(Color.BLACK);
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}

	public void draw(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;

		draw();
	}

	@Override
	public void draw() {
		getCamera().update();

		// Render normal objects.
		if (mShaderProgram != null) {
			mShaderProgram.begin();

			mShaderProgram.setUniformMatrix("modelViewProjectionMatrix", getCamera().combined);
			mRoot.draw(mShaderProgram);

			mShaderProgram.end();
		}
	}

	@Override
	public void addActor(Actor actor) {
		mRoot.addActor(actor);
	}

	@Override
	public void act(float delta) {
		mRoot.act(delta);
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

	@Override
	public void addAction(Action action) {
		mColorActor.addAction(action);
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		Vector2 actorCoords = new Vector2(stageX, stageY);
		getRoot().parentToLocalCoordinates(actorCoords);
		return getRoot().hit(actorCoords.x, actorCoords.y, touchable);
	}

	public void clearActors() {
		mRoot.clear();
	}

	@Override
	public void dispose() {
		super.dispose();
		mShaderProgram.dispose();
	}
}
