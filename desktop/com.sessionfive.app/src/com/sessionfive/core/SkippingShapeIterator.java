package com.sessionfive.core;

public class SkippingShapeIterator extends ShapeIterator {
	
	public SkippingShapeIterator(Presentation presentation) {
		super(presentation);
	}
	
	@Override
	public boolean hasNext() {
		while (super.hasNext() && getCurrentShape().getClass() == AbstractShape.class) {
			super.next();
		}
		return super.hasNext();
	}
	
	@Override
	public Shape next() {
		if (hasNext()) {
			return super.next();
		}
		else {
			return null;
		}
	}

}
