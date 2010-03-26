package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class AnimationControllerFocusEventingTest extends TestCase {

	private Presentation presentation;
	private Display display;
	private AnimationController controller;
	private AbstractShape shape1;
	private AbstractShape shape2;
	private AbstractShape shape3;
	private AnimationStep step1;
	private AnimationStep step2;
	private AnimationStep step3;
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
	
	public void testAnimationControllerEventsLinear() throws Exception {
		createLinearStructure();
		
		listener.reset();
		controller.forward();
		listener.waitForAnimationToFinish(controller);

		listener.assertStarts(shape1);
		listener.assertFinished(shape1);
		listener.assertGroupOfShapeLeft(new Shape[0]);
		listener.assertCanceled(new Shape[0]);
		
		listener.reset();
		controller.forward();
		listener.waitForAnimationToFinish(controller);
		controller.forward();
		listener.waitForAnimationToFinish(controller);

		listener.assertShapeLeft(shape1, shape2);
		listener.assertStarts(shape2, shape3);
		listener.assertFinished(shape2, shape3);
		listener.assertGroupOfShapeLeft(new Shape[0]);
		listener.assertCanceled(new Shape[0]);
		
		listener.reset();
		controller.backward();
		listener.waitForAnimationToFinish(controller);

		listener.assertShapeLeft(shape3);
		listener.assertStarts(shape2);
		listener.assertFinished(shape2);
		listener.assertGroupOfShapeLeft(new Shape[0]);
		listener.assertCanceled(new Shape[0]);
		
		listener.reset();
		controller.backward();
		listener.waitForAnimationToFinish(controller);
		controller.backward();
		listener.waitForAnimationToFinish(controller);
		
		assertNull(controller.getLastFocussedShape());
		listener.assertShapeLeft(shape2, shape1);
		listener.assertStarts(shape1);
		listener.assertFinished(shape1);
		listener.assertGroupOfShapeLeft(new Shape[0]);
		listener.assertCanceled(new Shape[0]);
	}
	
	public void testAnimationControllerCanceledEventWithLinearStructure() throws Exception {
		createLinearStructure();
		
		listener.reset();
		controller.forward();
		controller.forward();
		listener.waitForAnimationToFinish(controller);

		listener.assertStarts(shape1, shape2);
		listener.assertFinished(shape2);
		listener.assertCanceled(shape1);
		listener.assertGroupOfShapeLeft(new Shape[0]);
		listener.assertShapeLeft(new Shape[0]);
	}

	
	private void createLinearStructure() {
		shape1 = new AbstractShape();
		shape2 = new AbstractShape();
		shape3 = new AbstractShape();
		presentation.addShape(shape1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape3, LayerType.CAMERA_ANIMATED);
		
		step1 = new AnimationStep(shape1);
		step1.setStyle(new DefaultTestAnimationStyle());
		step2 = new AnimationStep(shape2);
		step2.setStyle(new DefaultTestAnimationStyle());
		step3 = new AnimationStep(shape3);
		step3.setStyle(new DefaultTestAnimationStyle());
		presentation.addAnimationStep(step1);
		presentation.addAnimationStep(step2);
		presentation.addAnimationStep(step3);
		
		presentation.setDefaultAnimationStyle(new DefaultTestAnimationStyle());
	}

}
