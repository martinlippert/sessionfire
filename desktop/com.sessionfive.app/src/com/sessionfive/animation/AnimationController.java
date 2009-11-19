package com.sessionfive.animation;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Presentation;

public class AnimationController {

	private Animator currentAnimator;
	private int currentAnimationNo;

	private Display display;
	private Presentation presentation;

	public void init(Presentation presentation, Display display) {
		this.presentation = presentation;
		this.display = display;
		this.currentAnimationNo = -1;
	}

	public int getCurrentAnimationNo() {
		return currentAnimationNo;
	}

	public void forward() {
		if (currentAnimationNo >= presentation.getAnimationCount() - 1) {
			return;
		}

		currentAnimationNo++;
		Animation animation = presentation.getAnimation(currentAnimationNo);
		Animator animator = animation.getForwardAnimation(display);

		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		currentAnimator.start();
	}

	public void backward() {
		if (currentAnimationNo < 0)
			return;

		Animation animation = presentation.getAnimation(currentAnimationNo);
		Animator animator = animation.getBackwardAnimation(display);
		currentAnimationNo--;

		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		currentAnimator.start();
	}

	public int getNumberOfKeyFrames() {
		return presentation.getAnimationCount();
	}

	public void goTo(int parsedNumber) {
		if (parsedNumber < -1 || parsedNumber >= presentation.getAnimationCount()
				|| parsedNumber == currentAnimationNo) {
			return;
		}
		resetTo(parsedNumber);
	}

	public void resetTo(int animationNo) {
		if (presentation.getAnimationCount() > 0) {
			currentAnimationNo = animationNo;

			Animation animation = null;
			Animator animator = null;

			if (animationNo == -1) {
				animation = presentation.getAnimation(0);
				animator = animation.getBackwardAnimation(display);
			} else {
				animation = presentation.getAnimation(currentAnimationNo);
				animator = animation.getForwardAnimation(display);
			}

			if (currentAnimator != null && currentAnimator.isRunning()) {
				currentAnimator.stop();
			}

			currentAnimator = animator;
			currentAnimator.start();
		}
	}


}
