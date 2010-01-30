package com.sessionfive.core;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

public interface Shape extends Focusable {
	
	public float getX();
	public float getY();
	public float getZ();
	public float getWidth();
	public float getHeight();
	public float getDepth();

	public float getRotationAngleX();
	public float getRotationAngleY();
	public float getRotationAngleZ();
	
	public void setPosition(float x, float y, float z);
	public void setSize(float width, float height, float depth);
	public void setRotation(float angleX, float angleY, float angleZ);
	
	public boolean isReflectionEnabled();
	public void setReflectionEnabled(boolean reflectionEnabled);
	
	public float getFocusScale();
	public void setFocusScale(float focusScale);
	
	public void initialize(GLContext context) throws Exception;
	public void release(GLContext context) throws Exception;
	
	public void display(GLAutoDrawable drawable);

	public void addShapeChangedListener(ShapeChangedListener listener);
	public void removeShapeChangedListener(ShapeChangedListener listener);

}
