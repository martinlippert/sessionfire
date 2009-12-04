package com.sessionfire.timer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TimerKeyListener implements KeyListener {
	
	public TimerKeyListener() {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_T) {
			Activator.getInstance().getTimerController().startTimer();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Activator.getInstance().getTimerController().stopTimer();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
}
