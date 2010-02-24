package com.sessionfive.core.ui;

import java.util.Iterator;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class SphereGroupingLayouter extends AbstractGroupingLayouter {

	private static final String NAME = "Sphere Grouping";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		
		int noTopLevelShapes = getNumberTopLevelShapes(presentation);

		final Camera startCamera = new Camera(-80f, 0f, noTopLevelShapes * 25f, 1, 1, 0f, 0f, 1, 0f);
		presentation.setDefaultStartCamera(startCamera);

		float space = presentation.getSpace() / 13f;

		float z = 0f;
		int i = 0;
		float centerx = 0f;
		float centery = 0f;
		float radius = noTopLevelShapes * 5f;
		float ellipseFactor = 1.4f;

		Iterator<Shape> iter = presentation.getShapes(LayerType.CAMERA_ANIMATED).iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			
			float x = (float) (centerx + radius * Math.cos(2 * Math.PI * i / noTopLevelShapes) * ellipseFactor) * space;
			float y = (float) (centery + radius * Math.sin(2 * Math.PI * i / noTopLevelShapes)) * space;
			z += 0.01f;
			shape.setPosition(x, y, z);
			i++;
		}
	}

	private int getNumberTopLevelShapes(Presentation presentation) {
		int result = 0;
		
		Iterator<Shape> iter = presentation.getShapes(LayerType.CAMERA_ANIMATED).iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			if (shape.getClass() != AbstractShape.class || shape.getShapes().size() > 0) {
				result++;
			}
		}
		return result;
	}

}
