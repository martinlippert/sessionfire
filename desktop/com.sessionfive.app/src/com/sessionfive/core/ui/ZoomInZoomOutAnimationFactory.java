package com.sessionfive.core.ui;

import com.sessionfive.animation.ZoomOutZoomInAnimation;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Shape;

public class ZoomInZoomOutAnimationFactory implements AnimationFactory {

	@Override
	public Animation createAnimation(Focusable startShape, Shape endShape) {
		return new ZoomOutZoomInAnimation(startShape, endShape);
	}

	@Override
	public String getName() {
		return "Zoom Out - Zoom In";
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
