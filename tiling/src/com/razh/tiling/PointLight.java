package com.razh.tiling;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PointLight extends Light {
	private float mDistance;
	private static Mesh sMesh;

	public PointLight() {
		super();

		setDistance(0.0f);
		if (sMesh == null) {
			sMesh = Geometry.createBillboard();
		}
	}

	public float getDistance() {
		return mDistance;
	}

	public void setDistance(float distance) {
		mDistance = distance;
	}

	@Override
	public void draw(ShaderProgram shaderProgram) {
		if (TilingGame.DEBUG) {
			shaderProgram.setUniformf("color", getColor());
			shaderProgram.setUniformf("translate", getPosition());
			shaderProgram.setUniformf("scale", getWidth(), getHeight(), getDepth());
			sMesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
		}
	}
}
