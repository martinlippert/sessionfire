package com.sessionfive.core.test;

import java.util.List;

import junit.framework.TestCase;

import com.sessionfive.animation.GoToAnimationStyle;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ui.LineGroupingLayouter;

public class LineGroupingLayouterTest extends TestCase {
	
	public void testEmptyPresentationAnimationCreation() {
		LineGroupingLayouter layouter = new LineGroupingLayouter();
		Presentation presentation = new Presentation();
		layouter.animate(presentation, new GoToAnimationStyle());
		
		assertEquals(0, presentation.getAnimationStepCount());
	}
	
	public void testFlatShapeHierarchyAnimationCreation() {
		LineGroupingLayouter layouter = new LineGroupingLayouter();
		Presentation presentation = new Presentation();
		Shape shape1 = new ConcreteShape();
		Shape shape2 = new ConcreteShape();
		Shape shape3 = new ConcreteShape();
		presentation.addShape(shape1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape3, LayerType.CAMERA_ANIMATED);
		
		layouter.animate(presentation, new GoToAnimationStyle());
		
		assertEquals(3, presentation.getAnimationStepCount());
		assertSame(presentation, presentation.getAnimationStep(0).getStartShape());
		assertSame(shape1, presentation.getAnimationStep(0).getEndShape());
		assertSame(shape1, presentation.getAnimationStep(1).getStartShape());
		assertSame(shape2, presentation.getAnimationStep(1).getEndShape());
		assertSame(shape2, presentation.getAnimationStep(2).getStartShape());
		assertSame(shape3, presentation.getAnimationStep(2).getEndShape());
	}
	
	public void testDeepShapeHierarchyAnimationCreation() {
		LineGroupingLayouter layouter = new LineGroupingLayouter();
		Presentation presentation = new Presentation();
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
		
		layouter.animate(presentation, new GoToAnimationStyle());
		
		assertEquals(3, presentation.getAnimationStepCount());
		assertSame(presentation, presentation.getAnimationStep(0).getStartShape());
		assertSame(top1, presentation.getAnimationStep(0).getEndShape());
		assertSame(top1, presentation.getAnimationStep(1).getStartShape());
		assertSame(child21, presentation.getAnimationStep(1).getEndShape());
		assertSame(child21, presentation.getAnimationStep(2).getStartShape());
		assertSame(top3, presentation.getAnimationStep(2).getEndShape());
		
		AnimationStep step1 = presentation.getAnimationStep(0);
		List<AnimationStep> zoomSteps1 = step1.getZoomIntoSteps();
		assertEquals(2, zoomSteps1.size());
		assertSame(top1, zoomSteps1.get(0).getStartShape());
		assertSame(child11, zoomSteps1.get(0).getEndShape());
		assertSame(child11, zoomSteps1.get(1).getStartShape());
		assertSame(child12, zoomSteps1.get(1).getEndShape());

		AnimationStep step2 = presentation.getAnimationStep(1);
		List<AnimationStep> zoomSteps2 = step2.getZoomIntoSteps();
		assertEquals(0, zoomSteps2.size());
	
		AnimationStep step3 = presentation.getAnimationStep(2);
		List<AnimationStep> zoomSteps3 = step3.getZoomIntoSteps();
		assertEquals(0, zoomSteps3.size());
	}
	
	protected static class ConcreteShape extends AbstractShape {
	}

}
