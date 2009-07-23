package com.sessionfive.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Presentation {

	private List<Shape> shapes;
	private List<Animation> animations;

	public Presentation() {
		shapes = new CopyOnWriteArrayList<Shape>();
		animations = new CopyOnWriteArrayList<Animation>();
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	
	public void removeShape(Shape shape) {
		shapes.remove(shape);
	}
	
	public int getAnimationCount() {
		return animations.size();
	}
	
	public Animation getAnimation(int index) {
		return animations.get(index);
	}
	
	public void addAnimation(Animation animation) {
		animations.add(animation);
	}
	
	public void removeAnimation(Animation animation) {
		animations.remove(animation);
	}

	public void reset() {
		for (Shape shape : shapes) {
			shape.reset();
		}
	}

}
