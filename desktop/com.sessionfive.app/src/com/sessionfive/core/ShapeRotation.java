package com.sessionfive.core;

public class ShapeRotation {

	private final float angleX;
	private final float angleY;
	private final float angleZ;

	public ShapeRotation(float angleX, float angleY, float angleZ) {
		super();
		this.angleX = angleX;
		this.angleY = angleY;
		this.angleZ = angleZ;
	}

	public float getRotationAngleX() {
		return angleX;
	}

	public float getRotationAngleY() {
		return angleY;
	}

	public float getRotationAngleZ() {
		return angleZ;
	}

}
