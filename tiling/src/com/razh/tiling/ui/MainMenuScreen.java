package com.razh.tiling.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.razh.tiling.TilingGame;
import com.razh.tiling.TilingMeshStage;
import com.razh.tiling.files.LevelLoader;

public class MainMenuScreen extends BasicScreen {
	private TilingMeshStage mBackgroundStage;

	private TextButton mButton;
	private TextButton mHelpButton;

	public MainMenuScreen(TilingGame game) {
		super(game);

		Stage stage = new Stage();
		stage.getSpriteBatch().setColor(Color.CLEAR);
		setStage(stage);
		setInputProcessor(stage);

		mBackgroundStage = new TilingMeshStage();
		LevelLoader levelLoader = new LevelLoader();
		levelLoader.getLevelByName("testGraphLevel").load(mBackgroundStage);

		Skin skin = new Skin();
		skin.add("image", new NinePatch(new Texture(Gdx.files.internal("data/white-square.png")), 1, 1, 1, 1));
		skin.load(Gdx.files.internal("ui/buttons.json"));

		mButton = new TextButton("Start", skin);
		mButton.setPosition(0, Gdx.graphics.getHeight() * 0.5f);
		mButton.setWidth(Gdx.graphics.getWidth() * 0.5f);
		mButton.pad(20.0f);
		mButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int mButton) {
				getGame().setScreenByName("GAME");
				return true;
			}
		});

		mHelpButton = new TextButton("Help", skin);
		mHelpButton.setPosition(0, mButton.getY() - mButton.getHeight() - mButton.getPadY());
		mHelpButton.setWidth(Gdx.graphics.getWidth() * 0.5f);
		mHelpButton.pad(20.0f);

		stage.addActor(mButton);
		stage.addActor(mHelpButton);
	}

	@Override
	public void render(float delta) {
		mBackgroundStage.act(delta);
		getStage().act(delta);

		Color backgroundColor = mBackgroundStage.getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		mBackgroundStage.draw();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);

		getStage().draw();

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
