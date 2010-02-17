package com.sessionfive.core.test;

import com.sessionfive.animation.GoToAnimationStyle;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ui.AbstractLinearLayouter;

import junit.framework.TestCase;

public class AbstractLinearLayouterTest extends TestCase {
	
	public void testEmptyPresentationAnimationCreation() {
		ConcreteLinearLayouter layouter = new ConcreteLinearLayouter();
		Presentation presentation = new Presentation();
		layouter.animate(presentation, new GoToAnimationStyle());
		
		assertEquals(0, presentation.getAnimationStepCount());
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
		
		assertEquals(3, presentation.getAnimationStepCount());
		assertSame(presentation, presentation.getAnimationStep(0).getStartShape());
		assertSame(shape1, presentation.getAnimationStep(0).getEndShape());
		assertSame(shape1, presentation.getAnimationStep(1).getStartShape());
		assertSame(shape2, presentation.getAnimationStep(1).getEndShape());
		assertSame(shape2, presentation.getAnimationStep(2).getStartShape());
		assertSame(shape3, presentation.getAnimationStep(2).getEndShape());
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
		
		assertEquals(5, presentation.getAnimationStepCount());
		assertSame(presentation, presentation.getAnimationStep(0).getStartShape());
		assertSame(top1, presentation.getAnimationStep(0).getEndShape());
		assertSame(top1, presentation.getAnimationStep(1).getStartShape());
		assertSame(child11, presentation.getAnimationStep(1).getEndShape());
		assertSame(child11, presentation.getAnimationStep(2).getStartShape());
		assertSame(child12, presentation.getAnimationStep(2).getEndShape());
		assertSame(child12, presentation.getAnimationStep(3).getStartShape());
		assertSame(child21, presentation.getAnimationStep(3).getEndShape());
		assertSame(child21, presentation.getAnimationStep(4).getStartShape());
		assertSame(top3, presentation.getAnimationStep(4).getEndShape());
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
