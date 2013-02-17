package com.razh.tiling.ui;

import com.badlogic.gdx.Gdx;
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

	public MainMenuScreen(TilingGame game) {
		super(game);

		Stage stage = new Stage();
		setStage(stage);
		setInputProcessor(stage);

		mBackgroundStage = new TilingMeshStage();
		LevelLoader levelLoader = new LevelLoader();

		Skin skin = new Skin();
		skin.add("image", new NinePatch(new Texture(Gdx.files.internal("data/white-square.png")), 1, 1, 1, 1));
		skin.load(Gdx.files.internal("ui/buttons.json"));

		TextButton button = new TextButton("Start", skin);
		button.setPosition(0, Gdx.graphics.getHeight() * 0.5f);
		button.setWidth(Gdx.graphics.getWidth());
		button.setHeight(100);
		button.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				TilingGame game = getGame();
				game.setScreen(game.getScreens().get("GAME"));
				return true;
			}
		});

		TextButton helpButton = new TextButton("Help", skin);
		helpButton.setPosition(0, button.getTop() - button.getHeight() - 110);
		helpButton.setWidth(Gdx.graphics.getWidth());
		helpButton.setHeight(100);

		stage.addActor(button);
		stage.addActor(helpButton);
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
