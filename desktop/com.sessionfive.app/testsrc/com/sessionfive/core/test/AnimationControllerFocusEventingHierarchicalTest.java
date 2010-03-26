package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.test.StandardAnimationPathCreatorTest.ConcreteShape;

public class AnimationControllerFocusEventingHierarchicalTest extends TestCase {

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
	
	private MockedShapeFocusListener listener;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		presentation = new Presentation();
		display = new Display(presentation);

		controller = new AnimationController();
		controller.init(presentation, display);
		
		listener = new MockedShapeFocusListener();
		controller.addFocusListener(listener);
	}
	
	public void testFocusWithinHierarchyZoomIntoGrouManually() throws Exception {
		createHierarchicalStructure(false);

		listener.reset();
		controller.forward();
		listener.waitForAnimationToFinish(controller);
		controller.zoomIn();
		listener.waitForAnimationToFinish(controller);
		listener.assertStarts(top1, child11);
		listener.assertFinished(top1, child11);
		listener.assertCanceled(new Shape[0]);
		listener.assertShapeLeft(top1);
		listener.assertGroupOfShapeLeft(new Shape[0]);
	}
	
	public void testFocusGroupLeftAtTheEnd() throws Exception {
		createHierarchicalStructure(false);

		controller.forward();
		controller.zoomIn();
		controller.forward();
		listener.waitForAnimationToFinish(controller);
		
		listener.reset();
		controller.forward();
		listener.waitForAnimationToFinish(controller);
		
		assertSame(child21, controller.getLastFocussedShape());

		listener.assertStarts(child21);
		listener.assertFinished(child21);
		listener.assertCanceled(new Shape[0]);
		listener.assertShapeLeft(child12);
		listener.assertGroupOfShapeLeft(child12);
	}
		
	public void testFocusGroupLeftManuallyZoomingOut() throws Exception {
		createHierarchicalStructure(false);

		controller.forward();
		controller.zoomIn();
		controller.forward();
		listener.waitForAnimationToFinish(controller);
		listener.reset();
		controller.zoomOut();
		listener.waitForAnimationToFinish(controller);
		
		assertSame(top1, controller.getLastFocussedShape());

		listener.assertStarts(top1);
		listener.assertFinished(top1);
		listener.assertCanceled(new Shape[0]);
		listener.assertShapeLeft(child12);
		listener.assertGroupOfShapeLeft(child12);
	}
		
	public void testFocusGroupLeftManuallyBySteppingBackward() throws Exception {
		createHierarchicalStructure(false);

		controller.forward();
		controller.zoomIn();
		listener.waitForAnimationToFinish(controller);
		listener.reset();
		controller.backward();
		listener.waitForAnimationToFinish(controller);
		
		assertSame(top1, controller.getLastFocussedShape());

		listener.assertStarts(top1);
		listener.assertFinished(top1);
		listener.assertCanceled(new Shape[0]);
		listener.assertShapeLeft(child11);
		listener.assertGroupOfShapeLeft(child11);
	}
		
	private void createHierarchicalStructure(boolean autoZoomIn) {
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
		
		DefaultTestAnimationStyle style = new DefaultTestAnimationStyle();

		AnimationStep topStep1 = new AnimationStep(top1);
		AnimationStep topStep2 = new AnimationStep(child21);
		AnimationStep topStep3 = new AnimationStep(top3);
		topStep1.setStyle(style);
		topStep2.setStyle(style);
		topStep3.setStyle(style);
		presentation.addAnimationStep(topStep1);
		presentation.addAnimationStep(topStep2);
		presentation.addAnimationStep(topStep3);
		
		AnimationStep childStep1 = new AnimationStep(child11);
		AnimationStep childStep2 = new AnimationStep(child12);
		topStep1.addChild(childStep1);
		topStep1.addChild(childStep2);
		AnimationStep childStep3 = new AnimationStep(child31);
		topStep3.addChild(childStep3);
		
		childStep1.setStyle(style);
		childStep2.setStyle(style);
		childStep3.setStyle(style);
		
		topStep1.setAutoZoomEnabled(autoZoomIn);
		topStep2.setAutoZoomEnabled(autoZoomIn);
		topStep3.setAutoZoomEnabled(autoZoomIn);
		childStep1.setAutoZoomEnabled(autoZoomIn);
		childStep2.setAutoZoomEnabled(autoZoomIn);
		childStep3.setAutoZoomEnabled(autoZoomIn);
	}
	
}
