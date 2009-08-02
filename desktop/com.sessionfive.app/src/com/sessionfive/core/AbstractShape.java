package com.sessionfive.core;

public abstract class AbstractShape implements Shape {

	private float x;
	private float y;
	private float z;
	private float rotation;

	public AbstractShape(float x, float y, float z, float rotation) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
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
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public Camera getFocussedCamera() {
		float shapeX = this.getX();
		float shapeY = this.getY();
		
		float shapeWidth = this.getWidth();
		float shapeHeight = this.getHeight();
		
		float rotationAngle = -this.getRotation();
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
