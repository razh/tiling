package com.razh.tiling;

public class PointLight extends Light {
	private float mIntensity = 1.0f;
	private float mDistance = 0.0f;

	public float getIntensity() {
		return mIntensity;
	}

	public void setIntensity(float intensity) {
		mIntensity = intensity;
	}

	public float getDistance() {
		return mDistance;
	}

	public void setDistance(float distance) {
		mDistance = distance;
	}

}
