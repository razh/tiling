package com.razh.tiling.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.razh.tiling.TilingGame;
import com.razh.tiling.TilingMeshStage;
import com.razh.tiling.files.LevelLoader;
import com.razh.tiling.MeshActor;
import com.razh.tiling.logic.TilingEntity;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen extends BasicScreen {
	private TilingMeshStage mBackgroundStage;

	private TextButton mStartButton;
	private TextButton mLevelButton;
	private TextButton mHelpButton;

	public MainMenuScreen(TilingGame game) {
		super(game);

		Stage stage = new Stage();
		stage.getSpriteBatch().setColor(Color.CLEAR);
		setStage(stage);
		addInputProcessor(stage);

		mBackgroundStage = new TilingMeshStage();
		LevelLoader levelLoader = new LevelLoader();
		levelLoader.getLevelByName("invertedRedTriangle").load(mBackgroundStage);

		Skin skin = new Skin();
		skin.add("image", new NinePatch(new Texture(Gdx.files.internal("data/gray-50-alpha-50-square.png")), 1, 1, 1, 1));
		skin.load(Gdx.files.internal("ui/buttons.json"));

		mStartButton = new TextButton("Start", skin);
		mStartButton.setPosition(0, Gdx.graphics.getHeight() * 0.5f);
		mStartButton.setWidth(Gdx.graphics.getWidth() * 0.5f);
		mStartButton.pad(20.0f);
		mStartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreenByName("GAME");
			}
		});

		mLevelButton = new TextButton("Level", skin);
		mLevelButton.setPosition(0, mStartButton.getY() - mStartButton.getHeight() - mStartButton.getPadY());
		mLevelButton.setWidth(Gdx.graphics.getWidth() * 0.5f);
		mLevelButton.pad(20.0f);
		mLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreenByName("LEVEL_SELECT");
			}
		});

		mHelpButton = new TextButton("Help", skin);
		mHelpButton.setPosition(0, mLevelButton.getY() - mLevelButton.getHeight() - mLevelButton.getPadY());
		mHelpButton.setWidth(Gdx.graphics.getWidth() * 0.5f);
		mHelpButton.pad(20.0f);

		mBackgroundStage.addAction(
			forever(
				sequence(
					delay(0.2f),
					new Action() {
						@Override
						public boolean act(float delta) {
							Random random = new Random();
							int index = random.nextInt(mBackgroundStage.getColorRoot().getChildren().size);
							((TilingEntity) ((MeshActor) mBackgroundStage.getColorRoot().getActorAt(index)).getEntity()).touch();

							return true;
						}
					}
				)
			)
		);

		stage.addActor(mStartButton);
		stage.addActor(mLevelButton);
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
	public void show() {
		super.show();

//		showButton(mStartButton, new ClickListener() {
//			@Override
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				getGame().setScreenByName("GAME");
//			}
//		});
//		showButton(mLevelButton, new ClickListener() {
//			@Override
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				getGame().setScreenByName("LEVEL_SELECT");
//			}
//		});

		System.out.println("HELLLO:" + mStartButton.getListeners().size);
		System.out.println("HELLLO2:" + mLevelButton.getListeners().size);
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
