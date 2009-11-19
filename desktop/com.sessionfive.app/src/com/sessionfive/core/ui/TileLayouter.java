package com.sessionfive.core.ui;

import java.util.List;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class TileLayouter implements Layouter {

	@Override
	public String getName() {
		return "Tiling";
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera startCamera = new Camera(-80f, -40f,
				120f, 20f, -40f, 0f, 0f, 1f, 0f);
		presentation.setStartCamera(startCamera);
		
		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		int size = shapes.size();
		
		double sqrt = Math.sqrt(size);
		long countPerLine = Math.round(sqrt + 1);
		
		float x = -40f;
		float y = -20f;
		float z = 0f;
		
		int lineCounter = 0;

		for (Shape shape: presentation.getShapes(LayerType.CAMERA_ANIMATED)) {
			shape.setPosition(x, y, z);
			
			x += 50f;
			z += 0.01f;
			
			if (lineCounter == countPerLine) {
				lineCounter = 0;
				y -= 40;
				x = -40f;
			}
			else {
				lineCounter++;
			}
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
