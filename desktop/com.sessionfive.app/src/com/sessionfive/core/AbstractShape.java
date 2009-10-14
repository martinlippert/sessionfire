package com.sessionfive.core;

public abstract class AbstractShape implements Shape {

	private float x;
	private float y;
	private float z;
	private float angleX;
	private float angleY;
	private float angleZ;

	public AbstractShape() {
		super();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getRotationAngleX() {
		return angleX;
	}
	
	public float getRotationAngleY() {
		return angleY;
	}
	
	public float getRotationAngleZ() {
		return angleZ;
	}

	public void setRotation(float angleX, float angleY, float angleZ) {
		this.angleX = angleX;
		this.angleY = angleY;
		this.angleZ = angleZ;
	}
	
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
		float locationZ = shapeWidth > shapeHeight ? shapeWidth : shapeHeight;
		
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

}
