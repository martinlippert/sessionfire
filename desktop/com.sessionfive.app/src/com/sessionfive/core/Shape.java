package com.sessionfive.core;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

public interface Shape extends Focusable {
	
	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();

	public float getRotationAngleX();
	public float getRotationAngleY();
	public float getRotationAngleZ();
	
	public void setPosition(float x, float f, float z);
	public void setRotation(float angleX, float angleY, float angleZ);
	
	public void initialize(GLContext context) throws Exception;
	public void release(GLContext context) throws Exception;

	public void display(GL gl);
	
	public void addShapeChangedListener(ShapeChangedListener listener);
	public void removeShapeChangedListener(ShapeChangedListener listener);

}
