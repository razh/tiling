package com.razh.tiling;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MeshActor extends Actor {
	private ShaderProgram mShaderProgram;

	private float mRotationAngle;
	private float mOrientation;

	public MeshActor() {
		super();
	}

	public void act(float delta) {
		super.act(delta);
	}

	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		this.mShaderProgram = shaderProgram;

		draw(parentAlpha);
	}

	public void draw(float parentAlpha) {
		mShaderProgram.setUniformf("rotation", getRotation());
		mShaderProgram.setUniformf("translate", getX(), getY());
		mShaderProgram.setUniformf("scale", getWidth(), getHeight());
		mShaderProgram.setUniformf("v_color", getColor());
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;

		if (x == getX() && y == getY())
			return this;

		return null;
	}

	@Override
	public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
		return parentCoords;
	}

	public float getRotationAngle() {
		return mRotationAngle;
	}

	public void setRotationAngle(float rotationAngle) {
		mRotationAngle = rotationAngle;
	}

	public float getOrientation() {
		return mOrientation;
	}

	public void setOrientation(float orientation) {
		mOrientation = orientation;
	}
}
