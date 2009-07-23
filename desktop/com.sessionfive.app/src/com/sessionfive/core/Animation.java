package com.sessionfive.core;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;

public interface Animation {

	public Animator getForwardAnimation(Display display);
	public Animator getBackwardAnimation(Display display);
	public void directlyGoTo(Display display);

}
