package com.sessionfive.core;

public abstract class AbstractShape implements Shape {

	private float x;
	private float y;
	private float z;
	private float angleX;
	private float angleY;
	private float angleZ;

	public AbstractShape(float x, float y, float z, float angleX, float angleY, float angleZ) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.angleX = angleX;
		this.angleY = angleY;
		this.angleZ = angleZ;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	}
	
	@Override
	public Camera getFocussedCamera() {
		float shapeX = this.getX();
		float shapeY = this.getY();
		
		float shapeWidth = this.getWidth();
		float shapeHeight = this.getHeight();
		
		float rotationAngle = -this.getRotationAngleZ();
		double rotationRadian = Math.toRadians(rotationAngle);
		
		double upX = Math.sin(rotationRadian);
		double upY = Math.cos(rotationRadian);
		double upZ = 0f;

		float cameraX = shapeX + (shapeWidth / 2);
		float cameraY = shapeY + (shapeHeight / 2);
		float cameraZ = shapeWidth > shapeHeight ? shapeWidth : shapeHeight;
		
		Camera cameraSetting = new Camera(cameraX, cameraY, cameraZ,
				cameraX, cameraY, 0, (float)upX, (float)upY, (float)upZ);
		
		return cameraSetting;
	}

}
