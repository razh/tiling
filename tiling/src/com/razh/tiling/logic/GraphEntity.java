package com.razh.tiling.logic;

import java.util.ArrayList;

import com.razh.tiling.Entity;

public class GraphEntity extends Entity {
	private ArrayList<GraphEntity> mNeighbors;

	public GraphEntity() {
		setNeighbors(new ArrayList<GraphEntity>());
	}

	public ArrayList<GraphEntity> getNeighbors() {
		return mNeighbors;
	}

	public void setNeighbors(ArrayList<GraphEntity> neighbors) {
		mNeighbors = neighbors;
	}

	public void addNeighbor(GraphEntity neighbor) {
		mNeighbors.add(neighbor);
	}

	@Override
	public void act(float delta) {}
}
