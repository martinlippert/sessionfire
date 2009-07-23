package com.sessionfive.animation;

import org.jdesktop.animation.timing.interpolation.Evaluator;

public class EvaluatorCameraSetting extends Evaluator<CameraSetting> {
	
    public CameraSetting evaluate(CameraSetting v0, CameraSetting v1, float fraction) {
        float locationX = v0.getLocationX() + ((v1.getLocationX() - v0.getLocationX()) * fraction);
        float locationY = v0.getLocationY() + ((v1.getLocationY() - v0.getLocationY()) * fraction);
        float locationZ = v0.getLocationZ() + ((v1.getLocationZ() - v0.getLocationZ()) * fraction);

        float targetX = v0.getTargetX() + ((v1.getTargetX() - v0.getTargetX()) * fraction);
        float targetY = v0.getTargetY() + ((v1.getTargetY() - v0.getTargetY()) * fraction);
        float targetZ = v0.getTargetZ() + ((v1.getTargetZ() - v0.getTargetZ()) * fraction);

        float upX = v0.getUpX() + ((v1.getUpX() - v0.getUpX()) * fraction);
        float upY = v0.getUpY() + ((v1.getUpY() - v0.getUpY()) * fraction);
        float upZ = v0.getUpZ() + ((v1.getUpZ() - v0.getUpZ()) * fraction);

        return new CameraSetting(locationX, locationY, locationZ, targetX, targetY, targetZ, upX, upY, upZ);
    }
}
