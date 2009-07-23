package com.sessionfive.animation;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class Camera {
    
    private Point location;
    private Point target;
    private Point up;
    
    public Camera() {
        location = new Point(0f, 0f, 0f);
        target = new Point(0f, 0f, 0f);
        up = new Point(0f, 0f, 0f);
    }
    
    public void setup(GL gl, GLU glu) {
        glu.gluLookAt(location.getX(), location.getY(), location.getZ(),
                      target.getX(), target.getY(), target.getZ(),
                      up.getX(), up.getY(), up.getZ());
    }
    
	public void setCameraSetting(CameraSetting cameraSetting) {
		location = cameraSetting.getLocation();
		target = cameraSetting.getTarget();
		up = cameraSetting.getUp();
	}
    
}
