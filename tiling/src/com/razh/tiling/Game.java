package com.razh.tiling;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Game implements ApplicationListener {
	private MeshStage mStage;
	private FPSLogger mFPSLogger;
	private boolean mGL20;

	private String mVertexShader =
		"uniform mat4 projection;\n" +
		"uniform float rotation;\n" +
		"uniform vec3 translate;\n" +
		"uniform vec3 scale;\n" +
		"attribute vec3 a_position;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
//		 "  vec3 position = vec3(0.0);\n" +
//		"  if (rotation != 0.0) {\n" +
//		"    float r_cos = cos(radians(rotation));\n" +
//		"    float r_sin = sin(radians(rotation));\n" +
//		"    mat2 rotationMatrix = mat2(r_cos, r_sin, -r_sin, r_cos);\n" +
//		"    position = rotationMatrix * (scale * a_position) + translate;\n" +
//		"  }\n" +
//		"  else {\n" +
//		"    position = scale * a_position + translate;\n" +
//		"  }\n" +
		"  vec3 position = scale * a_position + translate;\n" +
		"  gl_Position = projection * vec4(position, 1.0);\n" +
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
		
		MeshActor meshActor = new MeshActor();
		meshActor.setWidth(100.0f);
		meshActor.setHeight(100.0f);
		meshActor.setDepth(20.0f);
		meshActor.setPosition(0.0f, 0.0f, -meshActor.getDepth());
		meshActor.setColor(Color.BLUE);
		meshActor.setMesh(Geometry.createTriangularBipyramid());
		meshActor.addAction(
			sequence(
				moveBy(200, 100, 2.0f, Interpolation.pow2),
				moveBy(-100, 100, 2.0f, Interpolation.pow2)
			)
		);
		mStage.addActor(meshActor);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		update();

		Color backgroundColor = mStage.getColor();

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
