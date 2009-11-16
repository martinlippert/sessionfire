package com.sessionfire.twitter;

import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.SessionFiveApplication;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		final SFTwitter sfTwitter = new SFTwitter("sessionfire");
		sfTwitter.update();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				System.out.println("update");
				AnimationController animationController = SessionFiveApplication.getInstance()
						.getAnimationController();
				String randomTweet = sfTwitter.getRandomTweet();
				animationController.animateText(randomTweet);
			}
		}, 5000, 5000);

	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
