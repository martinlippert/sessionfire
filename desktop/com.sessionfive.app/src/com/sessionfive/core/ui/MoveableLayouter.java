package com.sessionfive.core.ui;

import java.awt.event.MouseEvent;

public abstract class MoveableLayouter implements Layouter {

	float startLocationX;
	float startTargetX;
	float startTargetY;

	float locationX;
	float targetX;
	float targetY;

	public MoveableLayouter(float startLocationX, float startTargetX, float startTargetY) {
		this.startLocationX = startLocationX;
		this.startTargetX = startTargetX;
		this.startTargetY = startTargetY;

		this.locationX = startLocationX;
		this.targetX = startTargetX;
		this.targetY = startTargetY;
	}

	public void setX(float diffX, int modifiers) {
		if ((modifiers & MouseEvent.SHIFT_MASK) != 0) {
			float newtargetX = this.targetX + (diffX / 60);

			if (newtargetX > -100 && newtargetX < 100) {
				this.targetX = newtargetX;
			}
		} else if ((modifiers & MouseEvent.META_MASK) != 0) {
			float newtargetY = this.targetY + (diffX / 60);

			if (newtargetY > -50 && newtargetY < 50) {
				this.targetY = newtargetY;
			}
		} else {

			float newlocation = this.locationX + (diffX / 30);

			if (newlocation > -800 && newlocation < 800) {
				this.locationX = newlocation;
			}

		}
	}

	public void reset() {
		locationX = startLocationX;
		targetX = startTargetX;
		targetY = startTargetY;
	}

	public float getLocationX() {
		return locationX;
	}

	public void setLocationX(float locationX) {
		this.locationX = locationX;
	}

	public float getTargetX() {
		return targetX;
	}

	public void setTargetX(float targetX) {
		this.targetX = targetX;
	}

	public float getTargetY() {
		return targetY;
	}

	public void setTargetY(float targetY) {
		this.targetY = targetY;
	}

}
