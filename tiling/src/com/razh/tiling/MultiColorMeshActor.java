package com.razh.tiling;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

public class MultiColorMeshActor extends MeshActor {
	private Color mColorA;
	private Color mColorB;
	private Color mColorC;

	public MultiColorMeshActor() {
		super();

		mColorA = new Color();
		mColorB = new Color();
		mColorC = new Color();
	}

	public Color getColorA() {
		return mColorA;
	}

	public void setColorA(Color color) {
		mColorA = color;
	}

	public Color getColorB() {
		return mColorB;
	}

	public void setColorB(Color color) {
		mColorB = color;
	}

	public Color getColorC() {
		return mColorC;
	}

	public void setColorC(Color color) {
		mColorC = color;
	}

	@Override
	public void draw(float parentAlpha) {
		getModelMatrix().idt()
	        .translate(getPosition())
	        .rotate(getRotationAxis(), getRotation())
	        .rotate(Vector3.Z, getOrientation())
	        .scale(getWidth(), getHeight(), getDepth());
		getShaderProgram().setUniformMatrix("modelMatrix", getModelMatrix());

		getNormalMatrix().set(getModelMatrix().cpy().inv()).transpose();
		getShaderProgram().setUniformMatrix("normalMatrix", getNormalMatrix());

		getShaderProgram().setUniformf("diffuse", getColor().r, getColor().g, getColor().b);
		getShaderProgram().setUniformf("diffuseA", getColorA().r, getColorA().g, getColorA().b);
		getShaderProgram().setUniformf("diffuseB", getColorB().r, getColorB().g, getColorB().b);
		getShaderProgram().setUniformf("diffuseC", getColorC().r, getColorC().g, getColorC().b);

		if (hasMaterial()) {
			getMaterial().bind(getShaderProgram());
		}

		if (hasMesh()) {
			getMesh().render(getShaderProgram(), GL20.GL_TRIANGLES);
		}
	}
}
