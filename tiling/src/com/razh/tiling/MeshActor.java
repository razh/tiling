package com.razh.tiling;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MeshActor extends Actor3D {
	private Mesh mMesh;
	private Material mMaterial;

	private Matrix4 mModelMatrix;
	private Matrix3 mNormalMatrix;

	private ShaderProgram mShaderProgram;

	private Vector3 mRotationAxis;
	private float mOrientation;

	private Entity mEntity;

	private ArrayList<Vector2> mVertices;

	public MeshActor() {
		super();

		mModelMatrix = new Matrix4();
		mNormalMatrix = new Matrix3();

		setRotationAxis(Vector3.Y);
		setOrientation(0.0f);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (mEntity != null) {
			mEntity.act(delta);
		}
	}

	public void draw(ShaderProgram shaderProgram, float stroke) {
		if (mShaderProgram != shaderProgram) {
			setShaderProgram(shaderProgram);
		}

		draw(stroke);
	}

	public void draw(float stroke) {
		mModelMatrix.idt()
		            .translate(getPosition())
		            .rotate(getRotationAxis(), getRotation())
		            .rotate(Vector3.Z, getOrientation())
		            .scale(getWidth() - stroke,
		                   getHeight() - stroke,
		                   getDepth() - stroke );
		mShaderProgram.setUniformMatrix("modelMatrix", mModelMatrix);

		mNormalMatrix.set(mModelMatrix.cpy().inv()).transpose();
		mShaderProgram.setUniformMatrix("normalMatrix", mNormalMatrix);

		mShaderProgram.setUniformf("diffuse", getColor().r, getColor().g, getColor().b);
		mShaderProgram.setUniformf("opacity", getColor().a);

		if (hasMaterial()) {
			mMaterial.bind(mShaderProgram);
		}

		if (hasMesh()) {
			getMesh().render(mShaderProgram, GL20.GL_TRIANGLES);
		}
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled) {
			return null;
		}

		if (getVertices() == null) {
			// Default to simple AABB hit-testing if no vertex data.
			if (Math.abs(x - getX()) <= getWidth() && Math.abs(y - getY()) <= getHeight()) {
				return this;
			}

			return null;
		}

		if (contains(x, y)) {
			return this;
		}

		return null;
	}

	public boolean contains(float x, float y) {
		Vector2 point = worldToLocalCoordinates(new Vector2(x, y));
		return Intersector.isPointInPolygon(getVertices(), point);
	}

	@Override
	public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
		return parentCoords;
	}

	public Vector2 worldToLocalCoordinates(Vector2 worldCoords) {
		return worldCoords.cpy()
		                  .sub(getX(), getY())
		                  .rotate(-getOrientation())
		                  .div(getWidth(), getHeight());
	}

	public Vector2 localToWorldCoordinates(Vector2 localCoords) {
		return localCoords.cpy()
		                  .mul(getWidth(), getHeight())
		                  .rotate(getOrientation())
		                  .add(getX(), getY());
	}

	public Vector2[] getIntersectingEdge(Vector2 point) {
		point = worldToLocalCoordinates(point);

		ArrayList<Vector2> vertices = getVertices();
		Vector2 p0, p1;
		for (int i = 0, n = vertices.size(); i < n; i++) {
			p0 = vertices.get(i);
			p1 = vertices.get((i + 1) % n);

			if (Intersector.intersectSegments(Vector2.Zero, point, p0, p1, null)) {
				Vector2[] segment = new Vector2[]{p0, p1};
				return segment;
			}
		}

		return null;
	}

	/**
	 * @param segment Must be an array of size 2.
	 * @return Angle of the shortest line from the actor's center to the given segment.
	 */
	public float getSegmentBisectorAngle(Vector2[] segment) {
		if (segment == null || segment.length != 2) {
			return 0.0f;
		}

		// Midpoint between the two Vector2s.
		Vector2 bisection = segment[0].cpy()
		                              .add(segment[1])
		                              .mul(0.5f);

		// Convert to world coordinates and find distance to actor center.
		// Calculate the angle from there.
		return localToWorldCoordinates(bisection).sub(getPosition2D())
		                                         .angle();
	}

	public Vector3 getRotationAxis() {
		return mRotationAxis;
	}

	public void setRotationAxis(Vector3 rotationAxis) {
		mRotationAxis = rotationAxis;
	}

	public float getOrientation() {
		return mOrientation;
	}

	public void setOrientation(float orientation) {
		mOrientation = orientation;
	}

	public Mesh getMesh() {
		return mMesh;
	}

	public void setMesh(Mesh mesh) {
		mMesh = mesh;
	}

	public boolean hasMesh() {
		return getMesh() != null;
	}

	public ArrayList<Vector2> getVertices() {
		return mVertices;
	}

	public void setVertices(float[] vertices) {
		int vertexCount = vertices.length / 2;
		ArrayList<Vector2> vertexList = new ArrayList<Vector2>(vertexCount);

		float x, y;
		for (int i = 0; i < vertexCount; i++) {
			x = vertices[2 * i];
			y = vertices[2 * i + 1];

			vertexList.add(new Vector2(x, y));
		}

		mVertices = vertexList;
	}

	public void setVertices(ArrayList<Vector2> vertices) {
		mVertices = vertices;
	}

	public Material getMaterial() {
		return mMaterial;
	}

	public void setMaterial(Material material) {
		mMaterial = material;
	}

	public boolean hasMaterial() {
		return getMaterial() != null;
	}

	public Matrix4 getModelMatrix() {
		return mModelMatrix;
	}

	public Matrix3 getNormalMatrix() {
		return mNormalMatrix;
	}

	public Entity getEntity() {
		return mEntity;
	}

	public void setEntity(Entity entity) {
		mEntity = entity;
		mEntity.setActor(this);
	}

	public boolean hasEntity() {
		return mEntity != null;
	}

	public ShaderProgram getShaderProgram() {
		return mShaderProgram;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		mShaderProgram = shaderProgram;
	}
}
