package com.sessionfive.core.ui;

import java.util.List;

import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class StyleImpl implements Style {

	private final String name;
	private final Layouter layouter;
	private final float x;
	private final float y;
	private final float z;
	
	public StyleImpl(String name, Layouter layouter, float x, float y, float z) {
		this.name = name;
		this.layouter = layouter;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Layouter getLayout() {
		return this.layouter;
	}

	@Override
	public float getRotationX() {
		return this.x;
	}

	@Override
	public float getRotationY() {
		return this.y;
	}

	@Override
	public float getRotationZ() {
		return this.z;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		return toString().equals(obj.toString());
	}

	@Override
	public boolean matches(Presentation presentation) {
		float x = 0;
		float y = 0;
		float z = 0;
		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		if (shapes.size() > 0) {
			x = shapes.get(0).getRotation().getRotationAngleX();
			y = shapes.get(0).getRotation().getRotationAngleY();
			z = shapes.get(0).getRotation().getRotationAngleZ();
		}
		
		return x == this.getRotationX() && y == this.getRotationY() && z == getRotationZ()
			&& presentation.getDefaultLayouter().equals(this.getLayout());
	}

}
