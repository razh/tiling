package com.razh.tiling;

public class Shader {
	
	private static String mVertexShader =
		"uniform mat4 projection;\n" +
		"uniform float rotation;\n" +
		"uniform vec3 translate;\n" +
		"uniform vec3 scale;\n" +
		"attribute vec3 a_position;\n" +
		"attribute vec3 a_normal;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
//			 "  vec3 position = vec3(0.0);\n" +
//			"  if (rotation != 0.0) {\n" +
//			"    float r_cos = cos(radians(rotation));\n" +
//			"    float r_sin = sin(radians(rotation));\n" +
//			"    mat2 rotationMatrix = mat2(r_cos, r_sin, -r_sin, r_cos);\n" +
//			"    position = rotationMatrix * (scale * a_position) + translate;\n" +
//			"  }\n" +
//			"  else {\n" +
//			"    position = scale * a_position + translate;\n" +
//			"  }\n" +
		"  vec3 position = scale * a_position + translate;\n" +
		"  gl_Position = projection * vec4(position, 1.0);\n" +
		"}";

	private static String mFragmentShader =
		"#ifdef GL_ES\n" +
		"precision mediump float;\n" +
		"#endif\n" +
		"uniform vec3 ambientLightColor;\n" +
		"uniform vec4 v_color;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  gl_FragColor = v_color;\n" +
		"}";
	
	public static void createShaderProgram() {
		
	}
}
