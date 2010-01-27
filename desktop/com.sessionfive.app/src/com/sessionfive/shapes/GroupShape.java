package com.sessionfive.shapes;

import java.util.Arrays;
import java.util.List;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Shape;

public class GroupShape extends AbstractShape {

	private List<Shape> shapes;
	private final int groupnr;

	public GroupShape(int groupnr, Shape... shapes) {
		this.groupnr = groupnr;
		this.shapes = Arrays.asList(shapes);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		for (Shape shape : shapes) {
			shape.display(drawable);
		}
	}

	@Override
	public void initialize(GLContext context) throws Exception {
		for (Shape shape : shapes) {
			shape.initialize(context);
		}
	}

	@Override
	public void release(GLContext context) throws Exception {
		for (Shape shape : shapes) {
			shape.release(context);
		}
	}

	@Override
	public void setFocusScale(float focusScale) {
		super.setFocusScale(focusScale / 10);
	}

	@Override
	public float getHeight() {
		return shapes.get(0).getHeight();
	}

	@Override
	public float getWidth() {
		return shapes.get(0).getWidth();
	}

	@Override
	public void setRotation(float angleX, float angleY, float angleZ) {
		for (Shape shape : shapes) {
			shape.setRotation(angleX, angleY, angleZ);
		}
	}

	public void setPosition(float x, float y, float z, float space) {
		for (Shape shape : shapes) {
			shape.setPosition(x, y, z -= space);
		}
		setPosition(x, y, z);
	}

	@Override
	public String toString() {
		return "group:" + groupnr + " h:" + getHeight() + " w:" + getWidth() + " x:" + getX()
				+ " (" + shapes + ")";
	}

	public Shape getShape(int i) {
		return shapes.get(i);
	}

}
