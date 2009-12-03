package com.sessionfire.timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.SessionFiveApplication;

public class TimerController {
	
	private int milliseconds = 2000;
	private int currentFrame = 0;
	private Timer timer;

	public void setTime(int milliseconds) {
		this.milliseconds = milliseconds;
	}

	public void startTimer() {
		final AnimationController animationController = SessionFiveApplication.getInstance().getAnimationController();
		final int numberOfKeyFrames = animationController.getNumberOfKeyFrames();

		currentFrame = animationController.getCurrentAnimationNo();
		timer = new Timer(milliseconds, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentFrame++;
				System.out.println("goto: " + currentFrame);
				animationController.goTo(currentFrame);
				
				if (currentFrame >= numberOfKeyFrames - 1) {
					timer.stop();
				}
			}
		});
		
		timer.start();
	}

	public void stopTimer() {
		if (timer.isRunning()) {
			timer.stop();
		}
	}
	
}
