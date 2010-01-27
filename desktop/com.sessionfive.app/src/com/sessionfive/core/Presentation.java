package com.sessionfive.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sessionfive.core.ui.AnimationFactory;
import com.sessionfive.core.ui.GroupLineLayouter;
import com.sessionfive.core.ui.Layouter;
import com.sessionfive.core.ui.LineLayouter;
import com.sessionfive.core.ui.MoveToAnimationFactory;
import com.sessionfive.core.ui.ZoomInZoomOutAnimationFactory;
import com.sessionfive.shapes.TitleShape;

public class Presentation implements Focusable, ShapeChangedListener {

	public static final int DEFAULT_SPACE = 25;
	public static final float DEFAULT_FOCUS_SCALE = 1.0f;

	private Camera startCamera;
	private List<Animation> animations;
	private Map<LayerType, Layer> layers;
	private Color backgroundColor;
	private TitleShape titleShape;
	private String path;

	private Layouter defaultLayouter;
	private AnimationFactory defaultAnimation;
	private boolean defaultReflectionEnabled;
	private float defaultRotationX;
	private float defaultRotationY;
	private float defaultRotationZ;
	private float defaultFocusScale;

	private List<PresentationChangedListener> changeListeners;
	private float space;
	private Camera defaultStartCamera;

	public Presentation() {
		animations = new CopyOnWriteArrayList<Animation>();
		startCamera = new Camera(0, 0, 0, 0, 0, 0, 0, 1, 0);
		backgroundColor = Color.BLACK;
		layers = new HashMap<LayerType, Layer>();
		layers.put(LayerType.CAMERA_ANIMATED, new Layer());
		layers.put(LayerType.FIXED_POSTION, new Layer());
		changeListeners = new LinkedList<PresentationChangedListener>();
		titleShape = new TitleShape();
		defaultLayouter = new GroupLineLayouter();
		defaultAnimation = new MoveToAnimationFactory();
		defaultReflectionEnabled = true;
		path = "";

		defaultRotationX = 0.0f;
		defaultRotationY = 0.0f;
		defaultRotationZ = 0.0f;
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
		this.defaultStartCamera = defaultStartCamera;
		if (this.startCamera == null) {
			this.startCamera = defaultStartCamera;
		}
	}

	public void resetStartCamera() {
		if (defaultStartCamera != null) {
			this.startCamera = defaultStartCamera;
			firePresentationChanged();
		}
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

	public void addAnimation(Animation... animation) {
		animations.addAll(Arrays.asList(animation));
	}

	public void removeAnimation(Animation animation) {
		animations.remove(animation);
	}

	public void removeAllAnimations() {
		animations.clear();
	}

	@Override
	public Camera getFocussedCamera() {
		return startCamera;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if (!this.path.equals(path)) {
			this.path = path;
			firePresentationChanged();
		}
	}

	public AnimationFactory getDefaultAnimation() {
		return defaultAnimation;
	}

	public void setDefaultAnimation(AnimationFactory defaultAnimation) {
		if (!this.defaultAnimation.equals(defaultAnimation)) {
			this.defaultAnimation = defaultAnimation;
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

	public float getDefaultRotationX() {
		return defaultRotationX;
	}

	public float getDefaultRotationY() {
		return defaultRotationY;
	}

	public float getDefaultRotationZ() {
		return defaultRotationZ;
	}

	public void setDefaultRotation(float defaultRotationX, float defaultRotationY,
			float defaultRotationZ) {
		if (this.defaultRotationX != defaultRotationX || this.defaultRotationY != defaultRotationY
				|| this.defaultRotationZ != defaultRotationZ) {
			this.defaultRotationX = defaultRotationX;
			this.defaultRotationY = defaultRotationY;
			this.defaultRotationZ = defaultRotationZ;
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

}
