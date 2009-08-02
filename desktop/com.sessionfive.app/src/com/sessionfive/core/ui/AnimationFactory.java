package com.sessionfive.core.ui;

import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Shape;

public interface AnimationFactory {
	
	public String getName();
	public Animation createAnimation(Focusable startShape, Shape endShape);

}
