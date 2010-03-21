package com.sessionfive.core;

public class ShapeSize {

	public static final ShapeSize ZERO = new ShapeSize(0, 0, 0);

	private final float width;
	private final float height;
	private final float depth;

	public ShapeSize(float width, float height, float depth) {
		super();
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getDepth() {
		return depth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(depth);
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + Float.floatToIntBits(width);
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

		ShapeSize other = (ShapeSize) obj;
		return this.width == other.width && this.height == other.height
				&& this.depth == other.depth;
	}

}
