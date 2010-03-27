package com.sessionfive.core.ui;

import org.jdesktop.animation.timing.interpolation.Evaluator;

import com.sessionfive.core.ShapeRotation;

public class ShapeRotationEvaluator extends Evaluator<ShapeRotation> {

	@Override
	public ShapeRotation evaluate(ShapeRotation start, ShapeRotation end,
			float fraction) {
		float rotationX = start.getRotationAngleX()
				+ ((end.getRotationAngleX() - start.getRotationAngleX()) * fraction);
		float rotationY = start.getRotationAngleY()
				+ ((end.getRotationAngleY() - start.getRotationAngleY()) * fraction);
		float rotationZ = start.getRotationAngleZ()
				+ ((end.getRotationAngleZ() - start.getRotationAngleZ()) * fraction);
		return new ShapeRotation(rotationX, rotationY, rotationZ);
	}

}
