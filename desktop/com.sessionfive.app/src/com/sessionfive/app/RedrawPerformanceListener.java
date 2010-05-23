package com.sessionfive.app;

import org.jdesktop.animation.timing.TimingTarget;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFocusListener;

public class RedrawPerformanceListener implements ShapeFocusListener {
	
	private final Display display;
	private long endTicks;
	private long startTicks;

	public RedrawPerformanceListener(Display display) {
		this.display = display;
	}

	@Override
	public TimingTarget[] startsFocussing(Shape shape) {
		startTicks = display.getTicks();
		return null;
	}

	@Override
	public TimingTarget[] finishedFocussing(Shape shape) {
		endTicks = display.getTicks();
		System.out.println("redraw ticks: " + (endTicks - startTicks));
		return null;
	}

	@Override
	public TimingTarget[] canceledFocussing(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget[] shapeLeft(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget[] groupOfShapeLeft(Shape shape) {
		return null;
	}

}
