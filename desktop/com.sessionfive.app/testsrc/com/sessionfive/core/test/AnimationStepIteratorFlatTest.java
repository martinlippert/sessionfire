package com.sessionfive.core.test;

import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.AnimationStepIterator;

import junit.framework.TestCase;

public class AnimationStepIteratorFlatTest extends TestCase {
	
	public void testIterateThroughEmptyStepList() {
		AnimationStep top = new AnimationStep(null, null);
		AnimationStepIterator iter = new AnimationStepIterator(top);
		assertFalse(iter.hasNext());
		assertFalse(iter.hasPrevious());
	}
	
	public void testIterateThroughLinearSteps() {
		AnimationStep top = new AnimationStep(null, null);
		
		AnimationStep step1 = new AnimationStep(null, null);
		AnimationStep step2 = new AnimationStep(null, null);
		AnimationStep step3 = new AnimationStep(null, null);
		
		top.addAnimationStep(step1);
		top.addAnimationStep(step2);
		top.addAnimationStep(step3);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		assertNull(iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		
		iter.next();
		assertSame(step1, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		
		iter.next();
		assertSame(step2, iter.current());
		assertTrue(iter.hasNext());
		assertTrue(iter.hasPrevious());
		
		iter.previous();
		assertSame(step1, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		
		iter.next();
		iter.next();
		assertSame(step3, iter.current());
		assertFalse(iter.hasNext());
		assertTrue(iter.hasPrevious());
	}
	
	public void testIterateThroughLinearStepsUsingIncludingChilds() {
		AnimationStep top = new AnimationStep(null, null);
		
		AnimationStep step1 = new AnimationStep(null, null);
		AnimationStep step2 = new AnimationStep(null, null);
		AnimationStep step3 = new AnimationStep(null, null);
		
		top.addAnimationStep(step1);
		top.addAnimationStep(step2);
		top.addAnimationStep(step3);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		
		iter.nextIncludingChilds();
		assertSame(step1, iter.current());
		
		iter.nextIncludingChilds();
		assertSame(step2, iter.current());
		
		iter.previous();
		assertSame(step1, iter.current());
		
		iter.nextIncludingChilds();
		iter.nextIncludingChilds();
		assertSame(step3, iter.current());
	}
	
	public void testIterateFlatThroughHierarchicalSteps() {
		AnimationStep top = new AnimationStep(null, null);
		
		AnimationStep step1 = new AnimationStep(null, null);
		AnimationStep step2 = new AnimationStep(null, null);
		AnimationStep step3 = new AnimationStep(null, null);
		
		top.addAnimationStep(step1);
		top.addAnimationStep(step2);
		top.addAnimationStep(step3);
		
		AnimationStep step11 = new AnimationStep(null, null);
		AnimationStep step21 = new AnimationStep(null, null);
		AnimationStep step31 = new AnimationStep(null, null);

		step1.addAnimationStep(step11);
		step1.addAnimationStep(step21);
		step3.addAnimationStep(step31);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		
		iter.next();
		assertSame(step1, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		
		iter.next();
		assertSame(step2, iter.current());
		assertTrue(iter.hasNext());
		assertTrue(iter.hasPrevious());
		
		iter.next();
		assertSame(step3, iter.current());
		assertFalse(iter.hasNext());
		assertTrue(iter.hasPrevious());
		
		iter.previous();
		assertSame(step2, iter.current());
		assertTrue(iter.hasNext());
		assertTrue(iter.hasPrevious());
		
		iter.previous();
		assertSame(step1, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
	}

}
