package com.sessionfive.core.ui;

import org.jdesktop.animation.timing.TimingTarget;

import com.sessionfive.core.Shape;

public class ShapeTimingTarget implements TimingTarget {
	
	private Shape shape;
	private final TimingTarget[] shapeTargets;

	public ShapeTimingTarget(Shape shape, TimingTarget[] shapeTargets) {
		this.shape = shape;
		this.shapeTargets = shapeTargets;
	}
	
	public Shape getShape() {
		return shape;
	}

	@Override
	public void timingEvent(float fraction) {
		for (TimingTarget target : this.shapeTargets) {
			target.timingEvent(fraction);
		}
	}

	@Override
	public void begin() {
		for (TimingTarget target : this.shapeTargets) {
			target.begin();
		}
	}

	@Override
	public void end() {
		for (TimingTarget target : this.shapeTargets) {
			target.end();
		}
	}

	@Override
	public void repeat() {
		for (TimingTarget target : this.shapeTargets) {
			target.repeat();
		}
	}

}
