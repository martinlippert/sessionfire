package com.sessionfive.core.test;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Shape;

public class DefaultTestAnimationStyle implements AnimationStyle {

	@Override
	public Animator createBackwardAnimator(Camera cameraStart,
			Camera cameraEnd, Display display, Shape endShape) {
		return new Animator(1);
	}

	@Override
	public Animator createForwardAnimator(Camera cameraStart, Camera cameraEnd,
			Display display, Shape endShape) {
		return new Animator(1);
	}

	@Override
	public String getName() {
		return "Test";
	}

}
