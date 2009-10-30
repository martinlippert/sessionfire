package com.sessionfive.core;

public class ShapeChangedEvent {
	
	private final Shape changedShape;

	public ShapeChangedEvent(Shape changedShape) {
		this.changedShape = changedShape;
	}
	
	public Shape getChangedShape() {
		return changedShape;
	}

}
