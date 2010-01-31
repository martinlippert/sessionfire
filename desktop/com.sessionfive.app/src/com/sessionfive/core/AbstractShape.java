package com.sessionfive.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

public class AbstractShape implements Shape, ShapeChangedListener {

	private float x;
	private float y;
	private float z;
	
	private float width;
	private float height;
	private float depth;
	
	private float angleX;
	private float angleY;
	private float angleZ;
	
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
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}
	
	@Override
	public float getAbsoluteX() {
		return getX() + (this.getOwner() != null ? this.getOwner().getAbsoluteX() : 0);
	}
	
	@Override
	public float getAbsoluteY() {
		return getY() + (this.getOwner() != null ? this.getOwner().getAbsoluteY() : 0);
	}
	
	@Override
	public float getAbsoluteZ() {
		return getZ() + (this.getOwner() != null ? this.getOwner().getAbsoluteZ() : 0);
	}
	
	@Override
	public float getWidth() {
		return width;
	}
	
	@Override
	public float getHeight() {
		return height;
	}
	
	@Override
	public float getDepth() {
		return depth;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		if (this.x != x || this.y != y || this.z != z) {
			this.x = x;
			this.y = y;
			this.z = z;
			fireShapeChangedEvent();
		}
	}
	
	@Override
	public void setSize(float width, float height, float depth) {
		if (this.width != width || this.height != height || this.depth != depth) {
			this.width = width;
			this.height = height;
			this.depth = depth;
			fireShapeChangedEvent();
		}
	}

	@Override
	public float getRotationAngleX() {
		return angleX;
	}
	
	@Override
	public float getRotationAngleY() {
		return angleY;
	}
	
	@Override
	public float getRotationAngleZ() {
		return angleZ;
	}

	@Override
	public void setRotation(float angleX, float angleY, float angleZ) {
		if (this.angleX != angleX || this.angleY != angleY || this.angleZ != angleZ) {
			this.angleX = angleX;
			this.angleY = angleY;
			this.angleZ = angleZ;
			fireShapeChangedEvent();
		}
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
		float shapeX = this.getAbsoluteX();
		float shapeY = this.getAbsoluteY();
		float shapeZ = this.getAbsoluteZ();
		
		float shapeWidth = this.getWidth();
		float shapeHeight = this.getHeight();
		
		float rotationAngleX = this.getRotationAngleX();
		float rotationAngleY = this.getRotationAngleY();
		float rotationAngleZ = -this.getRotationAngleZ();
		double rotationRadianX = Math.toRadians(rotationAngleX);
		double rotationRadianY = Math.toRadians(rotationAngleY);
		double rotationRadianZ = Math.toRadians(rotationAngleZ);
		
		float lookAtX = shapeX + (shapeWidth / 2);
		float lookAtY = shapeY + (shapeHeight / 2);
		float lookAtZ = shapeZ;
		
		float locationX = 0;
		float locationY = 0;
		float locationZ = (shapeWidth > shapeHeight ? shapeWidth : shapeHeight) * focusScale;
		
		// location y-axis rotation
		locationX = (float) (locationZ * Math.sin(rotationRadianY));
		locationZ = (float) (locationZ * Math.cos(rotationRadianY));

		// location x-axis rotation
		locationY = (float) - (locationZ * Math.sin(rotationRadianX));
		locationZ = (float) (locationZ * Math.cos(rotationRadianX));
		
		locationX += lookAtX;
		locationY += lookAtY;
		
		double upX = Math.sin(rotationRadianZ);
		double upY = Math.cos(rotationRadianZ);
		double upZ = 0f;
		
		// up y-axis rotation
		double oldX = upX;
		double oldZ = upZ;
		upX = oldX * Math.cos(rotationRadianY) + oldZ * Math.sin(rotationRadianY);
		upZ = oldX * -Math.sin(rotationRadianY) + oldZ * Math.cos(rotationRadianY);

		// up x-axis rotation
		double oldY = upY;
		oldZ = upZ;
		upY = oldY * Math.cos(rotationRadianX) - oldZ * Math.sin(rotationRadianX);
		upZ = oldY * Math.sin(rotationRadianX) + oldZ * Math.cos(rotationRadianX);
		
		Camera cameraSetting = new Camera(locationX, locationY, locationZ,
				lookAtX, lookAtY, lookAtZ, (float)upX, (float)upY, (float)upZ);
		
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
