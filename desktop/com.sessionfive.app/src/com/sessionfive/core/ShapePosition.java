package com.sessionfive.core;

public class ShapePosition {

	private final float x;
	private final float y;
	private final float z;

	public static ShapePosition ZERO = new ShapePosition(0, 0, 0);

	public ShapePosition(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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

		ShapePosition other = (ShapePosition) obj;
		return this.x == other.x && this.y == other.y && this.z == other.z;
	}

	public ShapePosition add(ShapePosition additionalPosition) {
		return new ShapePosition(this.x + additionalPosition.getX(), this.y
				+ additionalPosition.getY(), this.z + additionalPosition.getZ());
	}

}
