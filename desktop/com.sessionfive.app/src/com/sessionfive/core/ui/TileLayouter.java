package com.sessionfive.core.ui;

import java.util.Iterator;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;

public class TileLayouter extends AbstractLayouter {

	private static final String NAME = "Tiling";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera startCamera = new Camera(-80f, -40f, 120f, 20f, -50f, 0f, 0f, 1f, 0f);
		presentation.setDefaultStartCamera(startCamera);

		int size = 0;
		Iterator<Shape> iter = presentation.shapeIterator(true);
		while (iter.hasNext()) {
			iter.next();
			size++;
		}

		double sqrt = Math.sqrt(size);
		long countPerLine = Math.round(sqrt - 1);
		float z = 0f;

		float space = presentation.getSpace() * 2f;
		float x = -space;
		float y = -space;

		int lineCounter = 0;

		iter = presentation.shapeIterator(false);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			if (shape.getClass() == AbstractShape.class) {
				shape.setPosition(ShapePosition.ZERO);
			}
			else {
				shape.setPosition(new ShapePosition(x, y, z));
				resizeToDefault(shape);
	
				x += space;
				z += 0.01f;
	
				if (lineCounter == countPerLine) {
					lineCounter = 0;
					y -= space;
					x = -space;
				} else {
					lineCounter++;
				}
			}
			
			shape.setFocussedPosition(null);
			shape.setCollapsedPosition(null);
			shape.setFocussedSize(null);
			shape.setCollapsedSize(null);
			shape.setFocussedRotation(null);
			shape.setCollapsedRotation(null);
		}
	}

}
