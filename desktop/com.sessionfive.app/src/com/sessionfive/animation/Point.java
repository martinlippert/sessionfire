package com.sessionfive.animation;

public class Point {

	private final float x, y, z;

	public Point(float x, float y, float z) {
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
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;

		Point other = (Point) obj;
		return this.x == other.x && this.y == other.y && this.z == other.z;
	}
}
