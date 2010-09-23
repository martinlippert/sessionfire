package com.sessionfire.live;

import java.util.List;

import org.jdesktop.animation.timing.TimingTarget;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFocusListener;

public class LiveSync implements ShapeFocusListener {

	private final AnimationController animationController;
	private final Live live;

	public LiveSync(AnimationController animationController, Live live) {
		this.animationController = animationController;
		this.live = live;
		
		animationController.addFocusListener(this);
	}

	@Override
	public TimingTarget[] startsFocussing(Shape shape) {
		if (live.isConnected()) {
			Presentation presentation = animationController.getPresentation();
			List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
			for (int i = 0; i < shapes.size(); i++) {
				if (shape == shapes.get(i)) {
					live.syncShapeFocus(i);
				}
			}
		}
		return null;
	}

	@Override
	public TimingTarget[] finishedFocussing(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget[] canceledFocussing(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget[] shapeLeft(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget[] groupOfShapeLeft(Shape shape) {
		return null;
	}

}
