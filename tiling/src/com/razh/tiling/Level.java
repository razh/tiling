package com.razh.tiling;

import java.util.ArrayList;

public class Level {
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
}
