package com.sessionfive.core;

import java.util.List;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import com.sessionfive.core.ui.ExplodeGroupAction;

public interface Shape {
	
	public ShapePosition getPosition();
	public ShapeRotation getRotation();
	public ShapeSize getSize();
	
	public ShapePosition getAbsolutePosition();

	public void setPosition(ShapePosition position);
	public void setRotation(ShapeRotation rotation);
	public void setSize(ShapeSize size);
	
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
	
	public void setZoomAction(ExplodeGroupAction explodeGroupAction);
	public ExplodeGroupAction getZoomAction();

	public void addShapeChangedListener(ShapeChangedListener listener);
	public void removeShapeChangedListener(ShapeChangedListener listener);
	
}
