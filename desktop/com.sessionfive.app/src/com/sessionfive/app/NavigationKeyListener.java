package com.sessionfive.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.sessionfive.animation.AnimationController;

public class NavigationKeyListener implements KeyListener {
	
	private final AnimationController animationController;
	private final StringBuffer goToKeyframeDirectlyBuffer;
	private final SessionFiveApplication app;
	
	private boolean pressed;
	private boolean pressedProcessed;

	public NavigationKeyListener(AnimationController animationController, SessionFiveApplication app) {
		this.animationController = animationController;
		this.app = app;
		this.goToKeyframeDirectlyBuffer = new StringBuffer();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (pressed && !pressedProcessed) {
			keyPressed(e, 1);
		}
		pressed = false;
		pressedProcessed = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (pressed && !pressedProcessed) {
			keyPressed(e, 2);
			pressedProcessed = true;
		}
		else {
			pressed = true;
		}
	}
	
	public void keyPressed(KeyEvent e, int pressCount) {
		if (e.getKeyCode() == KeyEvent.VK_HOME
				|| (e.getKeyCode() == KeyEvent.VK_UP && (e.getModifiers() & KeyEvent.META_MASK) != 0)) {
			animationController.goToKeyframeNo(0);
		} else if (e.getKeyCode() == KeyEvent.VK_END
				|| (e.getKeyCode() == KeyEvent.VK_DOWN && (e.getModifiers() & KeyEvent.META_MASK) != 0)) {
			animationController.goToKeyframeNo(animationController.getNumberOfKeyFrames() - 1);
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			goToKeyframeDirectly();
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN
				|| e.getKeyCode() == KeyEvent.VK_DOWN
				|| e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (pressCount == 2 && animationController.canZoomIn()) {
				animationController.zoomIn();
			}
			else if (pressCount == 2 && animationController.canZoomOut()) {
				animationController.zoomOut();
			}
			else {
				animationController.forward();
			}
		} else if (e.getKeyChar() == '+' || e.getKeyChar() == '=') {
			animationController.zoomIn();
		} else if (e.getKeyChar() == '-') {
			animationController.zoomOut();
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP
				|| e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT
				|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (pressCount == 2 && animationController.canZoomOut()) {
				animationController.zoomOut();
			}
			else {
				animationController.backward();
			}
		} else if (e.getKeyChar() == 'f') {
			app.switchFullScreen();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (app.isFullScreenShowing()) {
				app.switchFullScreen();
			}
		}

		if (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
			goToKeyframeDirectlyBuffer.append(e.getKeyChar());
			if (app.getPresentation().getAllShapes().size() < 10) {
				goToKeyframeDirectly();
				goToKeyframeDirectlyBuffer.delete(0, goToKeyframeDirectlyBuffer.length());
			}
		} else {
			goToKeyframeDirectlyBuffer.delete(0, goToKeyframeDirectlyBuffer.length());
		}
	}
	
	protected void goToKeyframeDirectly() {
		try {
			int keyframeNo = Integer.parseInt(goToKeyframeDirectlyBuffer.toString());
			if (keyframeNo >= 0) {
				keyframeNo = Math.min(keyframeNo, animationController.getNumberOfKeyFrames());
				animationController.goToKeyframeNo(keyframeNo - 1);
			}
		} catch (NumberFormatException e) {
		}
	}


}
