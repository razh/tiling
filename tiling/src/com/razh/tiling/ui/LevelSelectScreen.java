package com.razh.tiling.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.razh.tiling.MeshStage;
import com.razh.tiling.TilingGame;

public class LevelSelectScreen extends BasicScreen {
	private int mNumClicks;

	public LevelSelectScreen() {
		setStage(new Stage());

		Stage stage = getStage();
		setInputProcessor(stage);

		Skin skin = new Skin();
		skin.add("image", new Texture(Gdx.files.internal("data/white-square.png")));
		skin.load(Gdx.files.internal("ui/buttons.json"));

		mNumClicks = 0;

		TextButton button = new TextButton("Hello World!", skin, "default");
		button.setPosition(0, Gdx.graphics.getHeight() * 0.5f);
		button.setWidth(Gdx.graphics.getWidth());
		button.setHeight(100);
		button.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				mNumClicks++;
				if (mNumClicks > 4) {
					((TilingGame) getGame()).setState(TilingGame.State.SPLASH);
				}
				return true;
			}
		});

		stage.addActor(button);
	}

	@Override
	public void render(float delta) {
		getStage().act(delta);
//		Color backgroundColor = getMeshStage().getColor();

//		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
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
	public void show() {
	}

	@Override
	public void hide() {
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
