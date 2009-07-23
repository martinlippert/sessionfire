package com.sessionfive.animation;

public class CameraSetting {
	
	private float locationX, locationY, locationZ;
	private float targetX, targetY, targetZ;
	private float upX, upY, upZ;
	
	public CameraSetting(float locationX, float locationY, float locationZ,
			float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
		super();
		this.locationX = locationX;
		this.locationY = locationY;
		this.locationZ = locationZ;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
		this.upX = upX;
		this.upY = upY;
		this.upZ = upZ;
	}

	public float getLocationX() {
		return locationX;
	}

	public float getLocationY() {
		return locationY;
	}

	public float getLocationZ() {
		return locationZ;
	}

	public float getTargetX() {
		return targetX;
	}

	public float getTargetY() {
		return targetY;
	}

	public float getTargetZ() {
		return targetZ;
	}

	public float getUpX() {
		return upX;
	}
	
	public float getUpY() {
		return upY;
	}
	
	public float getUpZ() {
		return upZ;
	}

	public Point getLocation() {
		return new Point(locationX, locationY, locationZ);
	}

	public Point getTarget() {
		return new Point(targetX, targetY, targetZ);
	}

	public Point getUp() {
		return new Point(upX, upY, upZ);
	}

}
