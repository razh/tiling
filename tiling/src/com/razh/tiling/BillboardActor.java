package com.razh.tiling;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BillboardActor extends Actor {
	private static Mesh sMesh;

	public BillboardActor() {
		if (sMesh == null) {
			sMesh = Geometry.createBillboard();
		}
	}
}
