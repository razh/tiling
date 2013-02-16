package com.razh.tiling.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.razh.tiling.TilingGame;

public class LevelSelectScreen extends BasicScreen {

	public LevelSelectScreen(TilingGame game) {
		super(game);

		Stage stage = new Stage();
		setStage(stage);
		setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		getStage().act(delta);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		getStage().draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
