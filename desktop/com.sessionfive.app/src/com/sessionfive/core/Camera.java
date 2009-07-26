package com.sessionfive.core;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import com.sessionfive.animation.Point;

public class Camera {
	
	private Point location;
	private Point target;
	private Point up;
	
	public Camera(float locationX, float locationY, float locationZ,
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

    public void setTo(GL gl, GLU glu) {
        glu.gluLookAt(location.getX(), location.getY(), location.getZ(),
                      target.getX(), target.getY(), target.getZ(),
                      up.getX(), up.getY(), up.getZ());
    }
    
}
