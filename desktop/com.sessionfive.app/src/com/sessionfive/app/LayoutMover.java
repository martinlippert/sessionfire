package com.sessionfive.app;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.ui.Layouter;
import com.sessionfive.core.ui.MoveableLayouter;

public class LayoutMover {
	

	private final Presentation presentation;
	private final AnimationController animationController;
	private int x;

	public LayoutMover(Presentation presentation, AnimationController animationController) {
		this.presentation = presentation;
		this.animationController = animationController;
	}

	public void mouseClicked(AWTEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent mevent = (MouseEvent) event;
			if (mevent.getButton() == MouseEvent.BUTTON1
					&& mevent.getID() == MouseEvent.MOUSE_PRESSED) {
				x = mevent.getX();
			}
			if (mevent.getClickCount() == 2) {
				Layouter layouter = presentation.getDefaultLayouter();
				if (layouter instanceof MoveableLayouter) {
					MoveableLayouter ll = (MoveableLayouter) layouter;
					ll.reset();
					layouter.layout(presentation);
					//animationController.resetTo(-1);
					animationController.directlyGoTo(-1);
				}
			}
		}
	}
	
	public void mouseMoved(AWTEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent mevent = (MouseEvent) event;
			if (mevent.getButton() == MouseEvent.BUTTON1) {
				int diffX = mevent.getX() - x;
				Layouter layouter = presentation.getDefaultLayouter();
				if (layouter instanceof MoveableLayouter) {
					MoveableLayouter ll = (MoveableLayouter) layouter;
					ll.setX(diffX, mevent.getModifiers());
					layouter.layout(presentation);
					animationController.directlyGoTo(-1);
				}
			}
		}
	}

}
