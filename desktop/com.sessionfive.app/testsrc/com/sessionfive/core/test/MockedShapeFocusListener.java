package com.sessionfive.core.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jdesktop.animation.timing.TimingTarget;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFocusListener;

public class MockedShapeFocusListener extends Assert implements ShapeFocusListener {
	
	private List<Shape> canceled = new ArrayList<Shape>();
	private List<Shape> startsFocus = new ArrayList<Shape>();
	private List<Shape> finishedFocus = new ArrayList<Shape>();
	private List<Shape> shapeLeft = new ArrayList<Shape>();
	private List<Shape> groupOfShapeLeft = new ArrayList<Shape>();
	
	@Override
	public TimingTarget[] canceledFocussing(Shape shape) {
		canceled.add(shape);
		return null;
	}

	@Override
	public TimingTarget[] finishedFocussing(Shape shape) {
		finishedFocus.add(shape);
		return null;
	}

	@Override
	public TimingTarget[] groupOfShapeLeft(Shape shape) {
		groupOfShapeLeft.add(shape);
		return null;
	}

	@Override
	public TimingTarget[] shapeLeft(Shape shape) {
		shapeLeft.add(shape);
		return null;
	}

	@Override
	public TimingTarget[] startsFocussing(Shape shape) {
		startsFocus.add(shape);
		return null;
	}
	
	public void reset() {
		canceled.clear();
		startsFocus.clear();
		finishedFocus.clear();
		shapeLeft.clear();
		groupOfShapeLeft.clear();
	}
	
	public void assertCanceled(Shape... shapes) {
		assertEquals(shapes.length, canceled.size());
		for(int i = 0; i < shapes.length; i++) {
			assertSame(shapes[i], canceled.get(i));
		}
	}
	
	public void assertStarts(Shape... shapes) {
		assertEquals(shapes.length, startsFocus.size());
		for(int i = 0; i < shapes.length; i++) {
			assertSame(shapes[i], startsFocus.get(i));
		}
	}
	
	public void assertFinished(Shape... shapes) {
		assertEquals(shapes.length, finishedFocus.size());
		for(int i = 0; i < shapes.length; i++) {
			assertSame(shapes[i], finishedFocus.get(i));
		}
	}
	
	public void assertShapeLeft(Shape... shapes) {
		assertEquals(shapes.length, shapeLeft.size());
		for(int i = 0; i < shapes.length; i++) {
			assertSame(shapes[i], shapeLeft.get(i));
		}
	}
	
	public void assertGroupOfShapeLeft(Shape... shapes) {
		assertEquals(shapes.length, groupOfShapeLeft.size());
		for(int i = 0; i < shapes.length; i++) {
			assertSame(shapes[i], groupOfShapeLeft.get(i));
		}
	}
	
	protected void waitForAnimationToFinish(AnimationController controller) throws Exception {
		int timeoutTimer = 0;
		while (controller.isAnimationRunning() && timeoutTimer < 1000) {
			Thread.sleep(2);
			timeoutTimer++;
		}
	}
	
}
