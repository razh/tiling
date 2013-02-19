package com.razh.tiling;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.tiling.TilingGame.LightingModel;

public class TilingMeshStage extends MeshStage {
	private float mScale;
	private float mStroke;

	private MeshGroup mColorRoot;
	private ShaderProgram mColorShaderProgram;
	private ShaderProgram mPointLightShaderProgram;
	private ShaderProgram mShadowShaderProgram;

	private Uniforms mUniforms;

	private Vector2 mShadowOffset;
	private Color mShadowColor;

	private boolean mShaderProgramNeedsUpdate;
	private boolean mLightUniformsNeedRefresh;

	private SnapshotArray<Light> mLights;

	private static final float CAMERA_POSITION_Z = 1000.0f;
	private static final float CAMERA_FAR = 2000.0f;

	// Camera offset for virtual viewport.
	private Vector2 mCameraOffset;
	private Vector2 mCameraScale = new Vector2();

	public TilingMeshStage() {
		super();

		getCamera().position.z = CAMERA_POSITION_Z;
		getCamera().far = CAMERA_FAR;
		mCameraOffset = new Vector2();

		setScale(1.0f);

		mColorRoot = new MeshGroup();
		mColorRoot.setStage(this);

		mLights = new SnapshotArray<Light>(Light.class);

		// Set shader programs.
		ShaderProgram shaderProgram = null;
		ShaderProgram colorShaderProgram = null;
		if (TilingGame.lightingModel == LightingModel.PHONG) {
			shaderProgram = Shader.createPhongShaderProgram();
			colorShaderProgram = Shader.createColorPhongShaderProgram();
		} else if (TilingGame.lightingModel == LightingModel.LAMBERT) {
			shaderProgram = Shader.createLambertShaderProgram();
			colorShaderProgram = Shader.createColorLambertShaderProgram();
		}
		mUniforms = new Uniforms();

		setShadowColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		setShadowOffset(new Vector2(0.0f, 0.0f));

		setShaderProgram(shaderProgram);
		setColorShaderProgram(colorShaderProgram);
		setPointLightShaderProgram(Shader.createBillboardShaderProgram());
		setShadowShaderProgram(Shader.createShadowShaderProgram());

		mShaderProgramNeedsUpdate = true;
	}

	@Override
	public void draw() {
		getCamera().update();

		// Render normal objects.
		if (getShaderProgram() != null) {
			getShaderProgram().begin();
			getShaderProgram().setUniformMatrix("projectionMatrix", getCamera().projection);
			getShaderProgram().setUniformMatrix("viewMatrix", getCamera().view);

			getRoot().draw(getShaderProgram(), getStroke());

			getShaderProgram().end();
		}

		// Render shadows of multi-color objects.
		if (mShadowShaderProgram != null) {
			// Set blend function to multiply.
			Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO);
			mShadowShaderProgram.begin();

			mShadowShaderProgram.setUniformMatrix("projectionMatrix", getCamera().projection);
			mShadowShaderProgram.setUniformMatrix("viewMatrix", getCamera().view);

			mShadowShaderProgram.setUniformf("shadowColor", getShadowColor());
			mShadowShaderProgram.setUniformf("shadowOffset", getShadowOffset());

			mColorRoot.drawShadow(mShadowShaderProgram, getStroke());

			mShadowShaderProgram.end();
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}

		// Render multi-color objects.
		if (mColorShaderProgram != null) {
			mColorShaderProgram.begin();

			mColorShaderProgram.setUniformMatrix("projectionMatrix", getCamera().projection);
			mColorShaderProgram.setUniformMatrix("viewMatrix", getCamera().view);

			mColorRoot.draw(mColorShaderProgram, getStroke());

			mColorShaderProgram.end();
		}

		// Render light positions.
		if (mPointLightShaderProgram != null) {
			mPointLightShaderProgram.begin();

			mPointLightShaderProgram.setUniformMatrix("modelViewProjectionMatrix", getCamera().combined);

			Light[] lights = mLights.begin();
			for (int i = 0, n = mLights.size; i < n; i++) {
				Light light = lights[i];

				if (!light.isVisible()) {
					continue;
				}

				if (light instanceof PointLight) {
					light.draw(mPointLightShaderProgram);
				}
			}
			mLights.end();

			mPointLightShaderProgram.end();
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		mColorRoot.act(delta);

		Light[] lights = mLights.begin();
		for (int i = 0, n = mLights.size; i < n; i++) {
			Light light = lights[i];

			light.act(delta);
		}
		mLights.end();

		// Setup lights.
		setupLights();

		// Setup shaders.
		ShaderProgram shaderProgram = getShaderProgram();
		ShaderProgram colorShaderProgram = getColorShaderProgram();
		if (mShaderProgramNeedsUpdate) {
			mShaderProgramNeedsUpdate = false;
			getShaderProgram().dispose();
			getColorShaderProgram().dispose();

			if (TilingGame.lightingModel == LightingModel.PHONG) {
				shaderProgram = Shader.createPhongShaderProgram();
				colorShaderProgram = Shader.createColorPhongShaderProgram();
			} else if (TilingGame.lightingModel == LightingModel.LAMBERT) {
				shaderProgram = Shader.createLambertShaderProgram();
				colorShaderProgram = Shader.createColorLambertShaderProgram();
			}

			setShaderProgram(shaderProgram);
			setColorShaderProgram(colorShaderProgram);
		}

		if (mLightUniformsNeedRefresh) {
			mLightUniformsNeedRefresh = false;
			mUniforms.setUniforms(shaderProgram);
			mUniforms.setUniforms(colorShaderProgram);
		}
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		Vector2 actorCoords = new Vector2(stageX, stageY).div(getScale()).sub(mCameraOffset);
//		System.out.println(actorCoords);
		getRoot().parentToLocalCoordinates(actorCoords);
		Actor hit = getRoot().hit(actorCoords.x, actorCoords.y, touchable);
		if (hit == null) {
			return getColorRoot().hit(actorCoords.x, actorCoords.y, touchable);
		} else {
			return hit;
		}
	}

	public float getScale() {
		return mScale;
	}

	public void setScale(float scale) {
		if (mScale != scale) {
			float scaleWidth = TilingGame.WIDTH / scale;
			float scaleHeight = TilingGame.HEIGHT / scale;
			setViewport(scaleWidth, scaleHeight, true);
			getCamera().position.set(0.5f * scaleWidth,
			                         0.5f * scaleHeight,
			                         CAMERA_POSITION_Z);
			// Distance from center of camera position to center of viewport.
			mCameraOffset.set(0.5f * (Gdx.graphics.getWidth() - TilingGame.WIDTH) / scale,
			                  0.5f * (Gdx.graphics.getHeight() - TilingGame.HEIGHT) / scale);
			System.out.println("GUTTER: " + getGutterWidth() + ", " + getGutterHeight());
			System.out.println("CAMERA: " + getCamera().viewportWidth + ", " + getCamera().viewportHeight);
			mCameraScale.set(Gdx.graphics.getWidth() / (getCamera().viewportWidth + 2 * getGutterWidth()),
			                 Gdx.graphics.getHeight() / (getCamera().viewportHeight + 2 * getGutterHeight()));
		}

		mScale = scale;
	}

	public float getStroke() {
		return mStroke;
	}

	public void setStroke(float stroke) {
		mStroke = stroke;
	}

	public MeshGroup getColorRoot() {
		return mColorRoot;
	}

	public void addColorActor(MeshActor actor) {
		mColorRoot.addActor(actor);
	}

	public Color getShadowColor() {
		return mShadowColor;
	}

	public void setShadowColor(Color shadowColor) {
		mShadowColor = shadowColor;
	}

	public Vector2 getShadowOffset() {
		return mShadowOffset;
	}

	public void setShadowOffset(Vector2 shadowOffset) {
		mShadowOffset = shadowOffset;
	}

	public SnapshotArray<Light> getLights() {
		return mLights;
	}

	public void addLight(Light light) {
		mLights.add(light);
	}

	public void setupLights() {
		Color ambientLightColor = new Color();
		ArrayList<Color> pointLightColors = new ArrayList<Color>();
		ArrayList<Float> pointLightPositions = new ArrayList<Float>();
		ArrayList<Float> pointLightDistances = new ArrayList<Float>();
		int pointLightCount = 0;

		SnapshotArray<Light> children = getLights();
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

	public ShaderProgram getColorShaderProgram() {
		return mColorShaderProgram;
	}

	public void setColorShaderProgram(ShaderProgram shaderProgram) {
		mColorShaderProgram = shaderProgram;
	}

	public void setPointLightShaderProgram(ShaderProgram shaderProgram) {
		mPointLightShaderProgram = shaderProgram;
	}

	public void setShadowShaderProgram(ShaderProgram shaderProgram) {
		mShadowShaderProgram = shaderProgram;
	}

	@Override
	public void clearActors() {
		super.clearActors();
		mColorRoot.clear();
		mLights.clear();
	}

	@Override
	public void dispose() {
		super.dispose();
		mColorShaderProgram.dispose();
		mPointLightShaderProgram.dispose();
		mShadowShaderProgram.dispose();
	}
}
