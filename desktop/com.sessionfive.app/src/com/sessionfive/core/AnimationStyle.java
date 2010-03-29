package com.sessionfive.core;

import com.sessionfive.app.Display;

public interface AnimationStyle {

	public String getName();
	
	public CameraAnimator createForwardAnimator(Camera cameraStart, Camera cameraEnd, Display display, Shape endShape);
	public CameraAnimator createBackwardAnimator(Camera cameraStart, Camera cameraEnd, Display display, Shape endShape);

}
