package com.sessionfire.timer;

public interface TimerListener {
	
	public void timerStarted();
	public void timerStopped();
	public void remainingTimeChanged(int remainingSeconds);

}
