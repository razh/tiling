package com.razh.tiling;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Actor3D extends Actor {
	private float mZ;
	private float mDepth;

	public float getZ() {
		return mZ;
	}

	public void setZ(float z) {
		mZ = z;
	}

	public Vector3 getPosition() {
		return new Vector3(getX(), getY(), getZ());
	}

	public void setPosition(Vector3 position) {
		super.setPosition(position.x, position.y);
		setZ(position.z);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
	}

	public void setPosition(float x, float y, float z) {
		super.setPosition(x, y);
		setZ(z);
	}

	public float getDepth() {
		return mDepth;
	}

	public void setDepth(float depth) {
		mDepth = depth;
	}
}
