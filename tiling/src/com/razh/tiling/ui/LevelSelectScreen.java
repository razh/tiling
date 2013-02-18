package com.razh.tiling.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.razh.tiling.TilingGame;

public class LevelSelectScreen extends BasicScreen {

	private Carousel mCarousel;
	private Table mContainer;

	public LevelSelectScreen(TilingGame game) {
		super(game);

		Stage stage = new Stage();
		setStage(stage);
		setInputProcessor(stage);

		mContainer = new Table();
		stage.addActor(mContainer);
		mContainer.setFillParent(true);

		Table table = new Table();

		mCarousel = new Carousel(table);
		mContainer.add(mCarousel);

		Skin skin = new Skin();
		skin.add("image", new NinePatch(new Texture(Gdx.files.internal("data/gray-50-alpha-50-square.png")), 1, 1, 1, 1));
		skin.load(Gdx.files.internal("ui/buttons.json"));

		table.pad(10).defaults().space(40);
		Table subTable;
		TextButton button;
		for (int i = 0; i < 5; i++) {
			subTable = new Table();
			subTable.defaults().space(4);
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 5; k++) {
					button = new TextButton(i + "" + j + "" + k, skin);
					button.setWidth(Gdx.graphics.getWidth() * 0.75f);
					button.setHeight(Gdx.graphics.getHeight() * 0.75f);
					button.pad(20.0f);

					subTable.add(button);
				}
				subTable.row();
			}
			table.add(subTable);
		}
	}

	@Override
	public void render(float delta) {
		getStage().act(delta);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
		getStage().draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		super.dispose();
	}

}
