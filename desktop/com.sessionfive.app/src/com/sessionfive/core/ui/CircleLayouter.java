package com.sessionfive.core.ui;

import java.util.Iterator;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class CircleLayouter extends AbstractLinearLayouter {

	private static final String NAME = "Circle";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {

		int size = 0;
		Iterator<Shape> iter = presentation.shapeIterator(true);
		while (iter.hasNext()) {
			iter.next();
			size++;
		}

		final Camera startCamera = new Camera(-80f, 0f, size * 25f, 1, 1, 0f, 0f, 1, 0f);
		presentation.setDefaultStartCamera(startCamera);

		float space = presentation.getSpace() / 13f;

		float z = 0f;
		int i = 0;
		float centerx = 0f;
		float centery = 0f;
		float radius = size * 5f;

		iter = presentation.shapeIterator(false);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			if (shape.getClass() == AbstractShape.class) {
				shape.setPosition(0, 0, 0);
			}
			else {
				float x = (float) (centerx + radius * Math.cos(2 * Math.PI * i / size)) * space;
				float y = (float) (centery + radius * Math.sin(2 * Math.PI * i / size)) * space;
				z += 0.01f;
				shape.setPosition(x, y, z);
				resizeToDefault(shape);
				i++;
			}
		}
	}

}
