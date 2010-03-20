package com.sessionfive.animation;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class AnimationController {

	private Animator currentAnimator;

	private Display display;
	private Presentation presentation;
	private AnimationStep currentAnimationStep;

	public void init(Presentation presentation, Display display) {
		this.presentation = presentation;
		this.display = display;

		this.currentAnimationStep = null;
	}

	public boolean canGoForward() {
		if (currentAnimationStep == null) {
			return presentation.getTotalAnimationStepCount() > 0;
		} else if (currentAnimationStep.isAutoZoomEnabled()
				&& currentAnimationStep.hasChild()) {
			return true;
		} else {
			AnimationStep step = currentAnimationStep;
			while (step != null && !step.hasNext() && step.hasParent()) {
				step = step.getParent();
			}
			return step != null && step.hasNext();
		}
	}

	public boolean canGoBackward() {
		return currentAnimationStep != null;
	}

	public boolean canZoomIn() {
		return currentAnimationStep != null && currentAnimationStep.hasChild();
	}

	public boolean canZoomOut() {
		return currentAnimationStep != null && currentAnimationStep.hasParent();
	}

	public void forward() {
		if (!canGoForward()) {
			return;
		}

		if (canZoomIn() && currentAnimationStep.isAutoZoomEnabled()) {
			zoomIn();
		} else {
			if (currentAnimationStep == null) {
				currentAnimationStep = presentation.getFirstAnimationStep();
			} else {
				while (!currentAnimationStep.hasNext()
						&& currentAnimationStep.hasParent()) {
					currentAnimationStep = currentAnimationStep.getParent();
				}

				if (currentAnimationStep.hasNext()) {
					currentAnimationStep = currentAnimationStep.getNext();
				}
			}

			Animator animator = currentAnimationStep
					.getForwardAnimation(display);
			startFocusAnimator(animator);
		}
	}

	public void backward() {
		if (!canGoBackward()) {
			return;
		}
		
		Shape focussedShape = getLastFocussedShape();

		if (currentAnimationStep.hasPrevious()) {
			currentAnimationStep = currentAnimationStep.getPrevious();

			if (currentAnimationStep.isAutoZoomEnabled()) {
				while (currentAnimationStep.hasChild()) {
					currentAnimationStep = currentAnimationStep.getChild();
					while (currentAnimationStep.hasNext()) {
						currentAnimationStep = currentAnimationStep.getNext();
					}
				}
			}
		} else if (currentAnimationStep.hasParent()) {
			currentAnimationStep = currentAnimationStep.getParent();
		} else {
			currentAnimationStep = null;
		}
		
		Animator animator = null;
		if (currentAnimationStep != null) {
			animator = currentAnimationStep.getBackwardAnimation(display);
		} else {
			Camera cameraStart = display.getCamera();
			Camera cameraEnd = presentation.getFocussedCamera();
			animator = presentation.getDefaultAnimation().createBackwardAnimator(
					cameraStart, cameraEnd, display, focussedShape);
		}
		startFocusAnimator(animator);
	}

	public void zoomIn() {
		if (!canZoomIn()) {
			return;
		}

		currentAnimationStep = currentAnimationStep.getChild();
		Animator animator = currentAnimationStep.getForwardAnimation(display);
		startFocusAnimator(animator);
	}

	public void zoomOut() {
		if (!canZoomOut()) {
			return;
		}

		currentAnimationStep = currentAnimationStep.getParent();
		Animator animator = currentAnimationStep.getForwardAnimation(display);
		startFocusAnimator(animator);
	}

	public Shape getLastFocussedShape() {
		return currentAnimationStep != null ? currentAnimationStep
				.getFocussedShape() : null;
	}

	public int getNumberOfKeyFrames() {
		return presentation.getTotalAnimationStepCount();
	}

	public void goToKeyframeNo(int keyframeNo) {
		if (keyframeNo < 0 || keyframeNo >= getNumberOfKeyFrames()) {
			return;
		}

		currentAnimationStep = presentation.getFirstAnimationStep();
		int counter = 0;
		while (counter < keyframeNo) {
			if (currentAnimationStep.hasChild()) {
				currentAnimationStep = currentAnimationStep.getChild();
			} else if (currentAnimationStep.hasNext()) {
				currentAnimationStep = currentAnimationStep.getNext();
			} else if (currentAnimationStep.hasParent()) {
				currentAnimationStep = currentAnimationStep.getParent();

				while (!currentAnimationStep.hasNext()
						&& currentAnimationStep.hasParent()) {
					currentAnimationStep = currentAnimationStep.getParent();
				}
				if (currentAnimationStep.hasNext()) {
					currentAnimationStep = currentAnimationStep.getNext();
				}
			}
			counter++;
		}

		Animator animator = currentAnimationStep.getForwardAnimation(display);
		startFocusAnimator(animator);
	}

	public void readjustSmoothlyTo(Shape focussedShape) {
		if (focussedShape == null) {
			reset();
		} else {

			currentAnimationStep = presentation.getFirstAnimationStep();
			while (currentAnimationStep != null
					&& currentAnimationStep.getFocussedShape() != focussedShape) {
				if (currentAnimationStep.hasChild()) {
					currentAnimationStep = currentAnimationStep.getChild();
				} else if (currentAnimationStep.hasNext()) {
					currentAnimationStep = currentAnimationStep.getNext();
				} else if (currentAnimationStep.hasParent()) {
					currentAnimationStep = currentAnimationStep.getParent();

					while (!currentAnimationStep.hasNext()
							&& currentAnimationStep.hasParent()) {
						currentAnimationStep = currentAnimationStep.getParent();
					}
					if (currentAnimationStep.hasNext()) {
						currentAnimationStep = currentAnimationStep.getNext();
					}
				}
			}

			if (currentAnimationStep != null) {
				Animator animator = currentAnimationStep
						.getForwardAnimation(display);
				startFocusAnimator(animator);
			}
		}
	}

	public void readjustDirectly() {
		if (currentAnimationStep != null) {
			currentAnimationStep.directlyGoTo(display);
		} else {
			display.setCamera(presentation.getFocussedCamera());
		}
	}

	public void reset() {
		this.currentAnimationStep = null;

		Camera cameraStart = display.getCamera();
		Camera cameraEnd = presentation.getFocussedCamera();
		Animator animator = new MoveToAnimationStyle().createBackwardAnimator(
				cameraStart, cameraEnd, display, null);
		startFocusAnimator(animator);
	}

	protected void startFocusAnimator(Animator animator) {
		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		if (currentAnimator != null) {
			currentAnimator.start();
		}
	}

	/*
	 * protected void updateSelection() { Integer level1 =
	 * this.animationState.get(0); if (level1 == -1) { Shape[] allShapes =
	 * presentation.getShapes(LayerType.CAMERA_ANIMATED).toArray(new Shape[0]);
	 * SessionFiveApplication
	 * .getInstance().getSelectionService().setSelection(allShapes); } else {
	 * Shape currentFocussedShape =
	 * presentation.getShapes(LayerType.CAMERA_ANIMATED).get(level1);
	 * SessionFiveApplication
	 * .getInstance().getSelectionService().setSelection(new Shape[]
	 * {currentFocussedShape}); }
	 * 
	 * }
	 */

}
