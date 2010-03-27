package com.sessionfive.core.ui;

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
	public TimingTarget[] canceledFocussing(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget[] finishedFocussing(Shape shape) {
		return null;
	}

	@Override
	public TimingTarget[] startsFocussing(Shape shape) {
		ShapePosition position = shape.getPosition();
		ShapePosition focussedPosition = shape.getFocussedPosition();
		if (position.equals(focussedPosition)) {
			return null;
		}

		TimingTarget[] result = null;

		Shape parent = shape.getOwner();
		if (parent != null) {
			List<Shape> childs = parent.getShapes();
			result = new TimingTarget[childs.size()];
			int i = 0;
			for (Shape child : childs) {

				PropertySetter psPosition = createPositionTimingTarget(child,
						child.getPosition(), child.getFocussedPosition());
				PropertySetter psRotation = createRotationTarget(child, child
						.getRotation(), child.getFocussedRotation());
				PropertySetter psSize = createSizeTarget(child,
						child.getSize(), child.getFocussedSize());

				ShapeTimingTarget shapeTarget = new ShapeTimingTarget(child,
						new TimingTarget[] { psPosition, psRotation, psSize });
				result[i++] = shapeTarget;
			}
		}

		return result;
	}
	
	@Override
	public TimingTarget[] groupOfShapeLeft(Shape shape) {
		TimingTarget[] result = null;
		Shape parent = shape.getOwner();
		if (parent != null) {
			List<Shape> childs = parent.getShapes();
			result = new TimingTarget[childs.size()];
			int i = 0;
			for (Shape child : childs) {

				PropertySetter psPosition = createPositionTimingTarget(child,
						child.getPosition(), child.getCollapsedPosition());
				PropertySetter psRotation = createRotationTarget(child, child
						.getRotation(), child.getCollapsedRotation());
				PropertySetter psSize = createSizeTarget(child,
						child.getSize(), child.getCollapsedSize());

				ShapeTimingTarget shapeTarget = new ShapeTimingTarget(child,
						new TimingTarget[] { psPosition, psRotation, psSize });
				result[i++] = shapeTarget;
			}
		}

		return result;
	}

	@Override
	public TimingTarget[] shapeLeft(Shape shape) {
		return null;
	}

	private PropertySetter createPositionTimingTarget(Shape child,
			ShapePosition start, ShapePosition end) {
		KeyValues<ShapePosition> valuesPosition = KeyValues.create(
				new ShapePositionEvaluator(), start, end);
		KeyTimes timesPosition = new KeyTimes(0f, 1f);
		KeyFrames framesPosition = new KeyFrames(valuesPosition, timesPosition);
		PropertySetter psPosition = new PropertySetter(child, "position",
				framesPosition);
		return psPosition;
	}

	private PropertySetter createRotationTarget(Shape child,
			ShapeRotation start, ShapeRotation end) {
		KeyValues<ShapeRotation> valuesRotation = KeyValues.create(
				new ShapeRotationEvaluator(), start, end);
		KeyTimes timesRotation = new KeyTimes(0f, 1f);
		KeyFrames framesRotation = new KeyFrames(valuesRotation, timesRotation);
		PropertySetter psRotation = new PropertySetter(child, "rotation",
				framesRotation);
		return psRotation;
	}
	
	private PropertySetter createSizeTarget(Shape child, ShapeSize start,
			ShapeSize end) {
		KeyValues<ShapeSize> valuesSize = KeyValues.create(
				new ShapeSizeEvaluator(), start, end);
		KeyTimes timesSize = new KeyTimes(0f, 1f);
		KeyFrames framesSize = new KeyFrames(valuesSize, timesSize);
		PropertySetter psSize = new PropertySetter(child, "size", framesSize);
		return psSize;
	}
	
}
