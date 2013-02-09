package com.razh.tiling;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.tiling.files.LevelLoader;
import com.razh.tiling.tests.OriginalStageTest;

public class Game implements ApplicationListener {
	private InputProcessor mInputProcessor;
	private Player mPlayer;
	private LevelLoader mLevelLoader;
	private MeshStage mStage;
	private FPSLogger mFPSLogger;
	private boolean mGL20;

	private ShaderProgram mShaderProgram;
	private ShaderProgram mColorShaderProgram;
	private Uniforms mUniforms;

	private boolean mShaderProgramNeedsUpdate;
	private boolean mLightUniformsNeedRefresh;

	private BitmapFont mFont;
	private SpriteBatch mSpriteBatch;

	public enum LightingModel {
		LAMBERT,
		PHONG
	};
	public static LightingModel lightingModel = LightingModel.LAMBERT;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		mStage = new MeshStage();
		mFPSLogger = new FPSLogger();

		mGL20 = Gdx.graphics.isGL20Available();
		if (!mGL20) {
			Gdx.app.exit();
		}

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);

		mShaderProgramNeedsUpdate = false;
		mLightUniformsNeedRefresh = false;
		if (Game.lightingModel == LightingModel.PHONG) {
			mShaderProgram = Shader.createPhongShaderProgram();
		} else if (Game.lightingModel == LightingModel.LAMBERT) {
			mShaderProgram = Shader.createLambertShaderProgram();
		}
		mUniforms = new Uniforms();

		mStage.setShaderProgram(mShaderProgram);
		mStage.setPointLightShaderProgram(Shader.createPointLightShaderProgram());
		mStage.getCamera().position.z = 10000.0f;
		mStage.getCamera().far = 15000.0f;

		mSpriteBatch = new SpriteBatch();
		mFont = new BitmapFont();
		mFont.setColor(Color.WHITE);

		if (Game.lightingModel == LightingModel.PHONG) {
			mColorShaderProgram = Shader.createColorPhongShaderProgram();
		} else if (Game.lightingModel == LightingModel.LAMBERT) {
			mColorShaderProgram = Shader.createColorLambertShaderProgram();
		}
		mStage.setColorShaderProgram(mColorShaderProgram);

		OriginalStageTest test = new OriginalStageTest();
//		test.load(mStage);

		mPlayer = new Player();
		mLevelLoader = new LevelLoader();
		mLevelLoader.getLevelByIndex(2).load(mStage);

		mShaderProgramNeedsUpdate = true;

		mInputProcessor = new GameInputProcessor();
		((GameInputProcessor) mInputProcessor).setPlayer(mPlayer);
		((GameInputProcessor) mInputProcessor).setStage(mStage);
		Gdx.input.setInputProcessor(mInputProcessor);
	}

	public void setupLights() {
		Color ambientLightColor = new Color();
		ArrayList<Color> pointLightColors = new ArrayList<Color>();
		ArrayList<Float> pointLightPositions = new ArrayList<Float>();
		ArrayList<Float> pointLightDistances = new ArrayList<Float>();
		int pointLightCount = 0;

		SnapshotArray<Light> children = mStage.getLights();
		Light[] lights = children.begin();
		Vector3 position;
		for (int i = 0, n = children.size; i < n; i++) {
			Light light = lights[i];

			if (!light.isVisible()) {
				continue;
			}

			// Ambient light color is sum of all ambient lights.
			if (light instanceof AmbientLight) {
				ambientLightColor.add(light.getColor());
			} else if (light instanceof PointLight) {
				if (!light.isVisible()) {
					continue;
				}

				pointLightCount++;

				pointLightColors.add(light.getColor());

				position = light.getPosition();
				pointLightPositions.add(position.x);
				pointLightPositions.add(position.y);
				pointLightPositions.add(position.z);

				pointLightDistances.add(((PointLight) light).getDistance());
			}
		}
		children.end();

		if (Shader.MAX_POINT_LIGHTS != pointLightCount) {
			mShaderProgramNeedsUpdate = true;
			Shader.MAX_POINT_LIGHTS = pointLightCount;
		}

		mUniforms.setAmbientLightColor(ambientLightColor);
		if (pointLightCount > 0) {
			mUniforms.setPointLightColors(pointLightColors);
			mUniforms.setPointLightPositions(pointLightPositions);
			mUniforms.setPointLightDistances(pointLightDistances);
			mLightUniformsNeedRefresh = true;
		}
	}

	@Override
	public void dispose() {
		mSpriteBatch.dispose();
		mFont.dispose();
		mStage.dispose();
	}

	@Override
	public void render() {
		update();

		Color backgroundColor = mStage.getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		mStage.draw();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);

		mSpriteBatch.begin();
		mFont.draw(mSpriteBatch, Integer.toString(Gdx.graphics.getFramesPerSecond()), Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.9f);
		mSpriteBatch.end();

		mFPSLogger.log();
	}

	public void update() {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f);
		mStage.act(delta);

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

		}

		setupLights();

		if (mShaderProgramNeedsUpdate) {
			mShaderProgramNeedsUpdate = false;
			mShaderProgram.dispose();
			if (Game.lightingModel == LightingModel.PHONG) {
				mShaderProgram = Shader.createPhongShaderProgram();
			} else if (Game.lightingModel == LightingModel.LAMBERT) {
				mShaderProgram = Shader.createLambertShaderProgram();
			}
			if (Game.lightingModel == LightingModel.PHONG) {
				mColorShaderProgram = Shader.createColorPhongShaderProgram();
			} else if (Game.lightingModel == LightingModel.LAMBERT) {
				mColorShaderProgram = Shader.createColorLambertShaderProgram();
			}
			mStage.setShaderProgram(mShaderProgram);
			mStage.setColorShaderProgram(mColorShaderProgram);
		}

		if (mLightUniformsNeedRefresh) {
			mLightUniformsNeedRefresh = false;
			mUniforms.setUniforms(mShaderProgram);
			mUniforms.setUniforms(mColorShaderProgram);
		}
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
}
