package com.sessionfive.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFocusListener;
import com.sessionfive.core.ShapePosition;
import com.sessionfive.core.ShapeRotation;
import com.sessionfive.core.ShapeSize;

public class ExplodingGroupListener implements ShapeFocusListener {

	@Override
	public TimingTarget cancelFocussing(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget finishedFocussing(Shape shape) {
		System.out.println("focussing finished!!! " + shape + " - " + this);
		return null;
	}

	@Override
	public TimingTarget startsFocussing(Shape shape) {
		System.out.println("focussing started!!! " + shape);

		ShapePosition position = shape.getPosition();
		ShapePosition focussedPosition = shape.getFocussedPosition();
		if (position.equals(focussedPosition)) {
			return null;
		}
		
		List<Shape> shapes = new ArrayList<Shape>();
		List<ShapePosition> startPositions = new ArrayList<ShapePosition>();
		List<ShapePosition> endPositions = new ArrayList<ShapePosition>();
		List<ShapeRotation> startRotations = new ArrayList<ShapeRotation>();
		List<ShapeRotation> endRotations = new ArrayList<ShapeRotation>();
		List<ShapeSize> startSizes = new ArrayList<ShapeSize>();
		List<ShapeSize> endSizes = new ArrayList<ShapeSize>();
		
		Shape parent = shape.getOwner();
		if (parent != null) {
			for (Shape child : parent.getShapes()) {
				shapes.add(child);
				startPositions.add(child.getPosition());
				endPositions.add(child.getFocussedPosition());
				startRotations.add(child.getRotation());
				endRotations.add(child.getFocussedRotation());
				startSizes.add(child.getSize());
				endSizes.add(child.getFocussedSize());
			}
		}
		
		ExplodingGroup start = new ExplodingGroup(shapes, startPositions, startRotations, startSizes);
		ExplodingGroup end = new ExplodingGroup(shapes, endPositions, endRotations, endSizes);
		
		KeyValues<ExplodingGroup> values = KeyValues.create(
				new ExplodingGroupEvaluator(), start, end);
		KeyTimes times = new KeyTimes(0f, 1f);
		KeyFrames frames = new KeyFrames(values, times);
		PropertySetter ps = new PropertySetter(this, "explodingGroup", frames);
		
		return ps;
	}
	
	public void setExplodingGroup(ExplodingGroup group) {
		List<Shape> shapes = group.getShapes();
		List<ShapePosition> positions = group.getPositions();
		List<ShapeRotation> rotations = group.getRotations();
		List<ShapeSize> sizes = group.getSizes();
		
		for(int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			shape.setPosition(positions.get(i));
			shape.setRotation(rotations.get(i));
			shape.setSize(sizes.get(i));
		}
	}

}
