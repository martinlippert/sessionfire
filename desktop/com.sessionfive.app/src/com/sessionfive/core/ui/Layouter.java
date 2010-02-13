package com.sessionfive.core.ui;

import com.sessionfive.core.Presentation;

public interface Layouter {
	
	public String getName();
	public void layout(Presentation presentation);
	public void animate(Presentation presentation, AnimationFactory animationFactory);

}
