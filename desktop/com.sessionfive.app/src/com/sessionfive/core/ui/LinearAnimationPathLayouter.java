package com.sessionfive.core.ui;

import java.util.Iterator;

import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class LinearAnimationPathLayouter extends AbstractAnimationPathLayouter {

	@Override
	public String getName() {
		return "Straight through";
	}

	@Override
	public void layoutAnimationPath(Presentation presentation, AnimationStyle animationStyle) {
		presentation.removeAllAnimationSteps();

		Focusable animationStart = presentation;
		Iterator<Shape> iter = presentation.shapeIterator(true);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			AnimationStep step = new AnimationStep(animationStart, shape);
			step.setStyle(animationStyle);

			presentation.addAnimationStep(step);
			animationStart = shape;
		}
	}

}
