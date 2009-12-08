package com.sessionfive.core.ui;

import com.sessionfive.animation.MoveToAnimation;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Shape;

public class MoveToAnimationFactory implements AnimationFactory {

	@Override
	public Animation createAnimation(Focusable startShape, Shape endShape) {
		return new MoveToAnimation(startShape, endShape);
	}

	@Override
	public String getName() {
		return MoveToAnimation.NAME;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MoveToAnimationFactory)) return false;
		return toString().equals(obj.toString());
	}

}
