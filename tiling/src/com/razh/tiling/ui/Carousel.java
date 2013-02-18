package com.razh.tiling.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Carousel extends ScrollPane {

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

}
