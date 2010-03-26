package com.sessionfive.core;

import java.util.List;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

public interface Shape {
	
	public ShapePosition getPosition();
	public ShapePosition getAbsolutePosition();
	public ShapeRotation getRotation();
	public ShapeSize getSize();
	
	public void setPosition(ShapePosition position);
	public void setRotation(ShapeRotation rotation);
	public void setSize(ShapeSize size);
	
	public ShapePosition getFocussedPosition();
	public ShapePosition getAbsoluteFocussedPosition();
	public ShapeRotation getFocussedRotation();
	public ShapeSize getFocussedSize();
	
	public void setFocussedPosition(ShapePosition position);
	public void setFocussedRotation(ShapeRotation rotation);
	public void setFocussedSize(ShapeSize size);

	public ShapePosition getCollapsedPosition();
	public ShapeRotation getCollapsedRotation();
	public ShapeSize getCollapsedSize();
	
	public void setCollapsedPosition(ShapePosition position);
	public void setCollapsedRotation(ShapeRotation rotation);
	public void setCollapsedSize(ShapeSize size);

	public boolean isReflectionEnabled();
	public void setReflectionEnabled(boolean reflectionEnabled);
	
	public float getFocusScale();
	public void setFocusScale(float focusScale);
	
	public void initialize(GLContext context) throws Exception;
	public void release(GLContext context) throws Exception;
	
	public void display(GLAutoDrawable drawable);
	public Camera getFocussedCamera();

	public List<Shape> getShapes();
	public void addShape(Shape shape);
	public void removeShape(Shape shape);
	public Shape getOwner();
	public void setOwner(Shape owner);
	
	public void addShapeChangedListener(ShapeChangedListener listener);
	public void removeShapeChangedListener(ShapeChangedListener listener);
	
}
