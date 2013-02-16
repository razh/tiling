package com.razh.tiling;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.scenes.scene2d.Action;

public class ParticleSystemActor extends Actor3D {
	private Mesh mParticleMesh;

	public Properties properties;
	public Wind wind;

	public ParticleSystemActor() {
		addAction(
			new Action() {
				@Override
				public boolean act(float delta) {
					return false;
				}
			}
		);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	// ParticleSystem variables.
	static public class Properties {
		// Start particles at top left corner vs. filling the screen.
		public boolean originTopLeft = true;
		// Allow particles to be transparent.
		public boolean transparency = true;

		public int amount = 1500;
		public float scale = 1.0f;

		public float zMin = 0.0f;
		public float zMax = 1.0f;

		public float alphaMin = 0.1f;
		public float alphaMax = 1.3f;

		public float rotationSpeed = 1.0f;
	}

	static public class Wind {
		// Initial wind force.
		public int begin = 200;

		// Range of wind force.
		public int forceMin = 0;
		public int forceMax = 400;

		// Interval of time separating two wind bursts.
		public int timeMin = 1;
		public int timeMax = 6;

		public Direction direction = Direction.RIGHT;

		public enum Direction {
			LEFT,
			RIGHT,
			BOTH
		}
	}

}
