package com.razh.tiling;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class Level {
	private String mName;
	private ArrayList<MeshActor> mActors;
	private ArrayList<Light> mLights;

	private Color mBackgroundColor;
	private Color mAmbientColor;

	public Level() {
		setName("");
		setActors(new ArrayList<MeshActor>());
		setLights(new ArrayList<Light>());
		setBackgroundColor(new Color());
		setAmbientColor(new Color());
	}

	public ArrayList<MeshActor> getActors() {
		return mActors;
	}

	public void setActors(ArrayList<MeshActor> actors) {
		mActors = actors;
	}

	public void addActor(MeshActor actor) {
		mActors.add(actor);
	}

	public ArrayList<Light> getLights() {
		return mLights;
	}

	public void setLights(ArrayList<Light> lights) {
		mLights = lights;
	}

	public void addLight(Light light) {
		mLights.add(light);
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public Color getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		mBackgroundColor = backgroundColor;
	}

	public Color getAmbientColor() {
		return mAmbientColor;
	}

	public void setAmbientColor(Color ambientColor) {
		mAmbientColor = ambientColor;
	}

	public void loadOnto(MeshStage stage) {
		stage.setColor(getBackgroundColor());

		for (int i = 0, n = mActors.size(); i < n; i++) {
			stage.addColorActor(mActors.get(i));
		}

		AmbientLight light = new AmbientLight();
		light.setColor(getAmbientColor());
		stage.addLight(light);

		for (int i = 0, n = mLights.size(); i < n; i++) {
			stage.addLight(mLights.get(i));
		}
	}
}
