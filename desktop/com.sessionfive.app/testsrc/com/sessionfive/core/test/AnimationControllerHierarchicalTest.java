package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.test.LineGroupingLayouterTest.ConcreteShape;

public class AnimationControllerHierarchicalTest extends TestCase {

	private Presentation presentation;
	private Display display;
	private AnimationController controller;
	private ConcreteShape top1;
	private AbstractShape top2;
	private ConcreteShape top3;
	private ConcreteShape child21;
	private ConcreteShape child12;
	private ConcreteShape child11;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		presentation = new Presentation();
		display = new Display(presentation);

		controller = new AnimationController();
		controller.init(presentation, display);
	}
	
	public void testWalkThroughHierarchyWithoutZoomingIn() {
		createHierarchicalStructure();
		
		assertTrue(controller.canGoForward());
		assertFalse(controller.canGoBackward());
		assertFalse(controller.canZoomIn());
		assertNull(controller.getLastFocussedShape());
		
		controller.forward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertTrue(controller.canZoomIn());
		assertSame(top1, controller.getLastFocussedShape());
		
		controller.forward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertFalse(controller.canZoomIn());
		assertSame(child21, controller.getLastFocussedShape());

		controller.forward();
		assertFalse(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertFalse(controller.canZoomIn());
		assertSame(top3, controller.getLastFocussedShape());
	}

	public void testWalkThroughHierarchyWithZoomingIn() {
		createHierarchicalStructure();
		
		controller.forward();
		controller.zoomIn();
		
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertFalse(controller.canZoomIn());
		assertSame(child11, controller.getLastFocussedShape());
		
		controller.forward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertFalse(controller.canZoomIn());
		assertSame(child12, controller.getLastFocussedShape());

		controller.forward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertFalse(controller.canZoomIn());
		assertSame(child21, controller.getLastFocussedShape());
		
		controller.backward();
		assertTrue(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertSame(top1, controller.getLastFocussedShape());
		assertTrue(controller.canZoomIn());
	}
	
	private void createHierarchicalStructure() {
		top1 = new ConcreteShape();
		top2 = new AbstractShape();
		top3 = new ConcreteShape();
		presentation.addShape(top1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(top2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(top3, LayerType.CAMERA_ANIMATED);
		
		child11 = new ConcreteShape();
		child12 = new ConcreteShape();
		child21 = new ConcreteShape();
		
		top1.addShape(child11);
		top1.addShape(child12);
		top2.addShape(child21);
		
		AnimationStep topStep1 = new AnimationStep(presentation, top1);
		AnimationStep topStep2 = new AnimationStep(top1, child21);
		AnimationStep topStep3 = new AnimationStep(child21, top3);
		presentation.addAnimationStep(topStep1);
		presentation.addAnimationStep(topStep2);
		presentation.addAnimationStep(topStep3);
		
		AnimationStep childStep1 = new AnimationStep(top1, child11);
		AnimationStep childStep2 = new AnimationStep(child11, child12);
		topStep1.addAnimationStep(childStep1);
		topStep1.addAnimationStep(childStep2);
	}
	
}
