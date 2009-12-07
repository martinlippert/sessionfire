package com.sessionfive.core.ui;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class LineLayouter implements Layouter {
	
	public static final String NAME = "Line";

	public LineLayouter() {
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera startCamera = new Camera(-80f, -3.1f,
				90f, -20f, -3.1f, 0f, 0f, 1f, 0f);
		presentation.setStartCamera(startCamera);
		
		float x = -40f;
		float z = 0f;
		
		for (Shape shape: presentation.getShapes(LayerType.CAMERA_ANIMATED)) {
			shape.setPosition(x, -20f, z);
			x += 50f;
			z += 0.01f;
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LineLayouter)) return false;
		return toString().equals(obj.toString());
	}

}
