package com.sessionfive.core.test;

import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;

import junit.framework.TestCase;

public class PresentationTest extends TestCase {
	
	public void testSimplePresentation() {
		Presentation presentation = new Presentation();
		assertEquals(0, presentation.getTotalAnimationStepCount());
		assertEquals(0, presentation.getShapes(LayerType.CAMERA_ANIMATED).size());
	}
	
}
