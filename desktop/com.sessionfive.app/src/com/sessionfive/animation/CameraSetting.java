package com.sessionfive.animation;

public class CameraSetting {
	
	private Point location;
	private Point target;
	private Point up;
	
	public CameraSetting(float locationX, float locationY, float locationZ,
			float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
		super();
		this.location = new Point(locationX, locationY, locationZ);
		this.target = new Point(targetX, targetY, targetZ);
		this.up = new Point(upX, upY, upZ);
	}

	public Point getLocation() {
		return location;
	}

	public Point getTarget() {
		return target;
	}

	public Point getUp() {
		return up;
	}

}
