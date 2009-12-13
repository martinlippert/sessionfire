package com.sessionfire.timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.SessionFiveApplication;

public class TimerController {
	
	private int seconds = 20;
	private int currentFrame = 0;
	private int remainingSeconds;
	private Timer timer;
	
	private List<TimerListener> listener;
	
	public TimerController() {
		listener = new ArrayList<TimerListener>();
	}
	
	public void addTimerListener(TimerListener listener) {
		this.listener.add(listener);
	}
	
	public void removeTimerListener(TimerListener listener) {
		this.listener.remove(listener);
	}

	public void setTime(int seconds) {
		this.seconds = seconds;
	}

	public void startTimer() {
		if (timer != null && timer.isRunning()) return;
		
		final AnimationController animationController = SessionFiveApplication.getInstance().getAnimationController();
		final int numberOfKeyFrames = animationController.getNumberOfKeyFrames();

		currentFrame = animationController.getCurrentAnimationNo();
		timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remainingSeconds--;
				fireRemainingSecondsChanged(remainingSeconds);

				if (remainingSeconds <= 0) {
					currentFrame++;
					animationController.goTo(currentFrame);
					remainingSeconds = seconds;
				
					if (currentFrame >= numberOfKeyFrames - 1) {
						stopTimer();
					}
				}
			}
		});
		
		remainingSeconds = seconds;
		fireRemainingSecondsChanged(remainingSeconds);
		
		timer.start();
		fireTimerStarted();
	}

	public void stopTimer() {
		if (timer != null && timer.isRunning()) {
			timer.stop();
			fireTimerStopped();
		}
	}
	
	public boolean isRunning() {
		return timer != null && timer.isRunning();
	}
	
	protected void fireTimerStarted() {
		for (TimerListener timerlistener : this.listener) {
			timerlistener.timerStarted();
		}
	}
	
	protected void fireTimerStopped() {
		for (TimerListener timerlistener : this.listener) {
			timerlistener.timerStopped();
		}
	}
	
	protected void fireRemainingSecondsChanged(int remainingSeconds) {
		for (TimerListener timerlistener : this.listener) {
			timerlistener.remainingTimeChanged(remainingSeconds);
		}
	}
	
}
