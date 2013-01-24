package com.razh.tiling;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Uniforms {
	private Color mAmbientLightColor;

	private float[] mPointLightColors;
	private float[] mPointLightPositions;
	private float[] mPointLightDistances;

	public Uniforms() {
		setAmbientLightColor(new Color());
		setPointLightColors(new float[]{});
		setPointLightPositions(new float[]{});
		setPointLightDistances(new float[]{});
	}

	public Color getAmbientLightColor() {
		return mAmbientLightColor;
	}

	public void setAmbientLightColor(Color ambientLightColor) {
		mAmbientLightColor = ambientLightColor;
	}

	public float[] getPointLightColors() {
		return mPointLightColors;
	}

	public void setPointLightColors(ArrayList<Color> pointLightColors) {
		float[] colorsArray = new float[pointLightColors.size() * 3];
		int index = 0;

		Color color;
		for (int i = 0, n = pointLightColors.size(); i < n; i++ ) {
			color = pointLightColors.get(i);
			colorsArray[index++] = color.r;
			colorsArray[index++] = color.g;
			colorsArray[index++] = color.b;
		}

		setPointLightColors(colorsArray);
	}

	public void setPointLightColors(float[] pointLightColors) {
		mPointLightColors = pointLightColors;
	}

	public float[] getPointLightPositions() {
		return mPointLightPositions;
	}

	public void setPointLightPositions(ArrayList<Float> pointLightPositions) {
		setPointLightPositions(ListToPrimitiveFloatArray(pointLightPositions));
	}

	public void setPointLightPositions(float[] pointLightPositions) {
		mPointLightPositions = pointLightPositions;
	}

	public float[] getPointLightDistances() {
		return mPointLightDistances;
	}

	public void setPointLightDistances(ArrayList<Float> pointLightDistances) {
		setPointLightDistances(ListToPrimitiveFloatArray(pointLightDistances));
	}

	public void setPointLightDistances(float[] pointLightDistances) {
		mPointLightDistances = pointLightDistances;
	}

	private float[] ListToPrimitiveFloatArray(List<Float> list) {
		float[] floatArray = new float[list.size()];

		Float f;
		for (int i = 0, n = list.size(); i < n; i++) {
			f = list.get(i);
			floatArray[i] = (f != null ? f : Float.NaN);
		}

		return floatArray;
	}

	public void setUniforms(ShaderProgram shaderProgram) {
		shaderProgram.begin();

		Color ambient = getAmbientLightColor();
		shaderProgram.setUniformf("ambientLightColor", ambient.r, ambient.g, ambient.b);

		float[] array = getPointLightColors();
		shaderProgram.setUniform3fv("pointLightColor", array, 0, array.length);

		array = getPointLightPositions();
		shaderProgram.setUniform3fv("pointLightPosition", array, 0, array.length);

		array = getPointLightDistances();
		shaderProgram.setUniform1fv("pointLightDistance", array, 0, array.length);

		shaderProgram.end();
	}
}