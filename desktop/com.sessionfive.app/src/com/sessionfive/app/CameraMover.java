package com.sessionfive.app;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.Camera;
import com.sessionfive.core.Point;
import com.sessionfive.core.Presentation;

public class CameraMover {

	private final Presentation presentation;
	private final AnimationController animationController;
	private int mouseX;
	private int mouseY;

	public CameraMover(Presentation presentation, AnimationController animationController) {
		this.presentation = presentation;
		this.animationController = animationController;
	}

	public void mouseClicked(AWTEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent mevent = (MouseEvent) event;
			if (mevent.getButton() == MouseEvent.BUTTON1
					&& mevent.getID() == MouseEvent.MOUSE_PRESSED) {
				mouseX = mevent.getX();
				mouseY = mevent.getY();
			}
			if (mevent.getClickCount() == 2 && animationController.getCurrentAnimationNo() == -1) {
				presentation.resetStartCamera();
				animationController.directlyGoTo(-1);
			}
		}
	}

	public void mouseMoved(AWTEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent mevent = (MouseEvent) event;
			if (mevent.getButton() == MouseEvent.BUTTON1 && animationController.getCurrentAnimationNo() == -1) {
				float diffX = mouseX - mevent.getX();
				float diffY = mouseY - mevent.getY();
				setDiff(diffX, diffY, mevent.getModifiers());
				mouseX = mevent.getX();
				mouseY = mevent.getY();
			}
		}
	}

	private void setDiff(float diffX, float diffY, int modifiers) {

		Camera oldStartCamera = presentation.getStartCamera();
		Point location = oldStartCamera.getLocation();
		Point target = oldStartCamera.getTarget();

		if ((modifiers & MouseEvent.META_MASK) != 0) {
			diffX = diffX / 4;
			diffY = diffY / 4;
			Camera startCamera = new Camera(location.getX(), location.getY(), location.getZ(),
					target.getX() - diffX, target.getY() - diffY, target.getZ());
			presentation.setStartCamera(startCamera);
			animationController.directlyGoTo(-1);
		} else if ((modifiers & MouseEvent.SHIFT_MASK) != 0) {
			//diffY = diffY;
			Camera startCamera = new Camera(location.getX(), location.getY(), location.getZ()
					+ diffY, target.getX(), target.getY(), target.getZ() + diffY);
			presentation.setStartCamera(startCamera);
			animationController.directlyGoTo(-1);
		} else {
			diffX = diffX / 4;
			diffY = diffY / 4;
			Camera startCamera = new Camera(location.getX() + diffX, location.getY() - diffY,
					location.getZ(), target.getX() + diffX, target.getY() - diffY, target.getZ());
			presentation.setStartCamera(startCamera);
			animationController.directlyGoTo(-1);
		}
	}

}
