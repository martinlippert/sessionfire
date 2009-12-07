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
		return GoToAnimation.NAME;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GoToAnimationFactory)) return false;
		return toString().equals(obj.toString());
	}

}
