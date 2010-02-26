package com.sessionfive.core.ui;

import com.sessionfive.core.Shape;

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
		float sizeRatio = shape.getHeight() / shape.getWidth();
		float newWidth = 45f;
		float newHeight = newWidth * sizeRatio;
		shape.setSize(newWidth, newHeight, 0);
	}

}
