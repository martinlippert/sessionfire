package com.sessionfive.core.ui;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeColor;
import com.sessionfive.core.ShapeSize;

public abstract class AbstractLayouter implements Layouter {

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		return toString().equals(obj.toString());
	}

	public void resizeToDefault(Shape shape) {
		float sizeRatio = shape.getSize().getHeight() / shape.getSize().getWidth();
		float newWidth = 45f;
		float newHeight = newWidth * sizeRatio;
		shape.setSize(new ShapeSize(newWidth, newHeight, 0));
	}
	
	protected void reset(Shape shape) {
		shape.setFocussedPosition(null);
		shape.setCollapsedPosition(null);
		shape.setFocussedSize(null);
		shape.setCollapsedSize(null);
		shape.setFocussedRotation(null);
		shape.setCollapsedRotation(null);
		shape.setColor(ShapeColor.BLACK);
		shape.setCollapsedColor(ShapeColor.BLACK);
	}

}
