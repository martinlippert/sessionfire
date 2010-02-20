package com.sessionfive.core;

import java.util.Stack;

public class AnimationStepIterator {
	
	private Stack<StackElement> treeStack;
	
	public AnimationStepIterator(AnimationStepContainer container) {
		this.treeStack = new Stack<StackElement>();

		StackElement baseElement = new StackElement();
		baseElement.container = container;
		baseElement.current = -1;
		this.treeStack.push(baseElement);
	}

	public boolean hasNext() {
		StackElement element = this.treeStack.peek();
		return element.current < element.container.getAnimationSteps().size() - 1;
	}

	public boolean hasPrevious() {
		StackElement element = this.treeStack.peek();
		return element.current > 0;
	}

	public void next() {
		if (hasNext()) {
			this.treeStack.peek().current++;
		}
	}

	public void previous() {
		if (hasPrevious()) {
			this.treeStack.peek().current--;
		}
	}
	
	public void previousIncludingChilds() {
		if (hasPrevious()) {
			previous();
			
			while (hasChilds()) {
				intoChilds();
				while (hasNext()) {
					next();
				}
			}
		}
	}
	
	public boolean hasChilds() {
		AnimationStep step = current();
		return step != null && step.getAnimationSteps().size() > 0;
	}
	
	public boolean hasParent() {
		return this.treeStack.size() > 1;
	}
	
	public void intoChilds() {
		if (hasChilds()) {
			AnimationStep step = current();
			
			StackElement baseElement = new StackElement();
			baseElement.container = step;
			baseElement.current = 0;
			this.treeStack.push(baseElement);
		}
	}
	
	public void backToParent() {
		if (hasParent()) {
			this.treeStack.pop();
		}
	}
	
	public AnimationStep current() {
		StackElement element = this.treeStack.peek();
		if (element.current > -1) {
			return element.container.getAnimationSteps().get(element.current);
		}
		else {
			return null;
		}
	}
	
	protected static class StackElement {
		public AnimationStepContainer container;
		public int current;
	}

}
