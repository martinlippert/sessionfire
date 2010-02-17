package com.sessionfive.core.test;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.Presentation;

import junit.framework.TestCase;

public class AnimationControllerTest extends TestCase {
	
	public void testSimpleAnimationController() {
		Presentation presentation = new Presentation();
		Display display = new Display(presentation);

		AnimationController controller = new AnimationController();
		controller.init(presentation, display);
		
		assertEquals(0, controller.getNumberOfKeyFrames());
	}

}
