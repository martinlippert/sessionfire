package com.sessionfive.core;

import java.awt.Color;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.sessionfive.app.Display;
import com.sessionfive.app.EvaluatorColorSetting;

public class FadeInAnimation {

	public void startAnimation(Display display) {
		KeyValues<Color> values = KeyValues.create(new EvaluatorColorSetting(), Color.DARK_GRAY,
				null);
		KeyTimes times = new KeyTimes(0f, 1f);
		KeyFrames frames = new KeyFrames(values, times);
		PropertySetter ps = new PropertySetter(display, "color", frames);

		Animator animator = new Animator(800, ps);
		animator.setStartDelay(0);
		animator.setAcceleration(0.4f);
		animator.setDeceleration(0.4f);
		animator.start();
	}
}
