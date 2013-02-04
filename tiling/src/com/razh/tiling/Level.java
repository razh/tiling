package com.razh.tiling;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class Level {
	private String mName;
	private Color mBackgroundColor;
	private ArrayList<MeshActor> mActors;

	public Level() {
		setName("");
		setBackgroundColor(new Color());
		setActors(new ArrayList<MeshActor>());
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
}
