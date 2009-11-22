package com.sessionfire.timer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static Activator activator;
	private TimerController timerController;

	public Activator() {
	}
	
	public static Activator getInstance() {
		return activator;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		activator = this;
		timerController = new TimerController();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		activator = null;
	}
	
	public TimerController getTimerController() {
		return timerController;
	}

}
