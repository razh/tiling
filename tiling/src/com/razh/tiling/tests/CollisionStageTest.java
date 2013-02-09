package com.razh.tiling.tests;

import com.badlogic.gdx.graphics.Color;
import com.razh.tiling.MeshStage;
import com.razh.tiling.PointLight;
import com.razh.tiling.Shader;

public class CollisionStageTest extends StageTest {

	@Override
	public void load(MeshStage stage) {
		stage.setTestShaderProgram(Shader.createPointLightShaderProgram());
		PointLight test;
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {
				test = new PointLight();
				test.setPosition(250 + i * 5, 200 + j * 5);
				test.setColor(new Color(Color.RED));
				test.setWidth(1);
				test.setHeight(1);
				stage.addTest(test);
			}
		}
	}

}
