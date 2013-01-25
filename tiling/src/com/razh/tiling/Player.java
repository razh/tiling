package com.razh.tiling;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player {
	private int mScore;
	private Actor mSelected;

	public int getScore() {
		return mScore;
	}
	public void setScore(int score) {
		mScore = score;
	}
	public Actor getSelected() {
		return mSelected;
	}
	public void setSelected(Actor selected) {
		mSelected = selected;
	}
}
