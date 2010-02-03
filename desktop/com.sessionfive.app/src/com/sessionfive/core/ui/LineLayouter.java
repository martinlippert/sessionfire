package com.sessionfive.core.ui;

import java.util.Iterator;

import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class LineLayouter implements Layouter {

	public static final String NAME = "Line";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera newStartCamera = new Camera(-80f, 30f, 90f, -20f, 30.1f, 0f, 0f, 1f, 0f);
		presentation.setDefaultStartCamera(newStartCamera);

		float space = presentation.getSpace() * 2f;
		float x = -space;
		float z = 0f;

		Iterator<Shape> iter = presentation.shapeIterator(true);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			shape.setPosition(x, 10, z);
			x += space;
			z += 0.01f;
		}
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LineLayouter))
			return false;
		return toString().equals(obj.toString());
	}

}
