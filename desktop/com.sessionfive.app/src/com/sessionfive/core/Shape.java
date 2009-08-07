package com.sessionfive.core;

import javax.media.opengl.GL2;

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
	
	public void display(GL2 gl);

}
