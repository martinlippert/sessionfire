package com.sessionfive.core.ui;

import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class CircleLayouter implements Layouter {
	
	public CircleLayouter() {
	}

	@Override
	public String getName() {
		return "Circle";
	}

	@Override
	public void layout(Presentation presentation) {
		int size = presentation.getShapes().size();
		final Camera startCamera = new Camera(-80f, -3.1f,
				size * 12f, -20f, -3.1f, 0f, 0f, 1f, 0f);
		presentation.setStartCamera(startCamera);
		
		float z = 0f;
		int i = 0;		
		float centerx = 0f;
		float centery = 0f;
		float radius = size * 5f;
		
		for (Shape shape: presentation.getShapes()) {
			float x = (float) (centerx + radius * Math.cos(2 * Math.PI * i / size)) * 1.5f;
			float y = (float) (centery + radius * Math.sin(2 * Math.PI * i / size)) * 1.5f;
			z += 0.01f;
			shape.setPosition(x, y, z);
			i++;
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
