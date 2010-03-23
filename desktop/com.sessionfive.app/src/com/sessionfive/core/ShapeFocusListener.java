package com.sessionfive.core;

import org.jdesktop.animation.timing.TimingTarget;

public interface ShapeFocusListener {
	
	public TimingTarget startsFocussing(Shape shape);
	public TimingTarget finishedFocussing(Shape shape);
	public TimingTarget cancelFocussing(Shape shape);

}
