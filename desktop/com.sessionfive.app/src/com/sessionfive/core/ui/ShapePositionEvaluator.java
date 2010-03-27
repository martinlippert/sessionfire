package com.sessionfive.core.ui;

import org.jdesktop.animation.timing.interpolation.Evaluator;

import com.sessionfive.core.ShapePosition;

public class ShapePositionEvaluator extends Evaluator<ShapePosition> {

	@Override
	public ShapePosition evaluate(ShapePosition start, ShapePosition end,
			float fraction) {
		float locationX = start.getX()
				+ ((end.getX() - start.getX()) * fraction);
		float locationY = start.getY()
				+ ((end.getY() - start.getY()) * fraction);
		float locationZ = start.getZ()
				+ ((end.getZ() - start.getZ()) * fraction);
		return new ShapePosition(locationX, locationY, locationZ);
	}

}
