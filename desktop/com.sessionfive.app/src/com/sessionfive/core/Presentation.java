package com.sessionfive.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sessionfive.animation.ZoomOutZoomInAnimationStyle;
import com.sessionfive.core.ui.AnimationPathLayouter;
import com.sessionfive.core.ui.Layouter;
import com.sessionfive.core.ui.LineLayouter;
import com.sessionfive.core.ui.LinearAnimationPathLayouter;
import com.sessionfive.shapes.TitleShape;

public class Presentation implements ShapeChangedListener {

	public static final int DEFAULT_SPACE = 25;
	public static final float DEFAULT_FOCUS_SCALE = 1.0f;

	private Camera startCamera;
	private Map<LayerType, Layer> layers;
	private AnimationStep firstAnimationStep;
	private Color backgroundColor;
	private TitleShape titleShape;
	private String path;

	private Layouter defaultLayouter;
	private AnimationStyle defaultAnimation;
	private AnimationPathLayouter defaultPath;
	private boolean defaultReflectionEnabled;
	private float defaultFocusScale;

	private List<PresentationChangedListener> changeListeners;
	private float space;
	private Camera defaultStartCamera;

	public Presentation() {
		firstAnimationStep = null;
		startCamera = new Camera(0, 0, 0, 0, 0, 0, 0, 1, 0);
		defaultStartCamera = new Camera(0, 0, 0, 0, 0, 0, 0, 1, 0);
		backgroundColor = Color.BLACK;
		layers = new HashMap<LayerType, Layer>();
		layers.put(LayerType.CAMERA_ANIMATED, new Layer());
		layers.put(LayerType.FIXED_POSTION, new Layer());
		changeListeners = new LinkedList<PresentationChangedListener>();
		titleShape = new TitleShape();
		defaultLayouter = new LineLayouter();
		defaultAnimation = new ZoomOutZoomInAnimationStyle();
		defaultPath = new LinearAnimationPathLayouter();
		defaultReflectionEnabled = true;
		path = "";

		defaultFocusScale = DEFAULT_FOCUS_SCALE;
		space = DEFAULT_SPACE;

		addShape(titleShape, LayerType.FIXED_POSTION);
	}

	public Camera getStartCamera() {
		return startCamera;
	}

	public void setStartCamera(Camera startCamera) {
		if (!this.startCamera.equals(startCamera)) {
			this.startCamera = startCamera;
			firePresentationChanged();
		}
	}

	public void setDefaultStartCamera(Camera defaultStartCamera) {
		if (this.startCamera == null
				|| this.startCamera.equals(this.defaultStartCamera)) {
			setStartCamera(defaultStartCamera);
		}
		this.defaultStartCamera = defaultStartCamera;
	}

	public void resetStartCamera() {
		if (defaultStartCamera != null) {
			setStartCamera(defaultStartCamera);
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
		Iterator<Shape> allShapes = this.layers.get(layer).getShapes()
				.iterator();
		while (allShapes.hasNext()) {
			Shape shape = allShapes.next();
			shape.removeShapeChangedListener(this);
		}
		this.layers.get(layer).getShapes().clear();

		firePresentationChanged();
	}

	public void addAnimationStep(AnimationStep animation) {
		if (this.firstAnimationStep == null) {
			this.firstAnimationStep = animation;
			animation.setParent(null);
		} else {
			AnimationStep lastStep = this.firstAnimationStep;
			while (lastStep.getNext() != null) {
				lastStep = lastStep.getNext();
			}
			lastStep.setNext(animation);
		}
	}

	public void removeAllAnimationSteps() {
		firstAnimationStep = null;
	}

	public int getTotalAnimationStepCount() {
		return getTotalAnimationStepCountRecursively(this.firstAnimationStep);
	}

	private int getTotalAnimationStepCountRecursively(
			AnimationStep animationStep) {
		int result = 0;
		while (animationStep != null) {
			result++;
			result += getTotalAnimationStepCountRecursively(animationStep
					.getChild());
			animationStep = animationStep.getNext();
		}
		return result;
	}

	public AnimationStep getFirstAnimationStep() {
		return firstAnimationStep;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color newColor) {
		if (!this.backgroundColor.equals(newColor)) {
			backgroundColor = newColor;
			firePresentationChanged();
		}
	}

	public String getLayerText() {
		return titleShape.getText();
	}

	public void setLayerText(String layerText) {
		if (!titleShape.getText().equals(layerText)) {
			titleShape.setText(layerText);
			firePresentationChanged();
		}
	}

	@Override
	public void shapeChanged(ShapeChangedEvent event) {
		firePresentationChanged();
	}

	public void addPresentationChangedListener(
			PresentationChangedListener listener) {
		changeListeners.add(listener);
	}

	public void removePresentationChangedListener(
			PresentationChangedListener listener) {
		changeListeners.remove(listener);
	}

	protected void firePresentationChanged() {
		PresentationChangedEvent event = new PresentationChangedEvent(this);

		Iterator<PresentationChangedListener> listeners = changeListeners
				.iterator();
		while (listeners.hasNext()) {
			listeners.next().presentationChanged(event);
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if (!this.path.equals(path)) {
			this.path = path;
			firePresentationChanged();
		}
	}

	public AnimationStyle getDefaultAnimation() {
		return defaultAnimation;
	}

	public void setDefaultAnimation(AnimationStyle defaultAnimation) {
		if (!this.defaultAnimation.equals(defaultAnimation)) {
			this.defaultAnimation = defaultAnimation;
			firePresentationChanged();
		}
	}

	public AnimationPathLayouter getDefaultAnimationPathLayouter() {
		return defaultPath;
	}

	public void setDefaultAnimationPathLayouter(
			AnimationPathLayouter defaultAnimationPathLayouter) {
		if (!this.defaultPath.equals(defaultAnimationPathLayouter)) {
			this.defaultPath = defaultAnimationPathLayouter;
			firePresentationChanged();
		}
	}

	public Layouter getDefaultLayouter() {
		return defaultLayouter;
	}

	public void setDefaultLayouter(Layouter defaultLayouter) {
		if (!this.defaultLayouter.equals(defaultLayouter)) {
			this.defaultLayouter = defaultLayouter;
			firePresentationChanged();
		}
	}

	public float getSpace() {
		return space;
	}

	public void setSpace(float space, Layouter layouter) {
		if (this.space != space) {
			this.space = space;
			layouter.layout(this);
			firePresentationChanged();
		}
	}

	public boolean isDefaultReflectionEnabled() {
		return defaultReflectionEnabled;
	}

	public void setDefaultReflectionEnabled(boolean defaultReflectionEnabled) {
		if (this.defaultReflectionEnabled != defaultReflectionEnabled) {
			this.defaultReflectionEnabled = defaultReflectionEnabled;
			firePresentationChanged();
		}
	}

	public float getDefaultFocusScale() {
		return defaultFocusScale;
	}

	public void setDefaultFocusScale(float defaultFocusScale) {
		if (this.defaultFocusScale != defaultFocusScale) {
			this.defaultFocusScale = defaultFocusScale;
			firePresentationChanged();
		}
	}

	public Iterator<Shape> shapeIterator(boolean skipAbstractShapes) {
		return skipAbstractShapes ? new SkippingShapeIterator(this)
				: new ShapeIterator(this);
	}

}
