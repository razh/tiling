package com.razh.tiling;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PointLight extends Light {
	private float mDistance;
	private static Mesh sMesh;

	public PointLight() {
		super();

		setDistance(0.0f);
		if (sMesh == null) {
			sMesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
			                true, 4, 4,
			                new VertexAttribute(Usage.Position, 3,
	                                            ShaderProgram.POSITION_ATTRIBUTE));

			sMesh.setVertices(new float[]{-1.0f, -1.0f, 0.0f,
			                               1.0f, -1.0f, 0.0f,
			                              -1.0f,  1.0f, 0.0f,
			                               1.0f,  1.0f, 0.0f});
			sMesh.setIndices(new short[]{0, 1, 2, 3});
		}
	}

	public float getDistance() {
		return mDistance;
	}

	public void setDistance(float distance) {
		mDistance = distance;
	}

	@Override
	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		shaderProgram.setUniformf("color", getColor());
		shaderProgram.setUniformf("translate", getPosition());
		shaderProgram.setUniformf("scale", getWidth(), getHeight(), getDepth());
		sMesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
	}
}
