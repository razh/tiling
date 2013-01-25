package com.razh.tiling;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {
	public static int MAX_POINT_LIGHTS = 0;

	public static ShaderProgram createShaderProgram() {
		String vertex =
			"#define MAX_POINT_LIGHTS " + MAX_POINT_LIGHTS + "\n" +
			"uniform vec3 ambientLightColor;\n" +
			"#if MAX_POINT_LIGHTS > 0\n" +
			"  uniform vec3 pointLightColor[MAX_POINT_LIGHTS];\n" +
			"  uniform vec3 pointLightPosition[MAX_POINT_LIGHTS];\n" +
			"  uniform float pointLightDistance[MAX_POINT_LIGHTS];\n" +
			"#endif\n" +
			"uniform mat4 projectionMatrix;\n" +
			"uniform mat4 modelViewMatrix;\n" +
			"uniform mat3 normalMatrix;\n" +
			"uniform float rotation;\n" +
			"uniform vec3 translate;\n" +
			"uniform vec3 scale;\n" +
			"attribute vec3 a_position;\n" +
			"attribute vec3 a_normal;\n" +
			"varying vec3 v_lightFront;\n" +
			"\n" +
			"void main()\n" +
			"{\n" +
			"  mat3 rotationMatrix = mat3(1.0);\n" +
			"  if (rotation != 0.0) {\n" +
			"    float r_cos = cos(radians(rotation));\n" +
			"    float r_sin = sin(radians(rotation));\n" +
			"    rotationMatrix[0][0] = r_cos;\n" +
			"    rotationMatrix[0][2] = -r_sin;\n" +
			"    rotationMatrix[2][0] = r_sin;\n" +
			"    rotationMatrix[2][2] = r_cos;\n" +
			"  }\n" +
			"  vec3 position = rotationMatrix * (scale * a_position) + translate;\n" +
			"  vec4 mvPosition = modelViewMatrix * vec4(position, 1.0);\n" +
			"  vec3 transformedNormal = normalize(normalMatrix * a_normal);\n" +
			"  v_lightFront = vec3(0.0);\n" +
			"  #if MAX_POINT_LIGHTS > 0\n" +
			"    for (int i = 0; i < MAX_POINT_LIGHTS; i++) {\n" +
			"      vec4 lightPosition = modelViewMatrix * vec4(pointLightPosition[i], 1.0);\n" +
			"      vec3 lightVector = lightPosition.xyz - mvPosition.xyz;\n" +
			"      float lightDistance = 1.0;\n" +
			"      if (pointLightDistance[i] > 0.0) {\n" +
			"        lightDistance = 1.0 - min((length(lightVector) / pointLightDistance[i]), 1.0);\n" +
			"      }\n" +
			"      lightVector = normalize(lightVector);\n" +
			"      float dotProduct = dot(transformedNormal, lightVector);\n" +
			"      vec3 pointLightWeighting = vec3(max(dotProduct, 0.0));\n" +
			"      v_lightFront += pointLightColor[i] * pointLightWeighting * lightDistance;\n" +
			"    }\n" +
			"  #endif\n" +
			"  v_lightFront += ambientLightColor;\n" +
			"  gl_Position = projectionMatrix * mvPosition;\n" +
			"}";

		String fragment =
			"#ifdef GL_ES\n" +
			"precision mediump float;\n" +
			"#endif\n" +
			"uniform vec4 color;\n" +
			"varying vec3 v_lightFront;\n" +
			"\n" +
			"void main()\n" +
			"{\n" +
			"  gl_FragColor = color;\n" +
			"  gl_FragColor.xyz *= v_lightFront;\n" +
			"}";
		System.out.println(vertex);
		System.out.println(fragment);

		ShaderProgram shaderProgram = new ShaderProgram(vertex, fragment);
	 	System.out.println("Compiled: " + shaderProgram.isCompiled() + "----------");
		return shaderProgram;
	}

	public static ShaderProgram createPointLightShaderProgram() {
		String vertex =
			"uniform mat4 modelViewProjectionMatrix;\n" +
			"uniform vec3 translate;\n" +
			"uniform vec3 scale;\n" +
			"attribute vec3 a_position;\n" +
			"\n" +
			"void main()\n" +
			"{\n" +
			"  gl_PointSize = 5.0;\n" +
			"  gl_Position = modelViewProjectionMatrix * vec4(scale * a_position + translate, 1.0);\n" +
			"}";

		String fragment =
			"#ifdef GL_ES\n" +
			"precision mediump float;\n" +
			"#endif\n" +
			"uniform vec4 color;\n" +
			"\n" +
			"void main()\n" +
			"{\n" +
			"  gl_FragColor = color;\n" +
			"}";

		ShaderProgram shaderProgram = new ShaderProgram(vertex, fragment);
		System.out.println("PLCompiled: " + shaderProgram.isCompiled() + "----------");
		return shaderProgram;
	}
}
