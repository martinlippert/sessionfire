package com.sessionfive.core.ui;

import java.util.List;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;
import com.sessionfive.core.ShapeRotation;
import com.sessionfive.core.ShapeSize;

public class ExplodingGroup {
	
	private final List<Shape> shapes;
	private final List<ShapePosition> positions;
	private final List<ShapeRotation> rotations;
	private final List<ShapeSize> sizes;

	public ExplodingGroup(List<Shape> shapes, List<ShapePosition> positions, List<ShapeRotation> rotations, List<ShapeSize> sizes) {
		this.shapes = shapes;
		this.positions = positions;
		this.rotations = rotations;
		this.sizes = sizes;
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}
	
	public List<ShapePosition> getPositions() {
		return positions;
	}
	
	public List<ShapeRotation> getRotations() {
		return rotations;
	}
	
	public List<ShapeSize> getSizes() {
		return sizes;
	}

}
