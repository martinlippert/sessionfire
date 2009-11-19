package com.sessionfive.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Presentation implements Focusable, ShapeChangedListener {

	private Camera startCamera;
	private List<Animation> animations;
	private Map<LayerType, Layer> layers;
	private Color backgroundColor;
	private String layerText = "Sessionfire";
	private List<PresentationChangedListener> changeListeners;

	public Presentation() {
		animations = new CopyOnWriteArrayList<Animation>();
		startCamera = new Camera(0, 0, 0, 0, 0, 0, 0, 1, 0);
		backgroundColor = Color.BLACK;
		layers = new HashMap<LayerType, Layer>();
		layers.put(LayerType.CAMERA_ANIMATED, new Layer());
		layers.put(LayerType.FIXED_POSTION, new Layer());
		changeListeners = new LinkedList<PresentationChangedListener>();
	}

	public Camera getStartCamera() {
		return startCamera;
	}

	public void setStartCamera(Camera startCamera) {
		this.startCamera = startCamera;
		firePresentationChanged();
	}

	public void addShape(Shape shape, LayerType layer) {
		shape.addShapeChangedListener(this);
		this.layers.get(layer).add(shape);
		firePresentationChanged();
	}

	public void removeShape(Shape shape, LayerType layer) {
		shape.removeShapeChangedListener(this);
		this.layers.get(layer).remove(shape);
		firePresentationChanged();
	}

	public void removeAllShapes(LayerType layer) {
		Iterator<Shape> allShapes = this.layers.get(layer).getShapes().iterator();
		while (allShapes.hasNext()) {
			Shape shape = allShapes.next();
			shape.removeShapeChangedListener(this);
		}
		this.layers.get(layer).getShapes().clear();

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

	public List<Shape> getShapes(LayerType layertype) {
		return layers.get(layertype).getShapes();
	}

	public List<Shape> getAllShapes() {
		List<Shape> allshapes = new ArrayList<Shape>();
		for (Layer layer : this.layers.values()) {
			allshapes.addAll(layer.getShapes());
		}
		return allshapes;
	}

}
