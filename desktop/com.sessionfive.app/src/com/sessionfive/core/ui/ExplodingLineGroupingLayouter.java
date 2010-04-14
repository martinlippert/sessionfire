package com.sessionfive.core.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeColor;
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
		float childSpace = presentation.getSpace() * 1.5f;

		float additionalGroupSpace = 5;
		boolean additionalGroupSpaceAlreadyAdded = false;
		float x = -space;
		float z = 0f;

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();

			List<Shape> childs = shape.getShapes();
			if (childs.size() > 1) {
				Shape child1 = childs.get(0);
				child1.setFocusScale(presentation.getDefaultFocusScale() + 0.4f);
				
				if (!additionalGroupSpaceAlreadyAdded) {
					x += additionalGroupSpace;
				}
			}

			shape.setPosition(new ShapePosition(x, 10, z));
			shape.setFocussedPosition(null);
			resizeToDefault(shape);
			
			if (childs.size() > 1) {
				
			}

			float childZ = z - 1f;
			float childY = 0;
			float angleZ = random.nextInt() % 5;
			float color = 1;
			
			Iterator<Shape> childIter = childs.iterator();
			while (childIter.hasNext()) {
				Shape child = childIter.next();
				child.setPosition(new ShapePosition(0, 0, childZ));
				child.setRotation(new ShapeRotation(0, 0, angleZ));
				resizeToDefault(child);

				ShapeColor collapsedColor = new ShapeColor(color, color, color, 1f);
				child.setColor(collapsedColor);
				child.setCollapsedColor(collapsedColor);

				child.setFocussedRotation(new ShapeRotation(0, 0, 0));
				child.setFocussedPosition(new ShapePosition(0,
						childY, childZ));
				
				child.setCollapsedPosition(new ShapePosition(0, 0, childZ));
				child.setCollapsedRotation(new ShapeRotation(0, 0, angleZ));

				childY -= childSpace;
				childZ -= 1f;
				angleZ = (random.nextInt() % 15);
				color = Math.min(color, 0.9f);
				color = Math.max(0.1f, color - 0.05f);
			}

			if (childs.size() > 1) {
				childSpace *= -1;
				x += additionalGroupSpace;
				additionalGroupSpaceAlreadyAdded = true;
			}
			else {
				additionalGroupSpaceAlreadyAdded = false;
			}

			x += space;
			z += 0.01f;
		}
	}

}
