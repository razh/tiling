package com.razh.tiling;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class Light extends Actor3D {
	public abstract void draw(ShaderProgram shaderProgram, float parentAlpha);
};
