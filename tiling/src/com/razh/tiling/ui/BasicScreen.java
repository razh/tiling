package com.razh.tiling.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.razh.tiling.MeshStage;
import com.razh.tiling.TilingGame;

public abstract class BasicScreen implements Screen {
	private TilingGame mGame;
	private Stage mStage;
	private InputProcessor mInputProcessor;

	public BasicScreen(TilingGame game) {
		setGame(game);
	}

	public TilingGame getGame() {
		return mGame;
	}

	public void setGame(TilingGame game) {
		mGame = game;
	}

	public Stage getStage() {
		return mStage;
	}

	public MeshStage getMeshStage() {
		if (getStage() instanceof MeshStage) {
			return (MeshStage) getStage();
		}

		return null;
	}

	public void setStage(Stage stage) {
		mStage = stage;
	}

	public InputProcessor getInputProcessor() {
		return mInputProcessor;
	}

	public void setInputProcessor(InputProcessor inputProcessor) {
		mInputProcessor = inputProcessor;
	}

	@Override
	public void show() {
		InputMultiplexer inputMultiplexer = getGame().getInputMultiplexer();
		inputMultiplexer.addProcessor(getInputProcessor());
	}

	@Override
	public void hide() {
		InputMultiplexer inputMultiplexer = getGame().getInputMultiplexer();
		inputMultiplexer.removeProcessor(getInputProcessor());
	}

	@Override
	public void dispose() {
		mStage.dispose();
	}
}
