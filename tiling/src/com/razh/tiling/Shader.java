package com.razh.tiling;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {
	public static int MAX_POINT_LIGHTS = 0;

	public static ShaderProgram createShaderProgram() {
		String vertex =
			"#define MAX_POINT_LIGHTS " + MAX_POINT_LIGHTS + ";\n" +
			"uniform vec3 ambientLightColor;\n" +
			"#if MAX_POINT_LIGHTS > 0\n" +
			"  uniform vec3 pointLightColor[MAX_POINT_LIGHTS];\n" +
			"  uniform vec3 pointLightPosition[MAX_POINT_LIGHTS];\n" +
			"  uniform float pointLightDistance[MAX_POINT_LIGHTS];\n" +
			"#endif\n" +
			"uniform mat4 projection;\n" +
			"uniform float rotation;\n" +
			"uniform vec3 translate;\n" +
			"uniform vec3 scale;\n" +
			"attribute vec3 a_position;\n" +
			"attribute vec3 a_normal;\n" +
			"\n" +
			"void main()\n" +
			"{\n" +
//			"  if"
			"  vec3 position = scale * a_position + translate;\n" +
			"  gl_Position = projection * vec4(position, 1.0);\n" +
			"}";

		String fragment =
			"#ifdef GL_ES\n" +
			"precision mediump float;\n" +
			"#endif\n" +
			"uniform vec4 v_color;\n" +
			"\n" +
			"void main()\n" +
			"{\n" +
			"  gl_FragColor = v_color;\n" +
			"}";
		System.out.println(vertex);

		ShaderProgram shaderProgram = new ShaderProgram(vertex, fragment);
		return shaderProgram;
	}
}
