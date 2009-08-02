package com.sessionfive.core.ui;

import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class LineLayouter implements Layouter {
	
	private final boolean rotation;

	public LineLayouter(boolean rotation) {
		this.rotation = rotation;
	}

	@Override
	public String getName() {
		return rotation ? "Line with rotation" : "Line";
	}

	@Override
	public void layout(Presentation presentation) {
		float x = -40f;
		float z = 0f;
		float rot = 0f;
		
		for (Shape shape: presentation.getShapes()) {
			shape.setPosition(x, -20f, z);
			shape.setRotation(rot);
			
			x += 50f;
			z += 0.01f;
			
			if (rotation) rot += 5f;
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
