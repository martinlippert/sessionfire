package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.animation.GoToAnimationStyle;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ui.AbstractLinearLayouter;

public class AbstractLinearLayouterTest extends TestCase {
	
	public void testEmptyPresentationAnimationCreation() {
		ConcreteLinearLayouter layouter = new ConcreteLinearLayouter();
		Presentation presentation = new Presentation();
		layouter.animate(presentation, new GoToAnimationStyle());
		
		assertEquals(0, presentation.getTotalAnimationStepCount());
	}
	
	public void testFlatShapeHierarchyAnimationCreation() {
		ConcreteLinearLayouter layouter = new ConcreteLinearLayouter();
		Presentation presentation = new Presentation();
		Shape shape1 = new ConcreteShape();
		Shape shape2 = new ConcreteShape();
		Shape shape3 = new ConcreteShape();
		presentation.addShape(shape1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape3, LayerType.CAMERA_ANIMATED);
		
		layouter.animate(presentation, new GoToAnimationStyle());
		
		assertEquals(3, presentation.getTotalAnimationStepCount());
		AnimationStep step = presentation.getFirstAnimationStep();
		assertSame(presentation, step.getStartShape());
		assertSame(shape1, step.getEndShape());
		step = step.getNextStep();
		assertSame(shape1, step.getStartShape());
		assertSame(shape2, step.getEndShape());
		step = step.getNextStep();
		assertSame(shape2, step.getStartShape());
		assertSame(shape3, step.getEndShape());
	}
	
	public void testDeepShapeHierarchyAnimationCreation() {
		ConcreteLinearLayouter layouter = new ConcreteLinearLayouter();
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
		
		assertEquals(5, presentation.getTotalAnimationStepCount());
		AnimationStep step = presentation.getFirstAnimationStep();
		assertSame(presentation, step.getStartShape());
		assertSame(top1, step.getEndShape());
		step = step.getNextStep();
		assertSame(top1, step.getStartShape());
		assertSame(child11, step.getEndShape());
		step = step.getNextStep();
		assertSame(child11, step.getStartShape());
		assertSame(child12, step.getEndShape());
		step = step.getNextStep();
		assertSame(child12, step.getStartShape());
		assertSame(child21, step.getEndShape());
		step = step.getNextStep();
		assertSame(child21, step.getStartShape());
		assertSame(top3, step.getEndShape());
	}
	
	protected static class ConcreteLinearLayouter extends AbstractLinearLayouter {
		@Override
		public String getName() {
			return null;
		}

		@Override
		public void layout(Presentation presentation) {
		}
	}
	
	protected static class ConcreteShape extends AbstractShape {
	}

}
