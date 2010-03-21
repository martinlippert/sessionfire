package com.sessionfive.core;

public class ShapeRotation {

	public static final ShapeRotation ZERO = new ShapeRotation(0, 0, 0);
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(angleX);
		result = prime * result + Float.floatToIntBits(angleY);
		result = prime * result + Float.floatToIntBits(angleZ);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		ShapeRotation other = (ShapeRotation) obj;
		return this.angleX == other.angleX && this.angleY == other.angleY
				&& this.angleZ == other.angleZ;
	}

}
