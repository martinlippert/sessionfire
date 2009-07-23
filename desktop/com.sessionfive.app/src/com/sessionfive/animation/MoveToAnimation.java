package com.sessionfive.animation;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.sessionfive.app.Display;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Shape;

public class MoveToAnimation implements Animation {

	private final Shape endShape;
	private final Shape startShape;

	public MoveToAnimation(final Shape startShape, final Shape endShape) {
		this.startShape = startShape;
		this.endShape = endShape;
	}

	@Override
	public Animator getForwardAnimation(Display display) {
		CameraSetting startSetting = display.getCameraSetting();
		CameraSetting cameraEnd = ShapeFocus.getFocussedShape(endShape);

		KeyValues<CameraSetting> values2 = KeyValues.create(
				new EvaluatorCameraSetting(), startSetting,
				cameraEnd);
		KeyTimes times2 = new KeyTimes(0f, 1f);
		KeyFrames frames2 = new KeyFrames(values2, times2);
		PropertySetter ps2 = new PropertySetter(display, "cameraSetting",
				frames2);

		Animator animator = new Animator(800, ps2);
		animator.setStartDelay(0);
		animator.setAcceleration(0.4f);
		animator.setDeceleration(0.4f);

		return animator;
	}

	@Override
	public Animator getBackwardAnimation(Display display) {
		if (startShape == null)
			return null;

		CameraSetting startSetting = display.getCameraSetting();
		CameraSetting cameraEnd = ShapeFocus.getFocussedShape(startShape);

		KeyValues<CameraSetting> values2 = KeyValues.create(
				new EvaluatorCameraSetting(), startSetting,
				cameraEnd);
		KeyTimes times2 = new KeyTimes(0f, 1f);
		KeyFrames frames2 = new KeyFrames(values2, times2);
		PropertySetter ps2 = new PropertySetter(display, "cameraSetting",
				frames2);

		Animator animator = new Animator(800, ps2);
		animator.setStartDelay(0);
		animator.setAcceleration(0.4f);
		animator.setDeceleration(0.4f);

		return animator;
	}

	@Override
	public void directlyGoTo(Display display) {
		CameraSetting cameraEnd = ShapeFocus.getFocussedShape(endShape);
		display.setCameraSetting(cameraEnd);
	}
}
