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
import com.razh.tiling.ui.BasicScreen;
import com.razh.tiling.ui.GameScreen;
import com.razh.tiling.ui.LevelSelectScreen;
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

	public enum State {
		SPLASH,
		MAIN_MENU,
		LEVEL_SELECT,
		GAME
	};
	private State mState;
	private BasicScreen[] mScreens;

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

		mScreens = new BasicScreen[4];

		SplashScreen splashScreen = new SplashScreen(this);
		mScreens[State.SPLASH.ordinal()] = splashScreen;

		LevelSelectScreen levelSelectScreen = new LevelSelectScreen(this);
		mScreens[State.LEVEL_SELECT.ordinal()] = levelSelectScreen;

		GameScreen gameScreen = new GameScreen(this);
		mScreens[State.GAME.ordinal()] = gameScreen;

		TilingMeshStage stage = (TilingMeshStage) mScreens[State.GAME.ordinal()].getStage();

		mPlayer = new Player();
		mLevelLoader = new LevelLoader();
		mLevelLoader.getLevelByIndex(2).load(stage);

		// Input.
		mInputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(mInputMultiplexer);

		setState(State.LEVEL_SELECT);
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

	public State getState() {
		return mState;
	}

	public void setState(State state) {
		if (mState != state) {
			if (getScreen() != null) {
				mInputMultiplexer.removeProcessor(((BasicScreen) getScreen()).getInputProcessor());
			}

			mState = state;
			BasicScreen screen = mScreens[state.ordinal()];
			mInputMultiplexer.addProcessor(screen.getInputProcessor());
			setScreen(screen);
		}
	}

	public InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}
}
