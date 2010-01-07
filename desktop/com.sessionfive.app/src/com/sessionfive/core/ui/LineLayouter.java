package com.sessionfive.core.ui;

import java.util.List;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class LineLayouter extends MoveableLayouter implements Layouter {

	public static final String NAME = "Line";

	public LineLayouter() {
		super(-80f, -20f, -3.2f);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera startCamera = new Camera(locationX, -3.1f, 90f, targetX, targetY, 0f, 0f, 1f,
				0f);
		presentation.setStartCamera(startCamera);

		float space = presentation.getSpace() * 2f;
		float x = -space;
		float z = 0f;

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		for (Shape shape : shapes) {
			shape.setPosition(x, targetX, z);
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
