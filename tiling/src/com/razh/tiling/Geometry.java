package com.razh.tiling;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Factory class for generating geometry for various MeshActors.
 */
public class Geometry {
	public static Mesh createTriangularBipyramid() {
		return createBipyramid(3);
	}
	
	public static Mesh createOctahedron() {
		return createBipyramid(4);
	}
	
	public static Mesh createHexagonalBipyramid() {
		return createBipyramid(6);
	}
	
	public static Mesh createOctagonalBipyramid() {
		return createBipyramid(8);
	}
	
	public static Mesh createBipyramid(int subdivisions) {
		if (subdivisions < 3) {
			return null;
		}

		// Side vertices + the top and bottom vertices (2).
		int numVertices = (subdivisions + 2) * 3;
		// Three vertices per face. Two faces per side (bipyramid).
		int numIndices = subdivisions * 6;
		Mesh mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
                             true, numVertices, numIndices,
                             new VertexAttribute(Usage.Position, 3,
                                                 ShaderProgram.POSITION_ATTRIBUTE));
//                             new VertexAttribute(Usage.Normal, 3,
//                                                 ShaderProgram.NORMAL_ATTRIBUTE));

		float[] vertices = new float[numVertices];
		short[] indices = new short[numIndices];
		
		int vtxIndex = 0;
		int idxIndex = 0;
		
		float subdivAngle = (float) (Math.PI * 2 / subdivisions);

		// Top vertex.
		vertices[vtxIndex++] = 0.0f;
		vertices[vtxIndex++] = 0.0f;
		vertices[vtxIndex++] = 1.0f;
		
		// Side vertices.
		for (int i = 0; i < subdivisions; i++) {
			vertices[vtxIndex++] = (float) Math.sin(i * subdivAngle) * 60;
			vertices[vtxIndex++] = (float) Math.cos(i * subdivAngle) * 60;
			vertices[vtxIndex++] = 0.0f;

			indices[idxIndex++] = 0;
			indices[idxIndex++] = (short) (i + 1);
			indices[idxIndex++] = (short) ((i + 1) % subdivisions + 1);
			
			// Indices for bottom half of bipyramid.
			indices[idxIndex++] = (short) (subdivisions + 2);
			indices[idxIndex++] = (short) ((i + 1) % subdivisions + 1);
			indices[idxIndex++] = (short) (i + 1);
		}
		
		// Bottom vertex.
		vertices[vtxIndex++] = 0.0f;
		vertices[vtxIndex++] = 0.0f;
		vertices[vtxIndex++] = -1.0f;
	
		mesh.setVertices(vertices);
		mesh.setIndices(indices);

		return mesh;
	}
}
