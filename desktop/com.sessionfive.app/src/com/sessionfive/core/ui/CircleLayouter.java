package com.sessionfive.core.ui;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class CircleLayouter extends MoveableLayouter implements Layouter {

	public static final String NAME = "Circle";

	public CircleLayouter() {
		super(-80f, 1f, 1f);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		int size = presentation.getShapes(LayerType.CAMERA_ANIMATED).size();
		final Camera startCamera = new Camera(locationX, 0f, size * 25f, targetX, targetY, 0f, 0f,
				targetY, 0f);
		presentation.setStartCamera(startCamera);

		float space = presentation.getSpace() / 13f;

		float z = 0f;
		int i = 0;
		float centerx = 0f;
		float centery = 0f;
		float radius = size * 5f;

		for (Shape shape : presentation.getShapes(LayerType.CAMERA_ANIMATED)) {
			float x = (float) (centerx + radius * Math.cos(2 * Math.PI * i / size)) * space;
			float y = (float) (centery + radius * Math.sin(2 * Math.PI * i / size)) * space;
			z += 0.01f;
			shape.setPosition(x, y, z);
			i++;
		}
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CircleLayouter))
			return false;
		return toString().equals(obj.toString());
	}

}
