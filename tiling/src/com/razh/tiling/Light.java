package com.razh.tiling;

import com.badlogic.gdx.graphics.Color;

public class Light extends Actor3D {
	private Color mAmbientColor;
	private Color mDiffuseColor;
	private Color mSpecularColor;
	
	public Light() {
		setAmbientColor(new Color());
		setDiffuseColor(new Color());
		setSpecularColor(new Color());		
	}
	
	public Color getAmbientColor() {
		return mAmbientColor;
	}
	public void setAmbientColor(Color ambientColor) {
		mAmbientColor = ambientColor;
	}
	public Color getDiffuseColor() {
		return mDiffuseColor;
	}
	public void setDiffuseColor(Color diffuseColor) {
		mDiffuseColor = diffuseColor;
	}
	public Color getSpecularColor() {
		return mSpecularColor;
	}
	public void setSpecularColor(Color specularColor) {
		mSpecularColor = specularColor;
	}
}
