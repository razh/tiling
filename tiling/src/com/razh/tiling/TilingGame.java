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
import com.razh.tiling.ui.GameScreen;
import com.razh.tiling.ui.LevelSelectScreen;
import com.razh.tiling.ui.MainMenuScreen;
import com.razh.tiling.ui.SplashScreen;

public class TilingGame extends Game {
	private InputMultiplexer mInputMultiplexer;
	private Player mPlayer;
	private LevelLoader mLevelLoader;
	private FPSLogger mFPSLogger;
	private boolean mGL20;

	private SpriteBatch mSpriteBatch;
	private BitmapFont mFont;

	public enum LightingModel {
		LAMBERT,
		PHONG
	};
	public static LightingModel lightingModel = LightingModel.LAMBERT;
	public static boolean DEBUG = false;

	public SplashScreen splashScreen;
	public MainMenuScreen mainMenuScreen;
	public LevelSelectScreen levelSelectScreen;
	public GameScreen gameScreen;

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

		mPlayer = new Player();

		splashScreen = new SplashScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		levelSelectScreen = new LevelSelectScreen(this);
		gameScreen = new GameScreen(this);

		mLevelLoader = new LevelLoader();
		mLevelLoader.getLevelByIndex(2).load((TilingMeshStage) gameScreen.getMeshStage());

		// Input.
		mInputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(mInputMultiplexer);

		setScreen(mainMenuScreen);
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

	public Player getPlayer() {
		return mPlayer;
	}

	public InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}
}
