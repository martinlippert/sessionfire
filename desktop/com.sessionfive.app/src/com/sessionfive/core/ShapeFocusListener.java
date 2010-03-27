package com.sessionfive.core;

import org.jdesktop.animation.timing.TimingTarget;

public interface ShapeFocusListener {

	public TimingTarget[] startsFocussing(Shape shape);
	public TimingTarget[] finishedFocussing(Shape shape);
	public TimingTarget[] canceledFocussing(Shape shape);
	public TimingTarget[] shapeLeft(Shape shape);
	public TimingTarget[] groupOfShapeLeft(Shape shape);

}
