package com.sessionfive.core.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;
import com.sessionfive.core.ShapeRotation;

public class ExplodingLineGroupingLayouter extends AbstractLayouter {

	private static final String NAME = "Exploding Line Grouping";
	private final Random random = new Random();

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera newStartCamera = new Camera(-80f, 30f, 90f, -20f, 30.1f,
				0f, 0f, 1f, 0f);
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
			shape.setFocussedPosition(null);
			resizeToDefault(shape);

			float childZ = z - 0.1f;
			float childY = 0;
			float angleZ = 0;
			List<Shape> childs = shape.getShapes();
			Iterator<Shape> childIter = childs.iterator();

			while (childIter.hasNext()) {
				Shape child = childIter.next();
				child.setPosition(new ShapePosition(0, 0, childZ));
				child.setRotation(new ShapeRotation(0, 0, angleZ));
				resizeToDefault(child);

				child.setFocussedRotation(new ShapeRotation(0, 0, 0));
				child.setFocussedPosition(new ShapePosition(0,
						childY, childZ));
				
				child.setCollapsedPosition(new ShapePosition(0, 0, childZ));
				child.setCollapsedRotation(new ShapeRotation(0, 0, angleZ));

				childY -= childSpace;
				childZ -= 0.1f;
				angleZ = (random.nextInt() % 30);
			}

			if (childs.size() > 0) {
				childSpace *= -1;
			}

			x += space;
			z += 0.01f;
		}
	}

}
