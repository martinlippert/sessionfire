package com.sessionfive.core.test;

import java.util.Iterator;

import junit.framework.TestCase;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.shapes.ImageShape;

public class SkippingShapeIteratorTest extends TestCase {
	
	public void testShapeIteratorEmptyPresentation() {
		Presentation presentation = new Presentation();
		Iterator<Shape> iter = presentation.shapeIterator(true);
		assertFalse(iter.hasNext());
	}
	
	public void testShapeIteratorFlatPresentation() {
		Presentation presentation = new Presentation();
		Shape shape1 = new AbstractShape();
		Shape shape2 = new ImageShape(null);
		Shape shape3 = new ImageShape(null);
		presentation.addShape(shape1, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape2, LayerType.CAMERA_ANIMATED);
		presentation.addShape(shape3, LayerType.CAMERA_ANIMATED);
		
		Iterator<Shape> iter = presentation.shapeIterator(true);
		
		assertTrue(iter.hasNext());
		assertEquals(shape2, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(shape3, iter.next());
		assertFalse(iter.hasNext());
	}
	
	public void testShapeIteratorHierarchy() {
		Presentation presentation = new Presentation();

		Shape shape1 = new ImageShape(null);
		presentation.addShape(shape1, LayerType.CAMERA_ANIMATED);
		
		Shape group1 = new AbstractShape();
		Shape child1 = new ImageShape(null);
		Shape child2 = new ImageShape(null);
		group1.addShape(child1);
		group1.addShape(child2);
		presentation.addShape(group1, LayerType.CAMERA_ANIMATED);
		
		Shape child21 = new AbstractShape();
		child2.addShape(child21);
		
		Shape shape2 = new AbstractShape();
		presentation.addShape(shape2, LayerType.CAMERA_ANIMATED);
		
		Iterator<Shape> iter = presentation.shapeIterator(true);
		
		assertTrue(iter.hasNext());
		assertEquals(shape1, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(child1, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(child2, iter.next());
		assertFalse(iter.hasNext());
	}

}
