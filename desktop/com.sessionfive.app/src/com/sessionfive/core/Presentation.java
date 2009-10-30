package com.sessionfive.core;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Presentation implements Focusable, ShapeChangedListener {

	private Camera startCamera;
	private List<Shape> shapes;
	private List<Animation> animations;
	
	private Color backgroundColor;
	private String layerText = "Sessionfire";
	private List<PresentationChangedListener> changeListeners;

	public Presentation() {
		shapes = new CopyOnWriteArrayList<Shape>();
		animations = new CopyOnWriteArrayList<Animation>();
		startCamera = new Camera(0, 0, 0, 0, 0, 0, 0, 1, 0);
		backgroundColor = Color.BLACK;
		
		changeListeners = new LinkedList<PresentationChangedListener>();
	}
	
	public Camera getStartCamera() {
		return startCamera;
	}
	
	public void setStartCamera(Camera startCamera) {
		this.startCamera = startCamera;
		
		firePresentationChanged();
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}

	public void addShape(Shape shape) {
		shape.addShapeChangedListener(this);
		shapes.add(shape);
		
		firePresentationChanged();
	}
	
	public void removeShape(Shape shape) {
		shape.removeShapeChangedListener(this);
		shapes.remove(shape);
		
		firePresentationChanged();
	}
	
	public void removeAllShapes() {
		Iterator<Shape> allShapes = shapes.iterator();
		while (allShapes.hasNext()) {
			allShapes.next().removeShapeChangedListener(this);
		}
		shapes.clear();
		
		firePresentationChanged();
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
		
		firePresentationChanged();
	}

	public String getLayerText() {
		return layerText;
	}

	public void setLayerText(String layerText) {
		this.layerText = layerText;
		
		firePresentationChanged();
	}
	
	

	public void shapeChanged(ShapeChangedEvent event) {
		firePresentationChanged();
	}
	
	public void addPresentationChangedListener(PresentationChangedListener listener) {
		changeListeners.add(listener);
	}
	
	public void removePresentationChangedListener(PresentationChangedListener listener) {
		changeListeners.remove(listener);
	}
	
	protected void firePresentationChanged() {
		PresentationChangedEvent event = new PresentationChangedEvent(this);
		
		Iterator<PresentationChangedListener> listeners = changeListeners.iterator();
		while (listeners.hasNext()) {
			listeners.next().presentationChanged(event);
		}
	}

}
