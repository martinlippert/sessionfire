package com.sessionfire.twitter.core;

import java.awt.Color;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.Evaluator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;


public class FadeAnimation {

	public void doFadeOutAnimation(TwitterShape textshape, final Runnable optionalEndCallback) {
		doAnimation(textshape, optionalEndCallback, new EvaluatorColorFadeout());
	}

	public void doFadeInAnimation(TwitterShape textshape, final Runnable optionalEndCallback) {
		doAnimation(textshape, optionalEndCallback, new EvaluatorColorFadein());
	}

	private void doAnimation(TwitterShape textshape, final Runnable endCallback,
			Evaluator<Color> evaluator) {
		KeyValues<Color> values = KeyValues.create(evaluator, Color.DARK_GRAY, Color.DARK_GRAY);
		KeyTimes times = new KeyTimes(0f, 1f);
		KeyFrames frames = new KeyFrames(values, times);
		PropertySetter ps = new PropertySetter(textshape, "color", frames);

		Animator animator = new Animator(1500, ps);
		// animator.setStartDirection(Direction.FORWARD);
		animator.setStartDelay(0);
		animator.setAcceleration(0.4f);
		animator.setDeceleration(0.4f);
		animator.setRepeatCount(1);

		animator.start();
		animator.addTarget(new TimingTargetAdapter() {
			@Override
			public void end() {
				if (endCallback != null)
					endCallback.run();
			}
		});
	}

	class EvaluatorColorFadeout extends Evaluator<Color> {
		public Color evaluate(Color c1, Color c2, float fraction) {
			float r = ((float) c1.getRed()) / 255;
			float g = (float) c1.getGreen() / 255;
			float b = (float) c1.getBlue() / 255;
			return new Color(r, g, b, 1 - fraction);
		}
	}

	class EvaluatorColorFadein extends Evaluator<Color> {
		public Color evaluate(Color c1, Color c2, float fraction) {
			float r = ((float) c1.getRed()) / 255;
			float g = (float) c1.getGreen() / 255;
			float b = (float) c1.getBlue() / 255;
			return new Color(r, g, b, fraction);
		}
	}

}
