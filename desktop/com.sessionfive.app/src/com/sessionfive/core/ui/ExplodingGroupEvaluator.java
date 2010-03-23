package com.sessionfive.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.animation.timing.interpolation.Evaluator;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;
import com.sessionfive.core.ShapeRotation;
import com.sessionfive.core.ShapeSize;

public class ExplodingGroupEvaluator extends Evaluator<ExplodingGroup> {

	@Override
	public ExplodingGroup evaluate(ExplodingGroup start, ExplodingGroup end,
			float fraction) {
		List<Shape> shapes = start.getShapes();
		List<ShapePosition> positions = new ArrayList<ShapePosition>();
		List<ShapeRotation> rotations = new ArrayList<ShapeRotation>();
		List<ShapeSize> sizes = new ArrayList<ShapeSize>();
		
		for (int i = 0; i < shapes.size(); i++) {
			positions.add(evaluatePosition(start.getPositions().get(i), end.getPositions().get(i), fraction));
			rotations.add(evaluateRotation(start.getRotations().get(i), end.getRotations().get(i), fraction));
			sizes.add(evaluateSize(start.getSizes().get(i), end.getSizes().get(i), fraction));
		}
		
		return new ExplodingGroup(shapes, positions, rotations, sizes);
	}

	private ShapePosition evaluatePosition(ShapePosition start,
			ShapePosition end, float fraction) {
        float locationX = start.getX() + ((end.getX() - start.getX()) * fraction);
        float locationY = start.getY() + ((end.getY() - start.getY()) * fraction);
        float locationZ = start.getZ() + ((end.getZ() - start.getZ()) * fraction);
		return new ShapePosition(locationX, locationY, locationZ);
	}

	private ShapeRotation evaluateRotation(ShapeRotation start,
			ShapeRotation end, float fraction) {
        float rotationX = start.getRotationAngleX() + ((end.getRotationAngleX() - start.getRotationAngleX()) * fraction);
        float rotationY = start.getRotationAngleY() + ((end.getRotationAngleY() - start.getRotationAngleY()) * fraction);
        float rotationZ = start.getRotationAngleZ() + ((end.getRotationAngleZ() - start.getRotationAngleZ()) * fraction);
		return new ShapeRotation(rotationX, rotationY, rotationZ);
	}

	private ShapeSize evaluateSize(ShapeSize start,
			ShapeSize end, float fraction) {
        float width = start.getWidth() + ((end.getWidth() - start.getWidth()) * fraction);
        float height = start.getHeight() + ((end.getHeight() - start.getHeight()) * fraction);
        float depth = start.getDepth() + ((end.getDepth() - start.getDepth()) * fraction);
		return new ShapeSize(width, height, depth);
	}

}
