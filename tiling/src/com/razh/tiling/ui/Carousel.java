package com.razh.tiling.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Carousel extends ScrollPane {

	private int mSize;
	private int mIndex;
	private float mSpeed;

	public Carousel(Actor widget) {
		super(widget);
	}

	public Carousel(Actor widget, Skin skin) {
		super(widget, skin);
	}

	public Carousel(Actor widget, Skin skin, String styleName) {
		super(widget, skin, styleName);
	}

	public Carousel(Actor widget, ScrollPaneStyle style) {
		super(widget, style);
	}

	@Override
	public void layout() {
		super.layout();

		if (getWidget() instanceof Table) {

		}
	}

	/**
	 * Slide to previous.
	 */
	public void prev() {

	}

	/*
	 * Slide to next.
	 */
	public void next() {

	}

	public int size() {
		return mSize;
	}

	public int getIndex() {
		return mIndex;
	}

	public float getSpeed() {
		return mSpeed;
	}

}
