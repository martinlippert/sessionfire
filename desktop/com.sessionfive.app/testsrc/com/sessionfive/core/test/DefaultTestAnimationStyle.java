package com.sessionfive.core.test;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Shape;

public class DefaultTestAnimationStyle implements AnimationStyle {
	
	private int millis;

	public DefaultTestAnimationStyle() {
		this(1);
	}
	
	public DefaultTestAnimationStyle(int millis) {
		this.millis = millis;
	}

	@Override
	public Animator createBackwardAnimator(Camera cameraStart,
			Camera cameraEnd, Display display, Shape endShape) {
		return new Animator(millis);
	}

	@Override
	public Animator createForwardAnimator(Camera cameraStart, Camera cameraEnd,
			Display display, Shape endShape) {
		return new Animator(millis);
	}

	@Override
	public String getName() {
		return "Test";
	}

}
