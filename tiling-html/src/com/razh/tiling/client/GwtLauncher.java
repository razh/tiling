package com.razh.tiling.client;

import com.razh.tiling.TilingGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		cfg.antialiasing = true;
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new TilingGame();
	}
}