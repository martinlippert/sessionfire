package com.sessionfive.core.ui;

import java.util.LinkedList;
import java.util.List;

import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.shapes.GroupShape;

public class GroupLineLayouter implements Layouter {

	public static final String NAME = "Group Line";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void layout(Presentation presentation) {
		final Camera startCamera = new Camera(0f, 0f, 320f, 100f, -100f, 100f, 0f, 1f, 0f);
		presentation.setStartCamera(startCamera);

		// List<List<Shape>> groups = makeDummyGroups(presentation);
		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);

		int i = 0;
		for (Shape shape : shapes) {
			GroupShape group = (GroupShape) shape;
			float z = 0f;
			float space = presentation.getSpace() * 2f;
			float x = space + (i * 100);
			float y = -space - (i * 100);
			group.setPosition(x, y, z, space);
			x += 0.01;
			z -= space;
			i++;
		}

	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GroupLineLayouter))
			return false;
		return toString().equals(obj.toString());
	}

	private List<List<Shape>> makeDummyGroups(Presentation presentation) {
		List<Shape> allShapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		List<List<Shape>> groups = new LinkedList<List<Shape>>();
		int i = 0;
		LinkedList<Shape> group = null;
		for (Shape shape : allShapes) {
			if (i % 5 == 0) {
				group = new LinkedList<Shape>();
				groups.add(group);
			}
			group.add(shape);
			i++;
		}
		return groups;

	}

}
