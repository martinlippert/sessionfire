package com.sessionfive.core;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;

public class AnimationStep implements AnimationStepContainer {

	private final Shape endShape;
	private final Focusable startShape;
	private AnimationStyle style;

	private AnimationStep previous;
	private AnimationStep next;
	private AnimationStep parent;
	private AnimationStep child;

	public AnimationStep(final Focusable startShape, final Shape endShape) {
		this.startShape = startShape;
		this.endShape = endShape;
		this.style = null;
	}
	
	public AnimationStep() {
		this.endShape = null;
		this.startShape = null;
		this.style = null;
	}

	public Focusable getStartShape() {
		return startShape;
	}
	
	public Shape getEndShape() {
		return endShape;
	}
	
	public AnimationStyle getStyle() {
		return style;
	}
	
	public void setStyle(AnimationStyle style) {
		this.style = style;
	}

	public Animator getForwardAnimation(Display display) {
		if (style != null) {
			Camera cameraStart = display.getCamera();
			Camera cameraEnd = endShape.getFocussedCamera();
			return style.createForwardAnimator(cameraStart, cameraEnd, display, endShape);
		}
		return null;
	}

	public Animator getBackwardAnimation(Display display) {
		if (startShape == null || this.style == null)
			return null;
		
		Camera cameraStart = display.getCamera();
		Camera cameraEnd = startShape.getFocussedCamera();
		return style.createBackwardAnimator(cameraStart, cameraEnd, display, endShape);
	}

	public void directlyGoTo(Display display) {
		Camera cameraEnd = endShape.getFocussedCamera();
		display.setCamera(cameraEnd);
	}

	public List<AnimationStep> getAnimationSteps() {
		ArrayList<AnimationStep> result = new ArrayList<AnimationStep>();
		AnimationStep childStep = child;
		while (childStep != null) {
			result.add(childStep);
			childStep = childStep.next;
		}
		return result;
	}
	
	public void addAnimationStep(AnimationStep subStep) {
		if (child == null) {
			child = subStep;
			subStep.previous = null;
		}
		else {
			AnimationStep lastChild = child;
			while (lastChild.next != null) {
				lastChild = lastChild.next;
			}
			
			lastChild.next = subStep;
			subStep.previous = lastChild;
		}
		
		subStep.setParent(this);
	}
	
	public void setParent(AnimationStep parent) {
		this.parent = parent;
	}

	public AnimationStep getPreviousStep() {
		return previous;
	}

	public AnimationStep getNextStep() {
		return next;
	}
	
	public AnimationStep getParentStep() {
		return parent;
	}

	public AnimationStep getChild() {
		return child;
	}
	
	public void setNext(AnimationStep step) {
		this.next = step;
		step.previous = this;
	}
	
	public void setChild(AnimationStep step) {
	}

	public boolean hasNext() {
		return next != null;
	}

	public boolean hasPrevious() {
		return previous != null;
	}
	
	public boolean hasChild() {
		return child != null;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

}
