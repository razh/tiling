package com.razh.tiling;

import java.util.ArrayList;

public class Level {
	private String mName;
	private ArrayList<MeshActor> mActors;

	public Level() {
		mActors = new ArrayList<MeshActor>();
	}

	public ArrayList<MeshActor> getActors() {
		return mActors;
	}

	public void setActors(ArrayList<MeshActor> actors) {
		mActors = actors;
	}

	public void addActor(MeshActor actor) {
		this.mActors.add(actor);
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}
}
