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

}
