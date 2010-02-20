package com.sessionfive.animation;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.AnimationStepIterator;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class AnimationController {

	private Animator currentAnimator;

	private Display display;
	private Presentation presentation;
	
	private AnimationStepIterator animationIterator;
	
	public void init(Presentation presentation, Display display) {
		this.presentation = presentation;
		this.display = display;
		
		this.animationIterator = new AnimationStepIterator(presentation);
	}

	public boolean canGoForward() {
		return animationIterator.hasNext() || animationIterator.hasParent();
	}

	public boolean canZoomIn() {
		return animationIterator.hasChilds();
	}

	public boolean canGoBackward() {
		return animationIterator.hasPrevious() || animationIterator.current() != null;
	}

	public Shape getLastFocussedShape() {
		AnimationStep step = animationIterator.current();
		return step != null ? step.getEndShape() : null;
	}

	public void forward() {
		if (!canGoForward()) {
			return;
		}

		while (!animationIterator.hasNext() && animationIterator.hasParent()) {
			animationIterator.backToParent();
		}
		if (animationIterator.hasNext()) {
			animationIterator.next();
		}
		
		AnimationStep step = animationIterator.current();
		Animator animator = step.getForwardAnimation(display);

		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		if (currentAnimator != null) {
			currentAnimator.start();
		}

//		updateSelection();
	}

	public void backward() {
		if (!canGoBackward()) {
			return;
		}

		AnimationStep step = animationIterator.current();
		Animator animator = step.getBackwardAnimation(display);
		
		if (animationIterator.hasPrevious()) {
			animationIterator.previous();
		}
		else {
			animationIterator = new AnimationStepIterator(presentation);
		}

		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		if (currentAnimator != null) {
			currentAnimator.start();
		}
		
//		updateSelection();
	}

	public void zoomIn() {
		if (!canZoomIn()) {
			return;
		}
		
		animationIterator.intoChilds();
		AnimationStep step = animationIterator.current();
		Animator animator = step.getForwardAnimation(display);

		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		if (currentAnimator != null) {
			currentAnimator.start();
		}
	}

	public int getNumberOfKeyFrames() {
		return presentation.getAnimationStepCount();
	}
	
	public void goToKeyframeNo(int keyframeNo) {
		if (keyframeNo < 0 || keyframeNo >= getNumberOfKeyFrames()) {
			return;
		}
		
		animationIterator = new AnimationStepIterator(presentation);
		for (int i = 0; i < keyframeNo - 1; i++) {
			animationIterator.next();
		}
		
		AnimationStep animationStep = animationIterator.current();
		Animator animator = animationStep.getForwardAnimation(display);
		
		if (animator != null) {
			if (currentAnimator != null && currentAnimator.isRunning()) {
				currentAnimator.stop();
			}
			currentAnimator = animator;
			currentAnimator.start();
			
//			updateSelection();
		}
	}

	public void readjustSmoothlyTo(Shape focussedShape) {
		// TODO: implement
	}

	public void readjustDirectly() {
		AnimationStep currentStep = animationIterator.current();
		if (currentStep != null) {
			currentStep.directlyGoTo(display);
		}
		else {
			display.setCamera(presentation.getFocussedCamera());
		}
	}
	
	public void reset() {
		this.animationIterator = new AnimationStepIterator(presentation);

		Camera cameraStart = display.getCamera();
		Camera cameraEnd = presentation.getFocussedCamera();
		Animator animator = new MoveToAnimationStyle().createBackwardAnimator(cameraStart, cameraEnd, display, null);
		
		if (animator != null) {
			if (currentAnimator != null && currentAnimator.isRunning()) {
				currentAnimator.stop();
			}
			currentAnimator = animator;
			currentAnimator.start();
			
//			updateSelection();
		}
	}

/*	
	protected void updateSelection() {
		Integer level1 = this.animationState.get(0);
		if (level1 == -1) {
			Shape[] allShapes = presentation.getShapes(LayerType.CAMERA_ANIMATED).toArray(new Shape[0]);
			SessionFiveApplication.getInstance().getSelectionService().setSelection(allShapes);
		}
		else {
			Shape currentFocussedShape = presentation.getShapes(LayerType.CAMERA_ANIMATED).get(level1);
			SessionFiveApplication.getInstance().getSelectionService().setSelection(new Shape[] {currentFocussedShape});
		}
		
	}
*/

}
