package com.razh.tiling.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.razh.tiling.BillboardActor;
import com.razh.tiling.MeshStage;
import com.razh.tiling.Shader;

public class SplashScreen extends DefaultScreen {

	public SplashScreen() {
		setStage(new MeshStage());

		final BillboardActor backgroundActor = new BillboardActor();
		backgroundActor.setColor(new Color(0.75f, 0.25f, 0.25f, 1.0f));
		backgroundActor.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		backgroundActor.setWidth(Gdx.graphics.getWidth());
		backgroundActor.setHeight(Gdx.graphics.getHeight());

		getStage().addActor(backgroundActor);

		getStage().setShaderProgram(Shader.createBillboardShaderProgram());
	}

	@Override
	public void render(float delta) {
		Color backgroundColor = getStage().getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		getStage().draw();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
