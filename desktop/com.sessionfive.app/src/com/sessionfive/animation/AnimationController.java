package com.sessionfive.animation;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;
import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

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
		if (currentAnimationNo >= presentation.getAnimationStepCount() - 1) {
			return;
		}

		currentAnimationNo++;
		AnimationStep animation = presentation.getAnimationStep(currentAnimationNo);
		Animator animator = animation.getForwardAnimation(display);

		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		currentAnimator.start();

//		updateSelection();
	}

	public void backward() {
		if (currentAnimationNo < 0)
			return;

		AnimationStep animation = presentation.getAnimationStep(currentAnimationNo);
		Animator animator = animation.getBackwardAnimation(display);
		currentAnimationNo--;

		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		currentAnimator.start();
		
//		updateSelection();
	}

	public int getNumberOfKeyFrames() {
		return presentation.getAnimationStepCount();
	}

	public void goTo(int parsedNumber) {
		if (parsedNumber < -1 || parsedNumber >= presentation.getAnimationStepCount()
				|| parsedNumber == currentAnimationNo) {
			return;
		}
		resetTo(parsedNumber);
	}

	public void resetTo(int animationNo) {
		Animator animator = null;

		if (presentation.getAnimationStepCount() > 0) {
			currentAnimationNo = animationNo;

			AnimationStep animation = null;
			if (animationNo == -1) {
				animation = presentation.getAnimationStep(0);
				animator = animation.getBackwardAnimation(display);
			} else {
				animation = presentation.getAnimationStep(currentAnimationNo);
				animator = animation.getForwardAnimation(display);
			}
		}
		else if (animationNo == -1) {
			Camera cameraStart = display.getCamera();
			Camera cameraEnd = presentation.getFocussedCamera();
			animator = new MoveToAnimationStyle().createBackwardAnimator(cameraStart, cameraEnd, display, null);
		}
		
		if (animator != null) {
			if (currentAnimator != null && currentAnimator.isRunning()) {
				currentAnimator.stop();
			}
			currentAnimator = animator;
			currentAnimator.start();
			
//			updateSelection();
		}
	}
	
	public void directlyGoTo(int animationNo) {
		if (presentation.getAnimationStepCount() > 0) {
			currentAnimationNo = animationNo;

			if (animationNo == -1) {
				display.setCamera(presentation.getFocussedCamera());
			} else {
				AnimationStep animation = presentation.getAnimationStep(currentAnimationNo);
				animation.directlyGoTo(display);
			}
			
//			updateSelection();
		}
	}
	
	protected void updateSelection() {
		if (currentAnimationNo == -1) {
			Shape[] allShapes = presentation.getShapes(LayerType.CAMERA_ANIMATED).toArray(new Shape[0]);
			SessionFiveApplication.getInstance().getSelectionService().setSelection(allShapes);
		}
		else {
			Shape currentFocussedShape = presentation.getShapes(LayerType.CAMERA_ANIMATED).get(currentAnimationNo);
			SessionFiveApplication.getInstance().getSelectionService().setSelection(new Shape[] {currentFocussedShape});
		}
		
	}

}
