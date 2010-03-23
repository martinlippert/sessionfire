package com.sessionfive.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

public class AbstractShape implements Shape, ShapeChangedListener {

	private ShapePosition position;
	private ShapeRotation rotation;
	private ShapeSize size;

	private ShapePosition focussedPosition;
	private ShapeRotation focussedRotation;
	private ShapeSize focussedSize;

	private boolean reflectionEnabled;
	private float focusScale;

	private List<Shape> shapes;
	private Shape owner;
	private List<ShapeChangedListener> changeListeners;

	public AbstractShape() {
		super();
		this.reflectionEnabled = true;
		this.focusScale = 1.0f;
		this.changeListeners = new LinkedList<ShapeChangedListener>();
		this.shapes = new CopyOnWriteArrayList<Shape>();
		this.owner = null;

		this.position = ShapePosition.ZERO;
		this.rotation = ShapeRotation.ZERO;
		this.size = ShapeSize.ZERO;
	}

	@Override
	public ShapePosition getPosition() {
		return position;
	}

	@Override
	public ShapeRotation getRotation() {
		return rotation;
	}

	@Override
	public ShapePosition getAbsolutePosition() {
		return this.getPosition().add(
				this.getOwner() != null ? this.getOwner().getAbsolutePosition()
						: ShapePosition.ZERO);
	}

	@Override
	public ShapeSize getSize() {
		return size;
	}

	@Override
	public void setPosition(ShapePosition position) {
		if (!this.position.equals(position)) {
			this.position = position;
			fireShapeChangedEvent();
		}
	}

	@Override
	public void setSize(ShapeSize size) {
		if (!this.size.equals(size)) {
			this.size = size;
			fireShapeChangedEvent();
		}
	}

	@Override
	public void setRotation(ShapeRotation rotation) {
		if (!this.rotation.equals(rotation)) {
			this.rotation = rotation;
			fireShapeChangedEvent();
		}
	}
	
	@Override
	public ShapePosition getFocussedPosition() {
		return this.focussedPosition != null ? this.focussedPosition
				: this.position;
	}
	
	@Override
	public void setFocussedPosition(ShapePosition position) {
		this.focussedPosition = position;
	}
	
	@Override
	public ShapePosition getAbsoluteFocussedPosition() {
		return this.getFocussedPosition().add(
				this.getOwner() != null ? this.getOwner().getAbsoluteFocussedPosition()
						: ShapePosition.ZERO);
	}

	@Override
	public ShapeRotation getFocussedRotation() {
		return this.focussedRotation != null ? this.focussedRotation : this.rotation;
	}

	@Override
	public void setFocussedRotation(ShapeRotation rotation) {
		this.focussedRotation = rotation;
	}
	
	@Override
	public ShapeSize getFocussedSize() {
		return this.focussedSize != null ? this.focussedSize : this.size;
	}

	@Override
	public void setFocussedSize(ShapeSize size) {
		focussedSize = size;
	}

	@Override
	public boolean isReflectionEnabled() {
		return reflectionEnabled;
	}

	@Override
	public void setReflectionEnabled(boolean reflectionEnabled) {
		if (this.reflectionEnabled != reflectionEnabled) {
			this.reflectionEnabled = reflectionEnabled;
			fireShapeChangedEvent();
		}
	}

	public float getFocusScale() {
		return focusScale;
	}

	public void setFocusScale(float focusScale) {
		if (this.focusScale != focusScale) {
			this.focusScale = focusScale;
			fireShapeChangedEvent();
		}
	}

	@Override
	public Camera getFocussedCamera() {
		ShapePosition absolutePosition = getAbsoluteFocussedPosition();
		float shapeX = absolutePosition.getX();
		float shapeY = absolutePosition.getY();
		float shapeZ = absolutePosition.getZ();

		float shapeWidth = this.getFocussedSize().getWidth();
		float shapeHeight = this.getFocussedSize().getHeight();

		float rotationAngleX = this.getFocussedRotation().getRotationAngleX();
		float rotationAngleY = this.getFocussedRotation().getRotationAngleY();
		float rotationAngleZ = -this.getFocussedRotation().getRotationAngleZ();

		double rotationRadianX = Math.toRadians(rotationAngleX);
		double rotationRadianY = Math.toRadians(rotationAngleY);
		double rotationRadianZ = Math.toRadians(rotationAngleZ);

		float lookAtX = shapeX + (shapeWidth / 2);
		float lookAtY = shapeY + (shapeHeight / 2);
		float lookAtZ = shapeZ;

		float locationX = 0;
		float locationY = 0;
		float locationZ = (shapeWidth > shapeHeight ? shapeWidth : shapeHeight)
				* focusScale;

		// location y-axis rotation
		locationX = (float) (locationZ * Math.sin(rotationRadianY));
		locationZ = (float) (locationZ * Math.cos(rotationRadianY));

		// location x-axis rotation
		locationY = (float) -(locationZ * Math.sin(rotationRadianX));
		locationZ = (float) (locationZ * Math.cos(rotationRadianX));

		locationX += lookAtX;
		locationY += lookAtY;

		double upX = Math.sin(rotationRadianZ);
		double upY = Math.cos(rotationRadianZ);
		double upZ = 0f;

		// up y-axis rotation
		double oldX = upX;
		double oldZ = upZ;
		upX = oldX * Math.cos(rotationRadianY) + oldZ
				* Math.sin(rotationRadianY);
		upZ = oldX * -Math.sin(rotationRadianY) + oldZ
				* Math.cos(rotationRadianY);

		// up x-axis rotation
		double oldY = upY;
		oldZ = upZ;
		upY = oldY * Math.cos(rotationRadianX) - oldZ
				* Math.sin(rotationRadianX);
		upZ = oldY * Math.sin(rotationRadianX) + oldZ
				* Math.cos(rotationRadianX);

		Camera cameraSetting = new Camera(locationX, locationY, locationZ,
				lookAtX, lookAtY, lookAtZ, (float) upX, (float) upY,
				(float) upZ);

		return cameraSetting;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		this.basicDisplay(drawable);
		for (Shape shape : shapes) {
			shape.display(drawable);
		}
	}

	protected void basicDisplay(GLAutoDrawable drawable) {
	}

	@Override
	public void initialize(GLContext context) throws Exception {
		this.basicInitialize(context);
		for (Shape shape : shapes) {
			shape.initialize(context);
		}
	}

	protected void basicInitialize(GLContext context) throws Exception {
	}

	@Override
	public void release(GLContext context) throws Exception {
		this.basicRelease(context);
		for (Shape shape : shapes) {
			shape.release(context);
		}
	}

	protected void basicRelease(GLContext context) throws Exception {
	}

	@Override
	public List<Shape> getShapes() {
		return this.shapes;
	}

	@Override
	public Shape getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(Shape owner) {
		this.owner = owner;
	}

	@Override
	public void addShape(Shape shape) {
		if (shape.getOwner() != null) {
			shape.getOwner().removeShape(shape);
		}

		this.shapes.add(shape);
		shape.addShapeChangedListener(this);
		shape.setOwner(this);
		fireShapeChangedEvent();
	}

	@Override
	public void removeShape(Shape shape) {
		this.shapes.remove(shape);
		shape.setOwner(null);
		shape.removeShapeChangedListener(this);
		fireShapeChangedEvent();
	}

	@Override
	public void shapeChanged(ShapeChangedEvent event) {
		fireShapeChangedEvent();
	}

	@Override
	public void addShapeChangedListener(ShapeChangedListener listener) {
		changeListeners.add(listener);
	}

	@Override
	public void removeShapeChangedListener(ShapeChangedListener listener) {
		changeListeners.remove(listener);
	}

	protected void fireShapeChangedEvent() {
		ShapeChangedEvent event = new ShapeChangedEvent(this);
		Iterator<ShapeChangedListener> listeners = changeListeners.iterator();
		while (listeners.hasNext()) {
			listeners.next().shapeChanged(event);
		}
	}

}
