package com.sessionfive.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GLContext;

public abstract class AbstractShape implements Shape {

	private float x;
	private float y;
	private float z;
	private float angleX;
	private float angleY;
	private float angleZ;
	private boolean reflectionEnabled;
	
	private float focusScale;
	
	private List<ShapeChangedListener> changeListeners;

	public AbstractShape() {
		super();
		this.reflectionEnabled = true;
		this.focusScale = 1.0f;
		this.changeListeners = new LinkedList<ShapeChangedListener>();
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		fireShapeChangedEvent();
	}

	@Override
	public float getRotationAngleX() {
		return angleX;
	}
	
	@Override
	public float getRotationAngleY() {
		return angleY;
	}
	
	@Override
	public float getRotationAngleZ() {
		return angleZ;
	}

	@Override
	public void setRotation(float angleX, float angleY, float angleZ) {
		this.angleX = angleX;
		this.angleY = angleY;
		this.angleZ = angleZ;
		
		fireShapeChangedEvent();
	}
	
	@Override
	public boolean isReflectionEnabled() {
		return reflectionEnabled;
	}
	
	@Override
	public void setReflectionEnabled(boolean reflectionEnabled) {
		if (this.reflectionEnabled != reflectionEnabled) {
			this.reflectionEnabled = reflectionEnabled;
			fireShapeChangedEvent();
		}
	}
	
	public float getFocusScale() {
		return focusScale;
	}
	
	public void setFocusScale(float focusScale) {
		if (this.focusScale != focusScale) {
			this.focusScale = focusScale;
			fireShapeChangedEvent();
		}
	}
	
	@Override
	public Camera getFocussedCamera() {
		float shapeX = this.getX();
		float shapeY = this.getY();
		float shapeZ = this.getZ();
		
		float shapeWidth = this.getWidth();
		float shapeHeight = this.getHeight();
		
		float rotationAngleX = this.getRotationAngleX();
		float rotationAngleY = this.getRotationAngleY();
		float rotationAngleZ = -this.getRotationAngleZ();
		double rotationRadianX = Math.toRadians(rotationAngleX);
		double rotationRadianY = Math.toRadians(rotationAngleY);
		double rotationRadianZ = Math.toRadians(rotationAngleZ);
		
		float lookAtX = shapeX + (shapeWidth / 2);
		float lookAtY = shapeY + (shapeHeight / 2);
		float lookAtZ = shapeZ;
		
		float locationX = 0;
		float locationY = 0;
		float locationZ = (shapeWidth > shapeHeight ? shapeWidth : shapeHeight) * focusScale;
		
		// location y-axis rotation
		locationX = (float) (locationZ * Math.sin(rotationRadianY));
		locationZ = (float) (locationZ * Math.cos(rotationRadianY));

		// location x-axis rotation
		locationY = (float) - (locationZ * Math.sin(rotationRadianX));
		locationZ = (float) (locationZ * Math.cos(rotationRadianX));
		
		locationX += lookAtX;
		locationY += lookAtY;
		
		double upX = Math.sin(rotationRadianZ);
		double upY = Math.cos(rotationRadianZ);
		double upZ = 0f;
		
		// up y-axis rotation
		double oldX = upX;
		double oldZ = upZ;
		upX = oldX * Math.cos(rotationRadianY) + oldZ * Math.sin(rotationRadianY);
		upZ = oldX * -Math.sin(rotationRadianY) + oldZ * Math.cos(rotationRadianY);

		// up x-axis rotation
		double oldY = upY;
		oldZ = upZ;
		upY = oldY * Math.cos(rotationRadianX) - oldZ * Math.sin(rotationRadianX);
		upZ = oldY * Math.sin(rotationRadianX) + oldZ * Math.cos(rotationRadianX);
		
		Camera cameraSetting = new Camera(locationX, locationY, locationZ,
				lookAtX, lookAtY, lookAtZ, (float)upX, (float)upY, (float)upZ);
		
		return cameraSetting;
	}
	
	@Override
	public void initialize(GLContext context) throws Exception {
	}
	
	@Override
	public void release(GLContext context) throws Exception {
	}
	
	@Override
	public void addShapeChangedListener(ShapeChangedListener listener) {
		changeListeners.add(listener);
	}
	
	@Override
	public void removeShapeChangedListener(ShapeChangedListener listener) {
		changeListeners.remove(listener);
	}
	
	protected void fireShapeChangedEvent() {
		ShapeChangedEvent event = new ShapeChangedEvent(this);
		Iterator<ShapeChangedListener> listeners = changeListeners.iterator();
		while (listeners.hasNext()) {
			listeners.next().shapeChanged(event);
		}
	}

}
