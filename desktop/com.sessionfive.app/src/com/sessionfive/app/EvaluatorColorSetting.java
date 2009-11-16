package com.sessionfive.app;

import java.awt.Color;

import org.jdesktop.animation.timing.interpolation.Evaluator;

public class EvaluatorColorSetting extends Evaluator<Color> {

	public Color evaluate(Color c1, Color c2, float fraction) {
		float r = ((float)c1.getRed()) / 255;
		float g = (float)c1.getGreen() / 255;
		float b = (float)c1.getBlue() / 255;
		//System.out.println("RGBA: " + r + ", " + g + ", " + b + ", " + fraction);
		return new Color(r, g, b, fraction);
	}
}
