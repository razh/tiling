package com.razh.tiling.logic;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.Action;

public class TilingEntity extends GraphEntity {
	private boolean mFlipping;

	public TilingEntity() {
		super();
		setFlipping(false);
	}

	public boolean isFlipping() {
		return mFlipping;
	}

	public void setFlipping(boolean flipping) {
		mFlipping = flipping;
	}

	public void flip() {
		if (isFlipping()) {
			return;
		}

		getActor().addAction(
			sequence(
				rotateBy( 180, 2.0f ),
				new Action() {
					@Override
					public boolean act(float delta) {
						setFlipping(false);
						return true;
					}
				}
			)
		);
	}

	public void touch() {
		if (isFlipping()) {
			return;
		}

		flip();

		for (int i = 0, n = getNeighbors().size(); i < n; i++) {
			((TilingEntity) getNeighbors().get(i)).flip();
		}
	}
}
