package com.razh.tiling.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.razh.tiling.BillboardActor;
import com.razh.tiling.MeshStage;
import com.razh.tiling.Shader;
import com.razh.tiling.TilingGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen extends BasicScreen {
	private SpriteBatch mSpriteBatch;
	private BitmapFont mFont;

	public SplashScreen(TilingGame game) {
		super(game);

		setStage(new MeshStage());
		setInputProcessor(getStage());

		final BillboardActor backgroundActor = new BillboardActor();
		backgroundActor.setColor(new Color(Color.DARK_GRAY));
		backgroundActor.setPosition(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		backgroundActor.setWidth(Gdx.graphics.getWidth());
		backgroundActor.setHeight(Gdx.graphics.getHeight());
		backgroundActor.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				backgroundActor.addAction(
					sequence(
						parallel(
							fadeOut(0.5f),
							new Action() {
								@Override
								public boolean act(float delta) {
									Color color = mFont.getColor();
									color.a = backgroundActor.getColor().a;
									mFont.setColor(color);
									if (color.a == 0.0f) {
										return true;
									}

									return false;
								}
							}
						),
						new Action() {
							@Override
							public boolean act(float delta) {
								TilingGame game = getGame();
								game.setScreen(game.gameScreen);
								return true;
							}
						}
					)
				);

				return true;
			}
		});
		getStage().addActor(backgroundActor);

		mSpriteBatch = new SpriteBatch();
		mFont = new BitmapFont(Gdx.files.internal("fonts/helv-neue-96-bold.fnt"),
		                       Gdx.files.internal("fonts/helv-neue-96-bold.png"),
		                       false);

		getMeshStage().setShaderProgram(Shader.createBillboardShaderProgram());
	}

	@Override
	public void render(float delta) {
		getStage().act(delta);

		Color backgroundColor = getMeshStage().getColor();

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		getStage().draw();
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);

		mSpriteBatch.begin();
		mFont.draw(mSpriteBatch, "antiprism", Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		mSpriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
