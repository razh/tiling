package com.razh.tiling;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.razh.tiling.files.LevelLoader;
import com.razh.tiling.screens.BasicScreen;
import com.razh.tiling.screens.GameScreen;
import com.razh.tiling.screens.LevelSelectScreen;
import com.razh.tiling.screens.MainMenuScreen;
import com.razh.tiling.screens.SplashScreen;

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

	private Map<String, BasicScreen> mScreens;

	// Virtual screen size.
	public static float WIDTH = 960.0f;
	public static float HEIGHT = 720.0f;

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

		mScreens = new HashMap<String, BasicScreen>();
		mScreens.put("SPLASH", new SplashScreen(this));
		mScreens.put("MAIN_MENU", new MainMenuScreen(this));
		mScreens.put("LEVEL_SELECT", new LevelSelectScreen(this));
		mScreens.put("GAME", new GameScreen(this));

		mLevelLoader = new LevelLoader();
		mLevelLoader.getLevelByName("testGraphLevel").load((TilingMeshStage) getScreens().get("GAME").getMeshStage());
//		mLevelLoader.getLevelByIndex(2).load((TilingMeshStage) getScreens().get("GAME").getMeshStage());

		// Input.
		mInputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(mInputMultiplexer);
		Gdx.input.setCatchBackKey(true);

		setScreen(getScreens().get("SPLASH"));
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

	public Map<String, BasicScreen> getScreens() {
		return mScreens;
	}

	public void setScreenByName(String name) {
		Screen screen = getScreens().get(name);
		if (screen != null) {
			setScreen(screen);
		}
	}

	public Player getPlayer() {
		return mPlayer;
	}

	public InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}
}
