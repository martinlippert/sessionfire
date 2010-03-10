package com.sessionfive.core.ui;

import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Presentation;

public class LinearAnimationPathLayouter extends AbstractAnimationPathLayouter {

	@Override
	public String getName() {
		return "Straight through";
	}

	@Override
	public void layoutAnimationPath(Presentation presentation, AnimationStyle animationStyle) {
		new StandardAnimationPathCreator().createAnimationPath(presentation, animationStyle, true);
	}

}
