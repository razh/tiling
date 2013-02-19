package com.razh.tiling.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.razh.tiling.MeshStage;
import com.razh.tiling.TilingGame;

public abstract class BasicScreen implements Screen {
	private TilingGame mGame;
	private Stage mStage;
	private InputMultiplexer mInputMultiplexer;

	public BasicScreen(TilingGame game) {
		setGame(game);
		setInputMultiplexer(new InputMultiplexer());
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

	public InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}

	public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
		mInputMultiplexer = inputMultiplexer;
	}

	public void addInputProcessor(InputProcessor inputProcessor) {
		mInputMultiplexer.addProcessor(inputProcessor);
	}

	public void removeInputProcessor(InputProcessor inputProcessor) {
		mInputMultiplexer.removeProcessor(inputProcessor);
	}

	@Override
	public void show() {
		getGame().getInputMultiplexer().addProcessor(getInputMultiplexer());
	}

	@Override
	public void hide() {
		getGame().getInputMultiplexer().removeProcessor(getInputMultiplexer());
	}

	@Override
	public void dispose() {
		getStage().dispose();
	}
}
