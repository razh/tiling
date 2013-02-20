package com.razh.tiling;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "tiling";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.samples = 4;
		cfg.useCPUSynch = false;

		new LwjglApplication(new TilingGame(), cfg);
	}
}
