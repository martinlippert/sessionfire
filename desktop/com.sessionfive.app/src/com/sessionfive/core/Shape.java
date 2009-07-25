package com.sessionfive.core;

import javax.media.opengl.GL2;

public interface Shape extends Focusable {
	
	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();
	public float getRotation();
	
	public void display(GL2 gl);

}
