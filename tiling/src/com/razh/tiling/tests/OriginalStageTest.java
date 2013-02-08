package com.razh.tiling.tests;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.razh.tiling.AmbientLight;
import com.razh.tiling.Game;
import com.razh.tiling.Geometry;
import com.razh.tiling.MeshActor;
import com.razh.tiling.MeshMaterial;
import com.razh.tiling.MeshStage;
import com.razh.tiling.PointLight;
import com.razh.tiling.Game.LightingModel;

public class OriginalStageTest extends StageTest {

	@Override
	public void load(MeshStage stage) {
		MeshMaterial material = new MeshMaterial(new Color(0.33f, 0.33f, 0.33f, 1.0f), new Color(Color.WHITE), new Color(Color.BLACK), 50);
		if (Game.lightingModel == LightingModel.PHONG) {
			material.setShiny(true);
		}

		MeshActor meshActor = new MeshActor();
		meshActor.setWidth(100.0f);
		meshActor.setHeight(100.0f);
		meshActor.setDepth(50.0f);
		meshActor.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 200);
		meshActor.setColor(new Color(Color.BLUE).add(new Color(0.25f,0.0f,0.0f,0.0f)));
		meshActor.setMaterial(material);
		meshActor.setOrientation(180);
		meshActor.setMesh(Geometry.createTriangularBipyramid());
		meshActor.addAction(
			parallel(
				forever(
					sequence(
						moveBy(200, 200, 2.0f, Interpolation.pow2),
						moveBy(-200, 200, 2.0f, Interpolation.pow2),
						moveBy(-200, -200, 2.0f, Interpolation.pow2),
						moveBy(200, -200, 2.0f, Interpolation.pow2)
					)
				),
				forever(
					rotateBy(360, 4.0f)
				)
			)
		);
		stage.addActor(meshActor);

		MeshActor meshActor2 = new MeshActor();
		meshActor2.setWidth(100.0f);
		meshActor2.setHeight(100.0f);
		meshActor2.setDepth(50.0f);
		meshActor2.setPosition(200, 200);
		meshActor2.setColor(new Color(Color.RED).add(new Color(0.0f, 0.0f, 0.25f, 0.0f)));
		meshActor2.setMesh(Geometry.createOctagonalBipyramid());
		meshActor2.setMaterial(material);
		meshActor2.addAction(
			forever(
				parallel(
					rotateBy(360, 4.0f),
					sequence(
						alpha(0.0f, 1.0f),
						alpha(1.0f, 1.0f)
					)
				)
			)
		);
		stage.addActor(meshActor2);

		MeshActor meshActor3 = new MeshActor();
		meshActor3.setWidth(100.0f);
		meshActor3.setHeight(100.0f);
		meshActor3.setDepth(50.0f);
		meshActor3.setPosition(800, 200);
		meshActor3.setColor(new Color(Color.WHITE));
		meshActor3.setMesh(Geometry.createOctagonalBipyramid());
		meshActor3.setMaterial(material);
		stage.addActor(meshActor3);

		MeshActor meshActor4 = new MeshActor();
		meshActor4.setWidth(100.0f);
		meshActor4.setHeight(100.0f);
		meshActor4.setDepth(50.0f);
		meshActor4.setPosition(800, 600);
		meshActor4.setColor(new Color(Color.GRAY));
		meshActor4.setMesh(Geometry.createOctahedron());
		meshActor4.setMaterial(material);
		meshActor4.addAction(
			forever(
				rotateBy(360, 2.0f)
			)
		);
		meshActor4.setRotationAxis(new Vector3(Vector3.X).add(Vector3.Y).nor());
		stage.addActor(meshActor4);

//		for (int i = 0; i < 50;i++) {
		MeshActor mA5 = new MeshActor();
		mA5.setWidth(50.0f);
		mA5.setHeight(50.0f);
		mA5.setDepth(50.0f);
//		mA5.setPosition(100 + 20 * i, 500);
		mA5.setPosition(100, 500);
		mA5.setColor(new Color(Color.GREEN));
		mA5.setMesh(Geometry.createBicolorBipyramid(4, new Color(Color.ORANGE), new Color(Color.MAGENTA)));
//		mA5.setMesh(Geometry.createOctahedron());
		mA5.setMaterial(material);
		mA5.addAction(
			forever(
				rotateBy(360, 2.0f)
			)
		);
		stage.addColorActor(mA5);
//		}


		AmbientLight aLight = new AmbientLight();
		aLight.setColor(0.25f, 0.25f, 0.25f, 1.0f);
		stage.addLight(aLight);

		PointLight pLight = new PointLight();
		pLight.setColor(new Color(Color.RED));
		pLight.setPosition(200, Gdx.graphics.getHeight() / 2 + 50, 100);
		pLight.setWidth(3);
		pLight.setHeight(3);
		pLight.addAction(
			forever(
				sequence(
					moveBy(600, 0, 3.0f, Interpolation.pow2),
					moveBy(-600, 0, 3.0f, Interpolation.pow2)
				)
			)
		);
		pLight.setDistance(600);
		stage.addLight(pLight);

		PointLight pLight2 = new PointLight();
		pLight2 = new PointLight();
		pLight2.setColor(new Color(Color.BLUE));
		pLight2.setPosition(800, Gdx.graphics.getHeight() / 2, 100);
		pLight2.setWidth(3);
		pLight2.setHeight(3);
		pLight2.setDistance(800);
		stage.addLight(pLight2);

		PointLight pLight3 = new PointLight();
		pLight3 = new PointLight();
		pLight3.setColor(new Color(Color.GREEN));
		pLight3.setPosition(500, Gdx.graphics.getHeight() / 2 - 200, 100);
		pLight3.setWidth(3);
		pLight3.setHeight(3);
		pLight3.setDistance(8000);
		stage.addLight(pLight3);

		PointLight pLight4 = new PointLight();
		pLight4 = new PointLight();
		pLight4.setColor(new Color(Color.WHITE));
		pLight4.setPosition(500, Gdx.graphics.getHeight() / 2 + 200, 100);
		pLight4.setWidth(3);
		pLight4.setHeight(3);
		pLight4.setDistance(2000);
		stage.addLight(pLight4);
	}
}
