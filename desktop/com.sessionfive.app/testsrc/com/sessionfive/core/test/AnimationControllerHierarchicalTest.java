package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.test.GroupedAnimationPathLayouterTest.ConcreteShape;

public class AnimationControllerHierarchicalTest extends TestCase {

	private Presentation presentation;
	private Display display;
	private AnimationController controller;
	private Shape top1;
	private Shape top2;
	private Shape top3;
	private Shape child21;
	private Shape child12;
	private Shape child11;
	private ConcreteShape child31;

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
		assertTrue(controller.canZoomIn());
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
		
		controller.forward();
		controller.forward();
		controller.zoomIn();
		assertSame(child31, controller.getLastFocussedShape());
		assertFalse(controller.canZoomIn());
		assertTrue(controller.canZoomOut());
		assertFalse(controller.canGoForward());
		assertTrue(controller.canGoBackward());
	}
	
	public void testWalkBackOutOfGroupWithoutZommingOut() {
		createHierarchicalStructure();
		
		controller.forward();
		controller.zoomIn();
		
		assertSame(child11, controller.getLastFocussedShape());
		controller.forward();
		assertSame(child12, controller.getLastFocussedShape());
		controller.backward();
		assertSame(child11, controller.getLastFocussedShape());
		controller.backward();
		assertSame(top1, controller.getLastFocussedShape());
	}
	
	public void testWalkBackOutOfGroupWithZommingOut() {
		createHierarchicalStructure();
		
		controller.forward();
		controller.zoomIn();
		
		assertSame(child11, controller.getLastFocussedShape());
		controller.forward();
		assertSame(child12, controller.getLastFocussedShape());
		controller.zoomOut();
		assertSame(top1, controller.getLastFocussedShape());
		
		controller.zoomIn();
		assertSame(child11, controller.getLastFocussedShape());
		controller.zoomOut();
		assertSame(top1, controller.getLastFocussedShape());
	}
	
	public void testWalkForwardThroughAutomaticallyZoomingIn() {
		createHierarchicalStructure();
		
		assertFalse(controller.isAutoZoomIn());
		controller.setAutoZoomIn(true);
		assertTrue(controller.isAutoZoomIn());
		
		controller.forward();
		assertSame(top1, controller.getLastFocussedShape());
		
		controller.forward();
		assertSame(child11, controller.getLastFocussedShape());
		
		controller.forward();
		assertSame(child12, controller.getLastFocussedShape());
		
		controller.forward();
		assertSame(child21, controller.getLastFocussedShape());
		
		controller.forward();
		assertSame(top3, controller.getLastFocussedShape());

		controller.forward();
		assertSame(child31, controller.getLastFocussedShape());
	}
	
	public void testWalkBackThroughHierarchyAutomaticallyZoomingIn() {
		createHierarchicalStructure();

		controller.setAutoZoomIn(true);
		controller.readjustSmoothlyTo(child31);
		
		assertSame(child31, controller.getLastFocussedShape());
		assertFalse(controller.canZoomIn());
		assertFalse(controller.canGoForward());
		assertTrue(controller.canGoBackward());
		assertTrue(controller.canZoomOut());
		
		controller.backward();
		assertSame(top3, controller.getLastFocussedShape());
		
		controller.backward();
		assertSame(child21, controller.getLastFocussedShape());
		
		controller.backward();
		assertSame(child12, controller.getLastFocussedShape());
		
		controller.backward();
		assertSame(child11, controller.getLastFocussedShape());
		
		controller.backward();
		assertSame(top1, controller.getLastFocussedShape());
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
		child31 = new ConcreteShape();
		
		top1.addShape(child11);
		top1.addShape(child12);
		top2.addShape(child21);
		top3.addShape(child31);
		
		AnimationStep topStep1 = new AnimationStep(presentation, top1);
		AnimationStep topStep2 = new AnimationStep(top1, child21);
		AnimationStep topStep3 = new AnimationStep(child21, top3);
		presentation.addAnimationStep(topStep1);
		presentation.addAnimationStep(topStep2);
		presentation.addAnimationStep(topStep3);
		
		AnimationStep childStep1 = new AnimationStep(top1, child11);
		AnimationStep childStep2 = new AnimationStep(child11, child12);
		topStep1.addChild(childStep1);
		topStep1.addChild(childStep2);
		AnimationStep childStep3 = new AnimationStep(top3, child31);
		topStep3.addChild(childStep3);
	}
	
}
