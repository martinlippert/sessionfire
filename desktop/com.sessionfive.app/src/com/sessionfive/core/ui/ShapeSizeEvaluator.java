package com.sessionfive.core.ui;

import org.jdesktop.animation.timing.interpolation.Evaluator;

import com.sessionfive.core.ShapeSize;

public class ShapeSizeEvaluator extends Evaluator<ShapeSize> {

	@Override
	public ShapeSize evaluate(ShapeSize start, ShapeSize end, float fraction) {
		float width = start.getWidth()
				+ ((end.getWidth() - start.getWidth()) * fraction);
		float height = start.getHeight()
				+ ((end.getHeight() - start.getHeight()) * fraction);
		float depth = start.getDepth()
				+ ((end.getDepth() - start.getDepth()) * fraction);
		return new ShapeSize(width, height, depth);
	}

}
