package com.sessionfire.demo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.Presentation;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		SessionFiveApplication instance = SessionFiveApplication.getInstance();
		if (instance != null) {
			Presentation presentation = instance.getPresentation();
			DemoCreator creator = new DemoCreator();
			creator.createDemo(presentation);
		}
	}

	public void stop(BundleContext context) throws Exception {
	}

}
