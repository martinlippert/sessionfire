package com.sessionfive.core.ui;

import com.sessionfive.animation.GoToAnimation;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Shape;

public class GoToAnimationFactory implements AnimationFactory {

	@Override
	public Animation createAnimation(Focusable startShape, Shape endShape) {
		return new GoToAnimation(startShape, endShape);
	}

	@Override
	public String getName() {
		return "Go To";
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
