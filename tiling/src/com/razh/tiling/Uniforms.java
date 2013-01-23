package com.razh.tiling;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Uniforms {
	public Color ambientLightColor;

	public Color[] pointLightColors;
	public float[] pointLightPositions;
	public float[] pointLightDistances;

	public Color getAmbientLightColor() {
		return ambientLightColor;
	}

	public void setAmbientLightColor(Color ambientLightColor) {
		this.ambientLightColor = ambientLightColor;
	}

	public Color[] getPointLightColors() {
		return pointLightColors;
	}

	public void setPointLightColors(Color[] pointLightColors) {
		this.pointLightColors = pointLightColors;
	}

	public float[] getPointLightPositions() {
		return pointLightPositions;
	}

	public void setPointLightPositions(Float[] pointLightPositions) {
		setPointLightPositions(toPrimitiveFloatArray(pointLightPositions));
	}

	public void setPointLightPositions(float[] pointLightPositions) {
		this.pointLightPositions = pointLightPositions;
	}

	public float[] getPointLightDistances() {
		return pointLightDistances;
	}

	public void setPointLightDistances(Float[] pointLightDistances) {
		setPointLightDistances(toPrimitiveFloatArray(pointLightDistances));
	}

	public void setPointLightDistances(float[] pointLightDistances) {
		this.pointLightDistances = pointLightDistances;
	}

	private float[] toPrimitiveFloatArray(Float[] array) {
		float[] floatArray = new float[array.length];

		Float f;
		for (int i = 0; i < array.length; i++) {
			f = array[i];
			floatArray[i] = (f != null ? f : Float.NaN);
		}

		return floatArray;
	}

	public void setUniforms(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("ambientLightColor", ambientLightColor);
		shaderProgram.setUniform3fv("pointLightColor", pointLightColors, offset, length)

	}
}