package com.sessionfive.shapes;

import javax.media.opengl.GL2;

import com.sessionfive.core.AbstractShape;

public class TextShape extends AbstractShape {

	public TextShape(float x, float y, float z, float rotation) {
		super(x, y, z, rotation);
	}

	@Override
	public float getWidth() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public void display(GL2 gl) {
	}

	@Override
	public void reset() {
	}

}
