package com.sessionfive.core.ui;

import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Presentation;

public interface AnimationPathLayouter {

	public String getName();
	public void layoutAnimationPath(Presentation presentation, AnimationStyle style);

}
