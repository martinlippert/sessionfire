package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.animation.GoToAnimationStyle;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ui.StandardAnimationPathCreator;

public class StandardAnimationPathCreatorTest extends TestCase {
	
	public void testEmptyPresentationAnimationCreation() {
		StandardAnimationPathCreator layouter = new StandardAnimationPathCreator();
		Presentation presentation = new Presentation();
		layouter.createAnimationPath(presentation, new GoToAnimationStyle(), true);
		
		assertEquals(0, presentation.getTotalAnimationStepCount());
	}
	
	public void testFlatShapeHierarchyAnimationCreation() {
		StandardAnimationPathCreator layouter = new StandardAnimationPathCreator();
		Presentation presentation = new Presentation();
		Shape shape1 = new ConcreteShape();
		Shape shape2 = new ConcreteShape();
		Shape shape3 = new ConcreteShape();
		presentation.addShape(shape1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape3, LayerType.CAMERA_ANIMATED);
		
		layouter.createAnimationPath(presentation, new GoToAnimationStyle(), true);
		
		assertEquals(3, presentation.getTotalAnimationStepCount());
		AnimationStep step = presentation.getFirstAnimationStep();
		assertSame(presentation,step.getStartShape());
		assertSame(shape1, step.getEndShape());
		assertTrue(step.isAutoZoomEnabled());
		step = step.getNext();
		assertSame(shape1, step.getStartShape());
		assertSame(shape2, step.getEndShape());
		assertTrue(step.isAutoZoomEnabled());
		step = step.getNext();
		assertSame(shape2, step.getStartShape());
		assertSame(shape3, step.getEndShape());
		assertTrue(step.isAutoZoomEnabled());
	}
	
	public void testDeepShapeHierarchyAnimationCreation() {
		StandardAnimationPathCreator layouter = new StandardAnimationPathCreator();
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
		
		layouter.createAnimationPath(presentation, new GoToAnimationStyle(), true);
		
		assertEquals(5, presentation.getTotalAnimationStepCount());

		AnimationStep step = presentation.getFirstAnimationStep();
		assertSame(presentation, step.getStartShape());
		assertSame(top1, step.getEndShape());
		assertTrue(step.isAutoZoomEnabled());
		step = step.getNext();
		assertSame(top1, step.getStartShape());
		assertSame(child21, step.getEndShape());
		assertTrue(step.isAutoZoomEnabled());
		step = step.getNext();
		assertSame(child21, step.getStartShape());
		assertSame(top3, step.getEndShape());
		assertTrue(step.isAutoZoomEnabled());
		
		step = presentation.getFirstAnimationStep();
		AnimationStep zoomStep = step.getChild();
		assertSame(top1, zoomStep.getStartShape());
		assertSame(child11, zoomStep.getEndShape());
		assertTrue(zoomStep.isAutoZoomEnabled());
		zoomStep = zoomStep.getNext();
		assertSame(child11, zoomStep.getStartShape());
		assertSame(child12, zoomStep.getEndShape());
		assertFalse(zoomStep.hasNext());
		assertTrue(zoomStep.isAutoZoomEnabled());
		
		step = step.getNext();
		zoomStep = step.getChild();
		assertNull(zoomStep);
	
		step = step.getNext();
		zoomStep = step.getChild();
		assertNull(zoomStep);
	}
	
	public void testDeepShapeHierarchyAnimationCreationWithDisabledZooming() {
		StandardAnimationPathCreator layouter = new StandardAnimationPathCreator();
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
		
		layouter.createAnimationPath(presentation, new GoToAnimationStyle(), false);
		
		AnimationStep step = presentation.getFirstAnimationStep();
		assertFalse(step.isAutoZoomEnabled());
		step = step.getNext();
		assertFalse(step.isAutoZoomEnabled());
		step = step.getNext();
		assertFalse(step.isAutoZoomEnabled());
		
		step = presentation.getFirstAnimationStep();
		AnimationStep zoomStep = step.getChild();
		assertFalse(zoomStep.isAutoZoomEnabled());
		zoomStep = zoomStep.getNext();
		assertFalse(zoomStep.isAutoZoomEnabled());
	}
	
	protected static class ConcreteShape extends AbstractShape {
	}

}
