package com.sessionfive.core.test;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.ShapePosition;

import junit.framework.TestCase;

public class AbstractShapeTest extends TestCase {
	
	public void testPositionWithoutOwnerInitial() {
		AbstractShape shape = new AbstractShape();

		assertNull(shape.getOwner());
		assertEquals(ShapePosition.ZERO, shape.getPosition());
		assertEquals(ShapePosition.ZERO, shape.getAbsolutePosition());
	}

	public void testPositionWithoutOwnerAtSpecificPosition() {
		AbstractShape shape = new AbstractShape();
		shape.setPosition(new ShapePosition(5f, 6.5f, 56.9f));

		assertEquals(new ShapePosition(5f, 6.5f, 56.9f), shape.getPosition());
		assertEquals(new ShapePosition(5f, 6.5f, 56.9f), shape.getAbsolutePosition());
		assertNull(shape.getOwner());
	}
	
	public void testPositionWithOwner() {
		AbstractShape shape = new AbstractShape();
		AbstractShape owner = new AbstractShape();
		
		owner.setPosition(new ShapePosition(1, 2, 3));
		shape.setPosition(new ShapePosition(4, 5, 6));
		owner.addShape(shape);
		
		assertSame(owner, shape.getOwner());
		assertEquals(new ShapePosition(4f, 5f, 6f), shape.getPosition());
		assertEquals(new ShapePosition(5f, 7f, 9f), shape.getAbsolutePosition());
	}

	public void testPositionWithOwnerHierarchy() {
		AbstractShape shape = new AbstractShape();
		AbstractShape owner = new AbstractShape();
		AbstractShape ownerowner = new AbstractShape();
		AbstractShape ownerownerowner = new AbstractShape();
		
		shape.setPosition(new ShapePosition(1, 2, 3));
		owner.setPosition(new ShapePosition(2, 4, 6));
		ownerowner.setPosition(new ShapePosition(3, 6, 9));
		ownerownerowner.setPosition(new ShapePosition(4, 8, 12));

		ownerownerowner.addShape(ownerowner);
		ownerowner.addShape(owner);
		owner.addShape(shape);
		
		assertEquals(new ShapePosition(1f, 2f, 3f), shape.getPosition());
		
		assertSame(owner, shape.getOwner());
		assertSame(ownerowner, owner.getOwner());
		assertSame(ownerownerowner, ownerowner.getOwner());

		assertEquals(new ShapePosition(10f, 20f, 30f), shape.getAbsolutePosition());
		assertEquals(new ShapePosition(9f, 18f, 27f), owner.getAbsolutePosition());
		assertEquals(new ShapePosition(7f, 14f, 21f), ownerowner.getAbsolutePosition());
	}

}
