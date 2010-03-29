package com.sessionfive.core.ui;

import java.util.Iterator;
import java.util.List;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;

public class LineGroupingLayouter extends AbstractLayouter {

	private static final String NAME = "Line Grouping";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera newStartCamera = new Camera(-80f, 30f, 90f, -20f, 30.1f, 0f, 0f, 1f, 0f);
		presentation.setDefaultStartCamera(newStartCamera);

		float space = presentation.getSpace() * 2f;
		float childSpace = space;
		float x = -space;
		float z = 0f;

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			shape.setPosition(new ShapePosition(x, 10, z));
			resizeToDefault(shape);
			
			float childY = 0;
			float childZ = 0;
			List<Shape> childs = shape.getShapes();
			Iterator<Shape> childIter = childs.iterator();
			
			while (childIter.hasNext()) {
				Shape child = childIter.next();
				child.setPosition(new ShapePosition(0, childY, childZ));
				resizeToDefault(child);

				child.setFocussedPosition(null);
				child.setCollapsedPosition(null);
				child.setFocussedSize(null);
				child.setCollapsedSize(null);
				child.setFocussedRotation(null);
				child.setCollapsedRotation(null);

				childY -= childSpace;
				childZ += 0.01f;
			}
			
			if (childs.size() > 0) {
				childSpace *= -1;
			}
			
			shape.setFocussedPosition(null);
			shape.setCollapsedPosition(null);
			shape.setFocussedSize(null);
			shape.setCollapsedSize(null);
			shape.setFocussedRotation(null);
			shape.setCollapsedRotation(null);
			
			x += space;
			z += 0.01f;
		}
	}
	
}
