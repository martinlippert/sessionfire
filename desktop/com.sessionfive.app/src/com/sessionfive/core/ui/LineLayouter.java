package com.sessionfive.core.ui;

import java.util.Iterator;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;

public class LineLayouter extends AbstractLayouter {

	private static final String NAME = "Line";

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

		Iterator<Shape> iter = presentation.shapeIterator(false);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			if (shape.getClass() == AbstractShape.class) {
				shape.setPosition(ShapePosition.EMPTY);
			}
			else {
				shape.setPosition(new ShapePosition(x, 10, z));
				resizeToDefault(shape);

				x += space;
				z += 0.01f;
			}
		}
	}

}
