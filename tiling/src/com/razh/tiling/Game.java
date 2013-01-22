package com.razh.tiling;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Game implements ApplicationListener {
	private MeshStage mStage;
	private FPSLogger mFPSLogger;
	private boolean mGL20;
	
	private String mVertexShader =
		"uniform mat4 projection;\n" +
		"uniform float rotation;\n" +
		"uniform vec2 translate;\n" +
		"uniform vec2 scale;\n" +
		"attribute vec2 a_position;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  vec2 position = vec2(0.0);\n" +
		"  if (rotation != 0.0) {\n" +
		"    float r_cos = cos(radians(rotation));\n" +
		"    float r_sin = sin(radians(rotation));\n" +
		"    mat2 rotationMatrix = mat2(r_cos, r_sin, -r_sin, r_cos);\n" +
		"    position = rotationMatrix * (scale * a_position) + translate;\n" +
		"  }\n" +
		"  else {\n" +
		"    position = scale * a_position + translate;\n" +
		"  }\n" +
		"  gl_Position = projection * vec4(position, 0.0, 1.0);\n" +
		"}";
	
	private String mFragmentShader =
		"#ifdef GL_ES\n" +
		"precision mediump float;\n" +
		"#endif\n" +
		"uniform vec4 v_color;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  gl_FragColor = v_color;\n" +
		"}";
	
	private ShaderProgram mShaderProgram;

	@Override
	public void create() {		
		Gdx.graphics.setVSync(true);
		
		mStage = new MeshStage();
		mFPSLogger = new FPSLogger();

		mGL20 = Gdx.graphics.isGL20Available();
		if (!mGL20) {
			Gdx.app.exit();
		}
		
		mShaderProgram = new ShaderProgram(mVertexShader, mFragmentShader);
		System.out.println("Compiled: " + mShaderProgram.isCompiled());
		
		mStage.setShaderProgram(mShaderProgram);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		update();

		Color backgroundColor = mStage.getColor();
		System.out.println( backgroundColor);
		
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mStage.draw();
		
		mFPSLogger.log();
	}
	
	public void update() {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f);
		mStage.act(delta);
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
