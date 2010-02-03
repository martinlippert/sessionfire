package com.sessionfive.core;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ShapeIterator implements Iterator<Shape> {

	private Stack<StackElement> treeStack;
	
	public ShapeIterator(Presentation presentation) {
		treeStack = new Stack<StackElement>();
		
		StackElement rootList = new StackElement();
		rootList.shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		rootList.index = 0;
		
		treeStack.push(rootList);
	}

	@Override
	public boolean hasNext() {
		if (treeStack.isEmpty()) return false;

		StackElement element = treeStack.peek();
		return (element.index < element.shapes.size());
	}

	@Override
	public Shape next() {
		StackElement element = treeStack.peek();
		Shape shape = element.shapes.get(element.index++);
		
		List<Shape> childs = shape.getShapes();
		if (childs.size() > 0) {
			StackElement childsElement = new StackElement();
			childsElement.shapes = childs;
			childsElement.index = 0;
			treeStack.push(childsElement);
		}
		else {
			while (element.index >= element.shapes.size() && !treeStack.isEmpty()) {
				treeStack.pop();
				if (!treeStack.isEmpty()) {
					element = treeStack.peek();
				}
			}
		}
		
		return shape;
	}

	@Override
	public void remove() {
	}
	
	protected Shape getCurrentShape() {
		StackElement element = treeStack.peek();
		Shape shape = element.shapes.get(element.index);
		return shape;
	}
	
	protected static class StackElement {
		public List<Shape> shapes;
		public int index;
	}

}
