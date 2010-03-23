package com.sessionfive.core;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;

public class AnimationStep {

	private final Shape focussedShape;
	private AnimationStyle style;

	private AnimationStep previous;
	private AnimationStep next;
	private AnimationStep parent;
	private AnimationStep child;
	private boolean autoZoomEnabled;

	public AnimationStep(final Shape focussedShape) {
		this.focussedShape = focussedShape;
		this.style = null;
		this.autoZoomEnabled = true;
	}
	
	public AnimationStep() {
		this.focussedShape = null;
		this.style = null;
		this.autoZoomEnabled = true;
	}

	public Shape getFocussedShape() {
		return focussedShape;
	}
	
	public AnimationStyle getStyle() {
		return style;
	}
	
	public void setStyle(AnimationStyle style) {
		this.style = style;
	}

	public boolean isAutoZoomEnabled() {
		return this.autoZoomEnabled;
	}

	public void setAutoZoomEnabled(boolean autoZoomEnabled) {
		this.autoZoomEnabled = autoZoomEnabled;
	}

	public Animator getForwardAnimation(Display display) {
		if (style != null) {
			Camera cameraStart = display.getCamera();
			Camera cameraEnd = focussedShape.getFocussedCamera();
			Animator forwardAnimator = style.createForwardAnimator(cameraStart, cameraEnd, display, focussedShape);
			return forwardAnimator;
		}
		return null;
	}

	public Animator getBackwardAnimation(Display display) {
		if (this.style == null)
			return null;
		
		Camera cameraStart = display.getCamera();
		Camera cameraEnd = focussedShape.getFocussedCamera();
		return style.createBackwardAnimator(cameraStart, cameraEnd, display, focussedShape);
	}

	public void directlyGoTo(Display display) {
		Camera cameraEnd = focussedShape.getFocussedCamera();
		display.setCamera(cameraEnd);
	}

	public void addChild(AnimationStep subStep) {
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

	public void setNext(AnimationStep step) {
		this.next = step;
		step.previous = this;
	}
	
	public AnimationStep getPrevious() {
		return previous;
	}

	public AnimationStep getNext() {
		return next;
	}
	
	public AnimationStep getParent() {
		return parent;
	}

	public AnimationStep getChild() {
		return child;
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
