package com.sessionfive.core.test;

import com.sessionfive.core.AbstractShape;

import junit.framework.TestCase;

public class AbstractShapeTest extends TestCase {
	
	public void testPositionWithoutOwnerInitial() {
		AbstractShape shape = new AbstractShape();
		assertEquals(0f, shape.getX());
		assertEquals(0f, shape.getY());
		assertEquals(0f, shape.getZ());
		
		assertNull(shape.getOwner());
		assertEquals(0f, shape.getAbsoluteX());
		assertEquals(0f, shape.getAbsoluteY());
		assertEquals(0f, shape.getAbsoluteZ());
	}

	public void testPositionWithoutOwnerAtSpecificPosition() {
		AbstractShape shape = new AbstractShape();
		shape.setPosition(5f, 6.5f, 56.9f);
		assertEquals(5f, shape.getX());
		assertEquals(6.5f, shape.getY());
		assertEquals(56.9f, shape.getZ());
		
		assertNull(shape.getOwner());
		assertEquals(5f, shape.getAbsoluteX());
		assertEquals(6.5f, shape.getAbsoluteY());
		assertEquals(56.9f, shape.getAbsoluteZ());
	}
	
	public void testPositionWithOwner() {
		AbstractShape shape = new AbstractShape();
		AbstractShape owner = new AbstractShape();
		
		owner.setPosition(1, 2, 3);
		shape.setPosition(4, 5, 6);
		owner.addShape(shape);
		
		assertEquals(4f, shape.getX());
		assertEquals(5f, shape.getY());
		assertEquals(6f, shape.getZ());
		
		assertSame(owner, shape.getOwner());
		assertEquals(5f, shape.getAbsoluteX());
		assertEquals(7f, shape.getAbsoluteY());
		assertEquals(9f, shape.getAbsoluteZ());
	}

	public void testPositionWithOwnerHierarchy() {
		AbstractShape shape = new AbstractShape();
		AbstractShape owner = new AbstractShape();
		AbstractShape ownerowner = new AbstractShape();
		AbstractShape ownerownerowner = new AbstractShape();
		
		shape.setPosition(1, 2, 3);
		owner.setPosition(2, 4, 6);
		ownerowner.setPosition(3, 6, 9);
		ownerownerowner.setPosition(4, 8, 12);

		ownerownerowner.addShape(ownerowner);
		ownerowner.addShape(owner);
		owner.addShape(shape);
		
		assertEquals(1f, shape.getX());
		assertEquals(2f, shape.getY());
		assertEquals(3f, shape.getZ());
		
		assertSame(owner, shape.getOwner());
		assertSame(ownerowner, owner.getOwner());
		assertSame(ownerownerowner, ownerowner.getOwner());

		assertEquals(10f, shape.getAbsoluteX());
		assertEquals(20f, shape.getAbsoluteY());
		assertEquals(30f, shape.getAbsoluteZ());

		assertEquals(9f, owner.getAbsoluteX());
		assertEquals(18f, owner.getAbsoluteY());
		assertEquals(27f, owner.getAbsoluteZ());

		assertEquals(7f, ownerowner.getAbsoluteX());
		assertEquals(14f, ownerowner.getAbsoluteY());
		assertEquals(21f, ownerowner.getAbsoluteZ());
	}

}
