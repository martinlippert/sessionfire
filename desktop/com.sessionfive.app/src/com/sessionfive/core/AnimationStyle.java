package com.sessionfive.core;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;

public interface AnimationStyle {

	public String getName();
	
	public Animator createForwardAnimator(Camera cameraStart, Camera cameraEnd, Display display, Shape endShape);
	public Animator createBackwardAnimator(Camera cameraStart, Camera cameraEnd, Display display, Shape endShape);

}
