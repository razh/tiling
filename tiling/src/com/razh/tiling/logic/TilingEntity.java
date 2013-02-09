package com.razh.tiling.logic;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
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

		setFlipping(true);

		getActor().addAction(
			sequence(
				rotateBy( 180, 0.4f, Interpolation.pow2 ),
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

		getActor().setRotationAxis(Vector3.Y.cpy().rotate(Vector3.Z, getActor().getOrientation()));
		flip();

		TilingEntity neighbor;
		for (int i = 0, n = getNeighbors().size(); i < n; i++) {
			neighbor = (TilingEntity) getNeighbors().get(i);

			neighbor.getActor().setRotationAxis(neighbor.getActor().vectorTo(getActor()));
			neighbor.flip();
		}
	}
}
