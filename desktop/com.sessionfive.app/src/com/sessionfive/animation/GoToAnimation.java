package com.sessionfive.animation;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.sessionfive.app.Display;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Shape;

public class GoToAnimation implements Animation {

	public static final String NAME = "Go To";

	private final Shape endShape;
	private final Focusable startShape;

	public GoToAnimation(final Focusable startShape, final Shape endShape) {
		this.startShape = startShape;
		this.endShape = endShape;
	}

	@Override
	public Animator getForwardAnimation(Display display) {
		Camera startSetting = display.getCamera();
		Camera cameraEnd = endShape.getFocussedCamera();

		KeyValues<Camera> values2 = KeyValues.create(
				new EvaluatorCameraSetting(), startSetting,
				cameraEnd);
		KeyTimes times2 = new KeyTimes(0f, 1f);
		KeyFrames frames2 = new KeyFrames(values2, times2);
		PropertySetter ps2 = new PropertySetter(display, "camera",
				frames2);

		Animator animator = new Animator(1, ps2);
		animator.setStartDelay(0);

		return animator;
	}

	@Override
	public Animator getBackwardAnimation(Display display) {
		if (startShape == null)
			return null;

		Camera startSetting = display.getCamera();
		Camera cameraEnd = startShape.getFocussedCamera();

		KeyValues<Camera> values2 = KeyValues.create(
				new EvaluatorCameraSetting(), startSetting,
				cameraEnd);
		KeyTimes times2 = new KeyTimes(0f, 1f);
		KeyFrames frames2 = new KeyFrames(values2, times2);
		PropertySetter ps2 = new PropertySetter(display, "camera",
				frames2);

		Animator animator = new Animator(1, ps2);
		animator.setStartDelay(0);

		return animator;
	}

	@Override
	public void directlyGoTo(Display display) {
		Camera cameraEnd = endShape.getFocussedCamera();
		display.setCamera(cameraEnd);
	}
}
