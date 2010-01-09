package com.sessionfive.core.ui;

import java.util.List;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class TileLayouter implements Layouter {

	public static final String NAME = "Tiling";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera startCamera = new Camera(-80f, -40f, 120f, 20f, -50f, 0f, 0f, 1f, 0f);
		presentation.setStartCamera(startCamera);
		presentation.setDefaultStartCamera(startCamera);

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		int size = shapes.size();

		double sqrt = Math.sqrt(size);
		long countPerLine = Math.round(sqrt - 1);
		float z = 0f;

		float space = presentation.getSpace() * 2f;
		float x = -space;
		float y = -space;

		int lineCounter = 0;

		for (Shape shape : presentation.getShapes(LayerType.CAMERA_ANIMATED)) {
			shape.setPosition(x, y, z);

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
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TileLayouter))
			return false;
		return toString().equals(obj.toString());
	}

}
