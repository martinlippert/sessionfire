package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;

public class AnimationStepTest extends TestCase {
	
	public void testSimpleAnimationStep() {
		AbstractShape shape = new AbstractShape();
		
		AnimationStep step = new AnimationStep(shape);
		assertSame(shape, step.getFocussedShape());
		assertFalse(step.hasChild());
	}
	
	public void testAnimationStepAutoZoom() {
		AnimationStep step = new AnimationStep();
		assertTrue(step.isAutoZoomEnabled());
		
		step.setAutoZoomEnabled(false);
		assertFalse(step.isAutoZoomEnabled());
	}
	
	public void testAnimationStepSequence() {
		AnimationStep step1 = new AnimationStep();
		AnimationStep step2 = new AnimationStep();
		AnimationStep step3 = new AnimationStep();

		assertNull(step1.getPrevious());
		assertNull(step1.getParent());
		assertNull(step1.getNext());
		assertNull(step1.getChild());
		
		step1.setNext(step2);
		step2.setNext(step3);
		
		assertSame(step2, step1.getNext());
		assertSame(step1, step2.getPrevious());
		assertSame(step3, step2.getNext());
		assertSame(step2, step3.getPrevious());
	}
	
	public void testAnimationStepChilds() {
		AnimationStep parent = new AnimationStep();
		AnimationStep child1 = new AnimationStep();
		AnimationStep child2 = new AnimationStep();
		AnimationStep child3 = new AnimationStep();
		
		parent.addChild(child1);
		assertTrue(parent.hasChild());
		assertSame(child1, parent.getChild());
		assertSame(parent, child1.getParent());
		
		parent.addChild(child2);
		parent.addChild(child3);
		
		assertTrue(parent.hasChild());
		assertSame(child1, parent.getChild());
		assertSame(parent, child1.getParent());
		
		assertTrue(child1.hasNext());
		assertFalse(child1.hasPrevious());
		assertSame(child2, child1.getNext());
		assertSame(child1, child2.getPrevious());
		
		assertTrue(child2.hasPrevious());
		assertTrue(child2.hasNext());
		assertSame(child1, child2.getPrevious());
		assertSame(child3, child2.getNext());
		assertSame(parent, child2.getParent());
		assertFalse(child2.hasChild());
		
		assertTrue(child3.hasPrevious());
		assertFalse(child3.hasNext());
		assertSame(child2, child3.getPrevious());
		assertSame(parent, child3.getParent());
		assertFalse(child3.hasChild());
	}

}
