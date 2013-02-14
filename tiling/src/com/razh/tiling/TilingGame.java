package com.razh.tiling;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.razh.tiling.files.LevelLoader;
import com.razh.tiling.input.BasicInputProcessor;
import com.razh.tiling.input.DebugInputProcessor;
import com.razh.tiling.input.GameInputProcessor;
import com.razh.tiling.ui.GameScreen;

public class TilingGame extends Game {
	private InputMultiplexer mInputMultiplexer;
	private Player mPlayer;
	private LevelLoader mLevelLoader;
	private FPSLogger mFPSLogger;
	private boolean mGL20;

	private BitmapFont mFont;
	private SpriteBatch mSpriteBatch;

	public enum LightingModel {
		LAMBERT,
		PHONG
	};
	public static LightingModel lightingModel = LightingModel.LAMBERT;
	public static boolean DEBUG = false;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);
		mFPSLogger = new FPSLogger();

		mGL20 = Gdx.graphics.isGL20Available();
		if (!mGL20) {
			Gdx.app.exit();
		}

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);

		mSpriteBatch = new SpriteBatch();
		mFont = new BitmapFont();
		mFont.setColor(Color.WHITE);

		GameScreen gameScreen = new GameScreen();
		setScreen(gameScreen);

		TilingMeshStage stage = (TilingMeshStage) gameScreen.getStage();

		mPlayer = new Player();
		mLevelLoader = new LevelLoader();
		mLevelLoader.getLevelByIndex(2).load(stage);

		// Input.
		mInputMultiplexer = new InputMultiplexer();

		BasicInputProcessor debugInputProcessor = new DebugInputProcessor();
		debugInputProcessor.setPlayer(mPlayer);
		debugInputProcessor.setStage(stage);
		mInputMultiplexer.addProcessor(debugInputProcessor);

		BasicInputProcessor gameInputProcessor = new GameInputProcessor();
		gameInputProcessor.setPlayer(mPlayer);
		gameInputProcessor.setStage(stage);
		mInputMultiplexer.addProcessor(gameInputProcessor);

		Gdx.input.setInputProcessor(mInputMultiplexer);
	}

	@Override
	public void dispose() {
		super.dispose();
		mSpriteBatch.dispose();
		mFont.dispose();
	}

	@Override
	public void render() {
		super.render();

		mSpriteBatch.begin();
		mFont.draw(mSpriteBatch, Integer.toString(Gdx.graphics.getFramesPerSecond()), Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.9f);
		mSpriteBatch.end();

		mFPSLogger.log();
	}
}
