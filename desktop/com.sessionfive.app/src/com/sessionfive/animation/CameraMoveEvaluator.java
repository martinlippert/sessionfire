package com.sessionfive.animation;

import org.jdesktop.animation.timing.interpolation.Evaluator;

import com.sessionfive.core.Camera;

public class CameraMoveEvaluator extends Evaluator<Camera> {
	
	@Override
    public Camera evaluate(Camera v0, Camera v1, float fraction) {
        float locationX = v0.getLocation().getX() + ((v1.getLocation().getX() - v0.getLocation().getX()) * fraction);
        float locationY = v0.getLocation().getY() + ((v1.getLocation().getY() - v0.getLocation().getY()) * fraction);
        float locationZ = v0.getLocation().getZ() + ((v1.getLocation().getZ() - v0.getLocation().getZ()) * fraction);

        float targetX = v0.getTarget().getX() + ((v1.getTarget().getX() - v0.getTarget().getX()) * fraction);
        float targetY = v0.getTarget().getY() + ((v1.getTarget().getY() - v0.getTarget().getY()) * fraction);
        float targetZ = v0.getTarget().getZ() + ((v1.getTarget().getZ() - v0.getTarget().getZ()) * fraction);

        float upX = v0.getUp().getX() + ((v1.getUp().getX() - v0.getUp().getX()) * fraction);
        float upY = v0.getUp().getY() + ((v1.getUp().getY() - v0.getUp().getY()) * fraction);
        float upZ = v0.getUp().getZ() + ((v1.getUp().getZ() - v0.getUp().getZ()) * fraction);

        return new Camera(locationX, locationY, locationZ, targetX, targetY, targetZ, upX, upY, upZ);
    }
}
