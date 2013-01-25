package com.razh.tiling;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MeshActor extends Actor3D {
	private Mesh mMesh;

	private ShaderProgram mShaderProgram;

	private float mRotationAngle;
	private float mOrientation;

	private Entity mEntity;

	public MeshActor() {
		super();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (mEntity != null) {
			mEntity.act(delta);
		}
	}

	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		setShaderProgram(shaderProgram);

		draw(parentAlpha);
	}

	public void draw(float parentAlpha) {
		mShaderProgram.setUniformf("rotation", getRotation());
		mShaderProgram.setUniformf("translate", getPosition());
		mShaderProgram.setUniformf("scale", getWidth(), getHeight(), getDepth());
		mShaderProgram.setUniformf("color", getColor());
		if (hasMesh()) {
			getMesh().render(getShaderProgram(), GL20.GL_TRIANGLES);
		}
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled) {
			return null;
		}

		if (Math.abs(x - getX()) <= getWidth() && Math.abs(y - getY()) <= getHeight()) {
			return this;
		}

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

	public Mesh getMesh() {
		return mMesh;
	}

	public void setMesh(Mesh mesh) {
		mMesh = mesh;
	}

	public boolean hasMesh() {
		return getMesh() != null;
	}

	public Entity getEntity() {
		return mEntity;
	}

	public void setEntity(Entity entity) {
		mEntity = entity;
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}
}
