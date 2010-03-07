package com.sessionfive.core.test;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;

import junit.framework.TestCase;

public class AnimationStepTest extends TestCase {
	
	public void testSimpleAnimationStep() {
		AbstractShape shape1 = new AbstractShape();
		AbstractShape shape2 = new AbstractShape();
		
		AnimationStep animation = new AnimationStep(shape1, shape2);
		assertSame(shape1, animation.getStartShape());
		assertSame(shape2, animation.getEndShape());
	}
	
	public void testAnimationStepSequence() {
		AbstractShape shape1 = new AbstractShape();
		AbstractShape shape2 = new AbstractShape();
		
		AnimationStep step1 = new AnimationStep(shape1, shape2);
		AnimationStep step2 = new AnimationStep(shape1, shape2);
		AnimationStep step3 = new AnimationStep(shape1, shape2);

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

}
