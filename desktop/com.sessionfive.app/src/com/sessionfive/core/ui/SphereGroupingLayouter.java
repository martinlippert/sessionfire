package com.sessionfive.core.ui;

import java.util.Iterator;
import java.util.List;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;
import com.sessionfive.core.ShapeSize;

public class SphereGroupingLayouter extends AbstractLayouter {

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
			shape.setPosition(new ShapePosition(x, y, z));
			i++;
			
			layoutChilds(shape);
		}
	}

	private void layoutChilds(Shape group) {
		List<Shape> childs = group.getShapes();
		if (childs.size() > 0) {
			int startIndex = 0;
			float width = group.getSize().getWidth();
			float height = group.getSize().getHeight();

			if (group.getClass() == AbstractShape.class) {
				childs.get(0).setPosition(ShapePosition.ZERO);
				width = childs.get(0).getSize().getWidth();
				height = childs.get(0).getSize().getHeight();
				startIndex = 1;
			}
			
			int noOfChildsToLayout = childs.size() - startIndex;
			float centerx = width / 2;
			float centery = height / 2;
			float radius =  height / 1.5f;
			float ellipseFactor = 1.4f;
			float z = 0f;
			float space = 1;
			
			for(int i = 0; i < noOfChildsToLayout; i++) {
				Shape child = childs.get(i + startIndex);
				
				float x = (float) (centerx + radius * Math.cos(2 * Math.PI * i / noOfChildsToLayout) * ellipseFactor) * space;
				float y = (float) (centery + radius * Math.sin(2 * Math.PI * i / noOfChildsToLayout)) * space;
				z += 0.01f;
				
				float childWidth = child.getSize().getWidth();
				float childHeight = child.getSize().getHeight();
				float childDepth = child.getSize().getDepth();

				float childFactor = childHeight / childWidth;
				
				childWidth = width / 4;
				childHeight = childWidth * childFactor;
				
				x -= childWidth / 2;
				y -= childHeight / 2;
				
				child.setPosition(new ShapePosition(x , y, z));
				child.setSize(new ShapeSize(childWidth, childHeight, childDepth));
			}
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
