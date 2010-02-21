package com.sessionfive.core.test;

import junit.framework.TestCase;

import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.AnimationStepIterator;

public class AnimationStepIteratorHierarchicalTest extends TestCase {

	public void testIterateDeepThroughLinearSteps() {
		AnimationStep top = new AnimationStep(null, null);
		AnimationStep step1 = new AnimationStep(null, null);
		AnimationStep step2 = new AnimationStep(null, null);
		AnimationStep step3 = new AnimationStep(null, null);
		top.addAnimationStep(step1);
		top.addAnimationStep(step2);
		top.addAnimationStep(step3);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		assertFalse(iter.hasChilds());
		assertFalse(iter.hasParent());
		
		iter.next();
		assertSame(step1, iter.current());
		assertFalse(iter.hasChilds());
		assertFalse(iter.hasParent());
		
		iter.next();
		assertSame(step2, iter.current());
		assertFalse(iter.hasChilds());
		assertFalse(iter.hasParent());
		
		iter.previous();
		assertSame(step1, iter.current());
		assertFalse(iter.hasChilds());
		assertFalse(iter.hasParent());
		
		iter.next();
		iter.next();
		assertSame(step3, iter.current());
		assertFalse(iter.hasChilds());
		assertFalse(iter.hasParent());
	}
	
	public void testIterateIntoChilds() {
		AnimationStep top = new AnimationStep(null, null);
		AnimationStep child = new AnimationStep(null, null);
		top.addAnimationStep(child);
		AnimationStep child2 = new AnimationStep(null, null);
		child.addAnimationStep(child2);
		AnimationStep child22 = new AnimationStep(null, null);
		child.addAnimationStep(child22);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		assertTrue(iter.hasNext());
		assertFalse(iter.hasChilds());
		assertFalse(iter.hasPrevious());
		assertFalse(iter.hasParent());
		
		iter.next();
		assertSame(child, iter.current());
		assertFalse(iter.hasNext());
		assertTrue(iter.hasChilds());
		assertFalse(iter.hasPrevious());
		assertFalse(iter.hasParent());
		
		iter.intoChilds();
		assertSame(child2, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertFalse(iter.hasChilds());
		assertTrue(iter.hasParent());
		
		iter.next();
		assertSame(child22, iter.current());
		assertFalse(iter.hasNext());
		assertFalse(iter.hasChilds());
		assertTrue(iter.hasParent());
	}
	
	public void testIterateIntoChildsUsingIncludingChilds() {
		AnimationStep top = new AnimationStep(null, null);
		AnimationStep child = new AnimationStep(null, null);
		top.addAnimationStep(child);
		AnimationStep child2 = new AnimationStep(null, null);
		child.addAnimationStep(child2);
		AnimationStep child22 = new AnimationStep(null, null);
		child.addAnimationStep(child22);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		
		iter.nextIncludingChilds();
		assertSame(child, iter.current());
		
		iter.nextIncludingChilds();
		assertSame(child2, iter.current());
		
		iter.nextIncludingChilds();
		assertSame(child22, iter.current());
	}
	
	public void testIterateByIncludingChildsAndBacktracking() {
		AnimationStep top = new AnimationStep(null, null);
		AnimationStep child1 = new AnimationStep(null, null);
		top.addAnimationStep(child1);
		AnimationStep child2 = new AnimationStep(null, null);
		top.addAnimationStep(child2);
		AnimationStep child11 = new AnimationStep(null, null);
		child1.addAnimationStep(child11);
		AnimationStep child12 = new AnimationStep(null, null);
		child1.addAnimationStep(child12);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		
		iter.nextIncludingChilds();
		assertSame(child1, iter.current());
		
		iter.nextIncludingChilds();
		assertSame(child11, iter.current());
		
		iter.nextIncludingChilds();
		assertSame(child12, iter.current());
		
		iter.nextIncludingChilds();
		assertSame(child2, iter.current());
	}
	
	public void testIterateIntoChildsAndBack() {
		AnimationStep top = new AnimationStep(null, null);
		AnimationStep child1 = new AnimationStep(null, null);
		AnimationStep child2 = new AnimationStep(null, null);
		top.addAnimationStep(child1);
		top.addAnimationStep(child2);

		AnimationStep child11 = new AnimationStep(null, null);
		child1.addAnimationStep(child11);
		AnimationStep child12 = new AnimationStep(null, null);
		child1.addAnimationStep(child12);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		assertNull(iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertFalse(iter.hasChilds());
		assertFalse(iter.hasParent());
		
		iter.next();
		iter.intoChilds();
		assertSame(child11, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasChilds());
		assertTrue(iter.hasParent());
		iter.next();
		assertSame(child12, iter.current());
		assertFalse(iter.hasNext());
		assertFalse(iter.hasChilds());
		assertTrue(iter.hasParent());
		
		iter.backToParent();
		assertSame(child1, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
		assertTrue(iter.hasChilds());
		assertFalse(iter.hasParent());
	}
	
	public void testIterateIntoChildsAndTheSameWayBack() {
		AnimationStep top = new AnimationStep(null, null);
		AnimationStep child1 = new AnimationStep(null, null);
		AnimationStep child2 = new AnimationStep(null, null);
		top.addAnimationStep(child1);
		top.addAnimationStep(child2);

		AnimationStep child11 = new AnimationStep(null, null);
		child1.addAnimationStep(child11);
		AnimationStep child12 = new AnimationStep(null, null);
		child1.addAnimationStep(child12);
		
		AnimationStepIterator iter = new AnimationStepIterator(top);
		iter.next();
		iter.next();
		assertSame(child2, iter.current());
		iter.previousIncludingChilds();
		assertSame(child12, iter.current());
		assertFalse(iter.hasNext());
		assertTrue(iter.hasParent());
		assertTrue(iter.hasPrevious());
		iter.previous();
		assertSame(child11, iter.current());
		assertTrue(iter.hasNext());
		assertFalse(iter.hasPrevious());
	}

}
