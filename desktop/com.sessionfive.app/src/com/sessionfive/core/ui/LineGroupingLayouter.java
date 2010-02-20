package com.sessionfive.core.ui;

import java.util.Iterator;
import java.util.List;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class LineGroupingLayouter extends AbstractLinearLayouter {

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
		float x = -space;
		float z = 0f;

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			shape.setPosition(x, 10, z);
			
			float childY = 0;
			float childZ = 0;
			List<Shape> childs = shape.getShapes();
			Iterator<Shape> childIter = childs.iterator();
			while (childIter.hasNext()) {
				Shape child = childIter.next();
				child.setPosition(0, childY, childZ);
				childY -= space;
				childZ += 0.01f;
			}
			
			x += space;
			z += 0.01f;
		}
	}
	
	@Override
	public void animate(Presentation presentation, AnimationStyle animationStyle) {

		presentation.removeAllAnimationSteps();

		Focusable animationStart = presentation;
		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			
			int startWithChildNo = 0;
			AnimationStep step = null;
			if (shape.getClass() != AbstractShape.class) {
				step = new AnimationStep(animationStart, shape);
				step.setStyle(animationStyle);
				presentation.addAnimationStep(step);
				animationStart = shape;
			}
			else {
				List<Shape> childs = shape.getShapes();
				if (childs.size() > 0) {
					Shape firstChild = childs.get(0);
					step = new AnimationStep(animationStart, firstChild);
					step.setStyle(animationStyle);
					presentation.addAnimationStep(step);
					animationStart = firstChild;
					startWithChildNo = 1;
				}
			}
			
			Focusable subStart = animationStart;
			List<Shape> childs = shape.getShapes();
			for(int i = startWithChildNo; i < childs.size(); i++) {
				Shape child = childs.get(i);
				
				AnimationStep subStep = new AnimationStep(subStart, child);
				subStep.setStyle(animationStyle);
				
				step.addAnimationStep(subStep);
				subStart = child;
			}
		}
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LineGroupingLayouter))
			return false;
		return toString().equals(obj.toString());
	}

}
