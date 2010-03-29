package com.sessionfive.animation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import com.sessionfive.app.Display;
import com.sessionfive.core.AnimationStep;
import com.sessionfive.core.Camera;
import com.sessionfive.core.CameraAnimator;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFocusListener;

public class AnimationController {

	private AnimationCoordinator coordinator;

	private Display display;
	private Presentation presentation;
	private AnimationStep currentAnimationStep;

	private Set<ShapeFocusListener> focusListeners;
	
	public AnimationController() {
		this.focusListeners = new HashSet<ShapeFocusListener>();
		this.coordinator = new AnimationCoordinator();
	}
	
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
			Shape lastFocussedShape = currentAnimationStep != null ? currentAnimationStep.getFocussedShape() : null;
			List<Shape> groupsOfShapesLeft = new ArrayList<Shape>();
			
			if (currentAnimationStep == null) {
				currentAnimationStep = presentation.getFirstAnimationStep();
			} else {
				lastFocussedShape = currentAnimationStep.getFocussedShape();

				while (!currentAnimationStep.hasNext()
						&& currentAnimationStep.hasParent()) {
					groupsOfShapesLeft.add(currentAnimationStep.getFocussedShape());
					currentAnimationStep = currentAnimationStep.getParent();
				}

				if (currentAnimationStep.hasNext()) {
					currentAnimationStep = currentAnimationStep.getNext();
				}
			}

			Shape nextFocussedShape = currentAnimationStep != null ? currentAnimationStep.getFocussedShape() : null;
			CameraAnimator animator = currentAnimationStep
					.getForwardAnimation(display);
			startFocusAnimator(lastFocussedShape, nextFocussedShape, groupsOfShapesLeft, animator);
		}
	}

	public void backward() {
		if (!canGoBackward()) {
			return;
		}
		
		Shape focussedShape = getLastFocussedShape();
		List<Shape> groupsOfShapesLeft = new ArrayList<Shape>();

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
			groupsOfShapesLeft.add(currentAnimationStep.getFocussedShape());
			currentAnimationStep = currentAnimationStep.getParent();
		} else {
			currentAnimationStep = null;
		}
		
		CameraAnimator animator = null;
		if (currentAnimationStep != null) {
			animator = currentAnimationStep.getBackwardAnimation(display);
		} else {
			Camera cameraStart = display.getCamera();
			Camera cameraEnd = presentation.getStartCamera();
			animator = presentation.getDefaultAnimationStyle().createBackwardAnimator(
					cameraStart, cameraEnd, display, focussedShape);
		}
		Shape nextFocussedShape = currentAnimationStep != null ? currentAnimationStep.getFocussedShape() : null;
		startFocusAnimator(focussedShape, nextFocussedShape, groupsOfShapesLeft, animator);
	}

	public void zoomIn() {
		if (!canZoomIn()) {
			return;
		}

		Shape lastFocussedShape = currentAnimationStep != null ? currentAnimationStep.getFocussedShape() : null;
		currentAnimationStep = currentAnimationStep.getChild();
		CameraAnimator animator = currentAnimationStep.getForwardAnimation(display);
		Shape nextFocussedShape = currentAnimationStep != null ? currentAnimationStep.getFocussedShape() : null;
		startFocusAnimator(lastFocussedShape, nextFocussedShape, null, animator);
	}

	public void zoomOut() {
		if (!canZoomOut()) {
			return;
		}

		Shape lastFocussedShape = currentAnimationStep != null ? currentAnimationStep.getFocussedShape() : null;
		currentAnimationStep = currentAnimationStep.getParent();
		CameraAnimator animator = currentAnimationStep.getForwardAnimation(display);
		Shape nextFocussedShape = currentAnimationStep != null ? currentAnimationStep.getFocussedShape() : null;

		List<Shape> groupsOfShapesLeft = new ArrayList<Shape>();
		groupsOfShapesLeft.add(lastFocussedShape);

		startFocusAnimator(lastFocussedShape, nextFocussedShape, groupsOfShapesLeft, animator);
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

		CameraAnimator animator = currentAnimationStep.getForwardAnimation(display);
		startFocusAnimator(null, null, null, animator);
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
				CameraAnimator animator = currentAnimationStep
						.getForwardAnimation(display);
				startFocusAnimator(null, null, null, animator);
			}
		}
	}

	public void readjustDirectly() {
		if (currentAnimationStep != null) {
			currentAnimationStep.directlyGoTo(display);
		} else {
			display.setCamera(presentation.getStartCamera());
		}
	}

	public void reset() {
		this.currentAnimationStep = null;

		Camera cameraStart = display.getCamera();
		Camera cameraEnd = presentation.getStartCamera();
		CameraAnimator animator = new MoveToAnimationStyle().createBackwardAnimator(
				cameraStart, cameraEnd, display, null);
		startFocusAnimator(null, null, null, animator);
	}
	
	public boolean isAnimationRunning() {
		return coordinator.isAnimationRunning();
	}

	protected void startFocusAnimator(final Shape lastFocussed, final Shape nextFocussed, List<Shape> groupsLeft, final CameraAnimator animator) {
		List<TimingTarget> cancelTargets = null;
		List<TimingTarget> shapeLeftTargets = null;
		List<TimingTarget> groupLeftTargets = null;
		List<TimingTarget> startFocusTargets = null;
		
		if (coordinator.isAnimationRunning()) {
			coordinator.stopCamera();
			if (lastFocussed != null) {
				cancelTargets = fireCanceledFocussingShape(lastFocussed);
			}
		}
		else if (lastFocussed != null) {
			shapeLeftTargets = fireShapeLeft(lastFocussed);
		}
		
		if (groupsLeft != null) {
			for (Shape shape : groupsLeft) {
				List<TimingTarget> groupLeftTarget = fireGroupLeft(shape);
				if (groupLeftTarget != null && groupLeftTarget.size() > 0) {
					if (groupLeftTargets == null) {
						groupLeftTargets = new ArrayList<TimingTarget>();
					}
					groupLeftTargets.addAll(groupLeftTarget);
				}
			}
		}

		if (animator != null) {
			
			if (nextFocussed != null) {
				startFocusTargets = fireStartsFocussingShape(nextFocussed);
			}
			
			List<TimingTarget> additionalTargets = new ArrayList<TimingTarget>();
			if (cancelTargets != null) additionalTargets.addAll(cancelTargets);
			if (shapeLeftTargets != null) additionalTargets.addAll(shapeLeftTargets);
			if (groupLeftTargets != null) additionalTargets.addAll(groupLeftTargets);
			if (startFocusTargets != null) additionalTargets.addAll(startFocusTargets);
			
			animator.addCameraTarget(new TimingTargetAdapter() {
				@Override
				public void end() {
					if (nextFocussed != null) {
						fireFinishedFocussingShape(nextFocussed);
					}
				}
			});
			
			coordinator.startAnimation(animator, additionalTargets);
		}
	}
	
	public void addFocusListener(ShapeFocusListener action) {
		focusListeners.add(action);
	}

	public void removeFocusListener(ShapeFocusListener action) {
		focusListeners.remove(action);
	}
	
	private List<TimingTarget> fireStartsFocussingShape(Shape shape) {
		List<TimingTarget> result = null;
		for (ShapeFocusListener listener : this.focusListeners) {
			TimingTarget[] timingTarget = listener.startsFocussing(shape);
			result = acculumateTimingTarget(result, timingTarget);
		}
		return result;
	}
	
	private List<TimingTarget> fireCanceledFocussingShape(Shape shape) {
		List<TimingTarget> result = null;
		for (ShapeFocusListener listener : this.focusListeners) {
			TimingTarget[] timingTarget = listener.canceledFocussing(shape);
			result = acculumateTimingTarget(result, timingTarget);
		}
		return result;
	}
	
	private List<TimingTarget> fireFinishedFocussingShape(Shape shape) {
		List<TimingTarget> result = null;
		for (ShapeFocusListener listener : this.focusListeners) {
			TimingTarget[] timingTarget = listener.finishedFocussing(shape);
			result = acculumateTimingTarget(result, timingTarget);
		}
		return result;
	}
	
	private List<TimingTarget> fireShapeLeft(Shape shape) {
		List<TimingTarget> result = null;
		for (ShapeFocusListener listener : this.focusListeners) {
			TimingTarget[] timingTarget = listener.shapeLeft(shape);
			result = acculumateTimingTarget(result, timingTarget);
		}
		return result;
	}
	
	private List<TimingTarget> fireGroupLeft(Shape shape) {
		List<TimingTarget> result = null;
		for (ShapeFocusListener listener : this.focusListeners) {
			TimingTarget[] timingTarget = listener.groupOfShapeLeft(shape);
			result = acculumateTimingTarget(result, timingTarget);
		}
		return result;
	}
	
	private List<TimingTarget> acculumateTimingTarget(
			List<TimingTarget> result, TimingTarget[] timingTargets) {
		if (timingTargets != null && timingTargets.length > 0) {
			if (result == null) {
				result = new ArrayList<TimingTarget>();
			}
			
			for (TimingTarget target : timingTargets) {
				result.add(target);
			}
		}
		return result;
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
