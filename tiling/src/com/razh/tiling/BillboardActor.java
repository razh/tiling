package com.razh.tiling;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class BillboardActor extends Actor3D {
	private static Mesh sMesh;

	public BillboardActor() {
		if (sMesh == null) {
			sMesh = Geometry.createBillboard();
		}
	}

	public void draw(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("color", getColor());
		shaderProgram.setUniformf("translate", getPosition());
		shaderProgram.setUniformf("scale", getWidth(), getHeight(), getDepth());
		sMesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
	}
}
