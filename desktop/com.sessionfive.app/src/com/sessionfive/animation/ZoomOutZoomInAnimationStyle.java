package com.sessionfive.animation;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.sessionfive.app.Display;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Shape;

public class ZoomOutZoomInAnimationStyle implements AnimationStyle {

	public static final String NAME = "Zoom Out - Zoom In";

	@Override
	public Animator createForwardAnimator(Camera cameraStart, Camera cameraEnd,
			Display display, Shape endShape) {
		float shapeX = endShape.getAbsolutePosition().getX();

		float positionY = (cameraStart.getLocation().getY() + cameraEnd
				.getLocation().getY()) / 2;
		float targetY = (cameraStart.getTarget().getY() + cameraEnd.getTarget()
				.getY()) / 2;

		Camera cameraMid = new Camera(shapeX - 70f, positionY, 100f,
				shapeX - 10f, targetY, 0, cameraStart.getUp().getX(),
				cameraStart.getUp().getY(), cameraStart.getUp().getZ());

		KeyValues<Camera> values = KeyValues
				.create(new CameraMoveEvaluator(), cameraStart, cameraMid,
						cameraEnd);
		KeyTimes times = new KeyTimes(0f, 0.5f, 1f);
		KeyFrames frames = new KeyFrames(values, times);
		PropertySetter ps = new PropertySetter(display, "camera", frames);

		Animator animator = new Animator(1500, ps);
		animator.setStartDelay(0);
		animator.setAcceleration(0.4f);
		animator.setDeceleration(0.4f);

		return animator;
	}

	@Override
	public Animator createBackwardAnimator(Camera cameraStart,
			Camera cameraEnd, Display display, Shape endShape) {
		float shapeX = endShape.getAbsolutePosition().getX();

		float positionY = (cameraStart.getLocation().getY() + cameraEnd
				.getLocation().getY()) / 2;
		float targetY = (cameraStart.getTarget().getY() + cameraEnd.getTarget()
				.getY()) / 2;

		Camera cameraMid = new Camera(shapeX - 70f, positionY, 100f,
				shapeX - 10f, targetY, 0, cameraStart.getUp().getX(),
				cameraStart.getUp().getY(), cameraStart.getUp().getZ());

		KeyValues<Camera> values = KeyValues
				.create(new CameraMoveEvaluator(), cameraStart, cameraMid,
						cameraEnd);
		KeyTimes times = new KeyTimes(0f, 0.5f, 1f);
		KeyFrames frames = new KeyFrames(values, times);
		PropertySetter ps = new PropertySetter(display, "camera", frames);

		Animator animator = new Animator(1500, ps);
		animator.setStartDelay(0);
		animator.setAcceleration(0.4f);
		animator.setDeceleration(0.4f);

		return animator;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ZoomOutZoomInAnimationStyle))
			return false;
		return toString().equals(obj.toString());
	}

}
