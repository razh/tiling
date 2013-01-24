package com.razh.tiling;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PointLight extends Light {
	private float mDistance;
	private Mesh mesh;

	public PointLight() {
		super();

		setDistance(0.0f);
		mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                true, 1, 1,
		                new VertexAttribute(Usage.Position, 3,
                                            ShaderProgram.POSITION_ATTRIBUTE));

		mesh.setVertices(new float[]{0.0f, 0.0f, 0.0f});
		mesh.setIndices(new short[]{0});
	}

	public float getDistance() {
		return mDistance;
	}

	public void setDistance(float distance) {
		mDistance = distance;
	}

	@Override
	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		shaderProgram.setUniformf("translate", getPosition());
		mesh.render(shaderProgram, GL20.GL_POINTS);
	}

}
