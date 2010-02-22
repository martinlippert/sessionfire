package com.sessionfive.core.test;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.test.LineGroupingLayouterTest.ConcreteShape;

import junit.framework.TestCase;

public class AnimationControllerKeyframeTest extends TestCase {
	
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
	
	public void testKeyframesWithLinearAnimationSteps() {
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
		
		assertEquals(3, controller.getNumberOfKeyFrames());
		controller.goToKeyframeNo(0);
		assertSame(shape1, controller.getLastFocussedShape());
		controller.goToKeyframeNo(1);
		assertSame(shape2, controller.getLastFocussedShape());
		controller.goToKeyframeNo(2);
		assertSame(shape3, controller.getLastFocussedShape());
	}
	
	public void testKeyframesWithHierarchicalSteps() {
		Shape top1 = new ConcreteShape();
		Shape top2 = new AbstractShape();
		Shape top3 = new ConcreteShape();
		presentation.addShape(top1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(top2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(top3, LayerType.CAMERA_ANIMATED);
		
		Shape child11 = new ConcreteShape();
		Shape child12 = new ConcreteShape();
		Shape child21 = new ConcreteShape();
		
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
		
		assertEquals(5, controller.getNumberOfKeyFrames());
		controller.goToKeyframeNo(0);
		assertSame(top1, controller.getLastFocussedShape());
		controller.goToKeyframeNo(1);
		assertSame(child11, controller.getLastFocussedShape());
		controller.goToKeyframeNo(2);
		assertSame(child12, controller.getLastFocussedShape());
		controller.goToKeyframeNo(3);
		assertSame(child21, controller.getLastFocussedShape());
		controller.goToKeyframeNo(4);
		assertSame(top3, controller.getLastFocussedShape());
	}

}
