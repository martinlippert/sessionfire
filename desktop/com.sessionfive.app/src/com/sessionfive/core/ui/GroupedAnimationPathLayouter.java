package com.sessionfive.core.ui;

import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Presentation;

public class GroupedAnimationPathLayouter extends AbstractAnimationPathLayouter {

	@Override
	public String getName() {
		return "Into Groups on Demand";
	}

	@Override
	public void layoutAnimationPath(Presentation presentation,
			AnimationStyle animationStyle) {
		new StandardAnimationPathCreator().createAnimationPath(presentation, animationStyle, false);
	}

}
