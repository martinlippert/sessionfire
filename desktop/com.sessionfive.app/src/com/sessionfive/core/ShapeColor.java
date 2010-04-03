package com.sessionfive.core;

public class ShapeColor {
	
	public static final ShapeColor BLACK = new ShapeColor(1f, 1f, 1f, 1f);
	
	private final float r;
	private final float g;
	private final float b;
	private final float a;
	
	public ShapeColor(float r, float g, float b, float a) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public float getR() {
		return r;
	}

	public float getG() {
		return g;
	}

	public float getB() {
		return b;
	}

	public float getA() {
		return a;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(a);
		result = prime * result + Float.floatToIntBits(b);
		result = prime * result + Float.floatToIntBits(g);
		result = prime * result + Float.floatToIntBits(r);
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
		ShapeColor other = (ShapeColor) obj;
		return this.r == other.r && this.b == other.b && this.g == other.g && this.a == other.a;
	}

}
