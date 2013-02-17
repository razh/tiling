package com.razh.tiling.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.tiling.AmbientLight;
import com.razh.tiling.Light;
import com.razh.tiling.PointLight;
import com.razh.tiling.Shader;
import com.razh.tiling.TilingGame;
import com.razh.tiling.TilingMeshStage;
import com.razh.tiling.Uniforms;
import com.razh.tiling.TilingGame.LightingModel;
import com.razh.tiling.input.DebugInputProcessor;
import com.razh.tiling.input.GameInputProcessor;

public class GameScreen extends BasicScreen {

	public GameScreen(TilingGame game) {
		super(game);

		setStage(new TilingMeshStage());

		InputMultiplexer inputMultiplexer = new InputMultiplexer();

		DebugInputProcessor debugInputProcessor = new DebugInputProcessor();
		debugInputProcessor.setStage(getMeshStage());
		debugInputProcessor.setGame(getGame());
		debugInputProcessor.setPlayer(getGame().getPlayer());

		GameInputProcessor gameInputProcessor = new GameInputProcessor();
		gameInputProcessor.setStage(getMeshStage());
		gameInputProcessor.setGame(getGame());
		gameInputProcessor.setPlayer(getGame().getPlayer());

		inputMultiplexer.addProcessor(debugInputProcessor);
		inputMultiplexer.addProcessor(gameInputProcessor);

		setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		update(delta);

		Color backgroundColor = getMeshStage().getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		getStage().draw();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}

	public void update(float delta) {
		getStage().act(delta);
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		super.dispose();
	}
}
