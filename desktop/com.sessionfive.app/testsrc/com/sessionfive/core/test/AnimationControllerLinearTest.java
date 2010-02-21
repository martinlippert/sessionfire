package com.sessionfive.core.test;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

import junit.framework.TestCase;

public class AnimationControllerLinearTest extends TestCase {
	
	private Presentation presentation;
	private Display display;
	private AnimationController controller;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		presentation = new Presentation();
		display = new Display(presentation);

		controller = new AnimationController();
		controller.init(presentation, display);
	}
	
	public void testAnimationControllerWithEmptyPresentation() {
		assertEquals(0, controller.getNumberOfKeyFrames());
		assertFalse(controller.canGoForward());
		assertFalse(controller.canGoBackward());
		assertNull(controller.getLastFocussedShape());
	}
	
	public void testAnimationControllerWithLinearPresentation() {
		Shape shape1 = new AbstractShape();
		Shape shape2 = new AbstractShape();
		Shape shape3 = new AbstractShape();
		presentation.addShape(shape1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape3, LayerType.CAMERA_ANIMATED);
		
		AnimationStep step1 = new AnimationStep(presentation, shape1);
		AnimationStep step2 = new AnimationStep(shape1, shape2);
		AnimationStep step3 = new AnimationStep(shape2, shape3);
		presentation.addAnimationStep(step1);
		presentation.addAnimationStep(step2);
		presentation.addAnimationStep(step3);
		
		assertTrue(controller.canGoForward());
		assertFalse(controller.canGoBackward());
		assertNull(controller.getLastFocussedShape());
		
		controller.forward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertEquals(shape1, controller.getLastFocussedShape());
		
		controller.forward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertEquals(shape2, controller.getLastFocussedShape());

		controller.forward();
		assertFalse(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertEquals(shape3, controller.getLastFocussedShape());
		
		controller.backward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertEquals(shape2, controller.getLastFocussedShape());

		controller.backward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertEquals(shape1, controller.getLastFocussedShape());

		controller.backward();
		assertTrue(controller.canGoForward());
		assertFalse(controller.canGoBackward());
		assertNull(controller.getLastFocussedShape());
		
		controller.forward();
		controller.backward();
		assertTrue(controller.canGoForward());
		assertFalse(controller.canGoBackward());
		assertNull(controller.getLastFocussedShape());
	}
	
}
