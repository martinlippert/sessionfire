package com.sessionfive.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Presentation implements Focusable {

	private Camera startCamera;
	private List<Shape> shapes;
	private List<Animation> animations;

	public Presentation() {
		shapes = new CopyOnWriteArrayList<Shape>();
		animations = new CopyOnWriteArrayList<Animation>();
		startCamera = new Camera(0, 0, 0, 0, 0, 0, 0, 1, 0);
	}
	
	public Camera getStartCamera() {
		return startCamera;
	}
	
	public void setStartCamera(Camera startCamera) {
		this.startCamera = startCamera;
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

	@Override
	public Camera getFocussedCamera() {
		return startCamera;
	}

}
