package com.razh.tiling;

import com.badlogic.gdx.graphics.Color;

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

	public void setPointLightPositions(float[] pointLightPositions) {
		this.pointLightPositions = pointLightPositions;
	}
}