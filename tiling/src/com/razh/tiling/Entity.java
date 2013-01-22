package com.razh.tiling;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Entity {
	private Actor mActor;

	public abstract void act(float delta);
	
	public Actor getActor() {
		return mActor;
	}
	
	public void setActor(Actor actor) {
		mActor = actor;
	}
}
