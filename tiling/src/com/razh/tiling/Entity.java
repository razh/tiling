package com.razh.tiling;

public abstract class Entity {
	private MeshActor mActor;

	public abstract void act(float delta);
	
	public Entity() {}
	
	public Entity(MeshActor actor) {
		setActor(actor);
	}
	
	public MeshActor getActor() {
		return mActor;
	}
	
	public void setActor(MeshActor actor) {
		mActor = actor;
	}
}
