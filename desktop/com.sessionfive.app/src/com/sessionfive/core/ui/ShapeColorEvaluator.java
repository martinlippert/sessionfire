package com.sessionfive.core.ui;

import org.jdesktop.animation.timing.interpolation.Evaluator;

import com.sessionfive.core.ShapeColor;

public class ShapeColorEvaluator extends Evaluator<ShapeColor> {

	@Override
	public ShapeColor evaluate(ShapeColor start, ShapeColor end, float fraction) {
		float r = start.getR() + ((end.getR() - start.getR()) * fraction);
		float g = start.getG() + ((end.getG() - start.getG()) * fraction);
		float b = start.getB() + ((end.getB() - start.getB()) * fraction);
		float a = start.getA() + ((end.getA() - start.getA()) * fraction);
		return new ShapeColor(r, g, b, a);
	}

}
