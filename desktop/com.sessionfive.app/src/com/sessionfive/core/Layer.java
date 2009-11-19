package com.sessionfive.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Layer {
	private List<Shape> shapes;

	public Layer() {
		shapes = new CopyOnWriteArrayList<Shape>();
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public void add(Shape shape) {
		shapes.add(shape);
	}

	public void remove(Shape shape) {
		shapes.remove(shape);
	}
}
