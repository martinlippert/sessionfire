package com.sessionfive.core.ui;

import java.util.Iterator;
import java.util.List;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class ExplodingLineGroupingLayouter extends AbstractLayouter {

	private static final String NAME = "Exploding Line Grouping";

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

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			shape.setPosition(x, 10, z);
			resizeToDefault(shape);
			
			float childZ = z - 0.1f;
			float angleZ = 0;
			List<Shape> childs = shape.getShapes();
			Iterator<Shape> childIter = childs.iterator();
			
			while (childIter.hasNext()) {
				Shape child = childIter.next();
				child.setPosition(0, 0, childZ);
				resizeToDefault(child);
				child.setRotation(0, 0, angleZ);

				childZ -= 0.1f;
				angleZ += 5;
			}
			
			x += space;
			z += 0.01f;
		}
	}
	
}
