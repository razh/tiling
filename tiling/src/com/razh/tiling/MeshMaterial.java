package com.razh.tiling;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class MeshMaterial extends Material {
	private Color mAmbient;
	private Color mSpecular;
	private Color mEmissive;
	private float mShininess;

	private boolean mIsShiny;

	public MeshMaterial() {
		this(new Color(Color.WHITE),
		     new Color(Color.DARK_GRAY),
		     new Color(Color.BLACK),
		     30);
	}

	public MeshMaterial(Color ambient, Color specular, Color emissive, float shininess) {
		super();

		setAmbient(ambient);
		setSpecular(specular);
		setEmissive(emissive);
		setShininess(shininess);

		setShiny(false);
	}

	@Override
	public void bind(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("ambient", mAmbient.r, mAmbient.g, mAmbient.b);
		shaderProgram.setUniformf("emissive", mEmissive.r, mEmissive.g, mEmissive.b);
		if (isShiny()) {
			shaderProgram.setUniformf("specular", mSpecular.r, mSpecular.g, mSpecular.b);
			shaderProgram.setUniformf("shininess", mShininess);
		}
	}

	public Color getAmbient() {
		return mAmbient;
	}

	public void setAmbient(Color ambient) {
		mAmbient = ambient;
	}

	public Color getSpecular() {
		return mSpecular;
	}

	public void setSpecular(Color specular) {
		mSpecular = specular;
	}

	public Color getEmissive() {
		return mEmissive;
	}

	public void setEmissive(Color emissive) {
		mEmissive = emissive;
	}

	public float getShininess() {
		return mShininess;
	}

	public void setShininess(float shininess) {
		mShininess = shininess;
	}

	public boolean isShiny() {
		return mIsShiny;
	}

	public void setShiny(boolean shiny) {
		mIsShiny = shiny;
	}
}
