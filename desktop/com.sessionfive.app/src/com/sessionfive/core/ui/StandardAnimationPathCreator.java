package com.sessionfive.core.ui;

import java.util.Iterator;
import java.util.List;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class StandardAnimationPathCreator {

	public void createAnimationPath(Presentation presentation,
			AnimationStyle style, boolean autoZoomIn) {
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
				step.setStyle(style);
				step.setAutoZoomEnabled(autoZoomIn);
				presentation.addAnimationStep(step);
				animationStart = shape;
			} else {
				List<Shape> childs = shape.getShapes();
				if (childs.size() > 0) {
					Shape firstChild = childs.get(0);
					step = new AnimationStep(animationStart, firstChild);
					step.setStyle(style);
					step.setAutoZoomEnabled(autoZoomIn);
					presentation.addAnimationStep(step);
					animationStart = firstChild;
					startWithChildNo = 1;
				}
			}

			Focusable subStart = animationStart;
			List<Shape> childs = shape.getShapes();
			for (int i = startWithChildNo; i < childs.size(); i++) {
				Shape child = childs.get(i);

				AnimationStep subStep = new AnimationStep(subStart, child);
				subStep.setStyle(style);
				subStep.setAutoZoomEnabled(autoZoomIn);

				step.addChild(subStep);
				subStart = child;
			}
		}
	}

}
