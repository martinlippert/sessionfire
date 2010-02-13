package com.sessionfive.core.ui;

import java.util.Iterator;

import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public abstract class AbstractLinearLayouter implements Layouter {

	@Override
	public void animate(Presentation presentation,
			AnimationFactory animationFactory) {
		
		presentation.removeAllAnimations();
		
		Focusable animationStart = presentation;
		Iterator<Shape> iter = presentation.shapeIterator(true);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			Animation animation = animationFactory.createAnimation(
					animationStart, shape);
			presentation.addAnimation(animation);
			animationStart = shape;
		}
	}

}
