package com.sessionfive.core;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Presentation implements Focusable {

	private Camera startCamera;
	private List<Shape> shapes;
	private List<Animation> animations;
	
	private Color backgroundColor;
	private String layerText = "Sessionfire";

	public Presentation() {
		shapes = new CopyOnWriteArrayList<Shape>();
		animations = new CopyOnWriteArrayList<Animation>();
		startCamera = new Camera(0, 0, 0, 0, 0, 0, 0, 1, 0);
		backgroundColor = Color.BLACK;
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
	
	public void removeAllShapes() {
		shapes.clear();
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

	public void removeAllAnimations() {
		animations.clear();
	}

	public Camera getFocussedCamera() {
		return startCamera;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color newColor) {
		backgroundColor = newColor;
	}

	public String getLayerText() {
		return layerText;
	}

	public void setLayerText(String layerText) {
		this.layerText = layerText;
	}

}
