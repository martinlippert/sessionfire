package com.sessionfire.twitter.core;

import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sessionfive.app.SessionFiveApplication;

public class Activator implements BundleActivator {
	private static Activator activator;
	SFTwitter sfTwitter;
	private Timer animationtimer;
	private TwitterShape texShape2;

	@Override
	public void start(BundleContext context) throws Exception {
		activator = this;
		sfTwitter = new SFTwitter();
		animationtimer = new Timer();
		texShape2 = new TwitterShape();
	}

	public void startNewTwitterPoll(String query) {
		animationtimer.cancel();
		sfTwitter.stop();
		sfTwitter.setQueryString(query);
		sfTwitter.start();
		animationtimer = new Timer();

		SessionFiveApplication.getInstance().getPresentation().addShape(texShape2);

		animationtimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				String randomTweet = sfTwitter.getRandomTweet();
				// randomTweet =
				// "luebken: Dies ist ein schöner Tweet und ich erzähle hier mal was.";
				System.out.println("Get random Tweet: " + randomTweet);
				texShape2.setText(randomTweet);
			}
		}, 1000, 5000);
	}

	public void stopTwitterPoll() {
		animationtimer.cancel();
		sfTwitter.stop();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		activator = null;
	}

	public static Activator getActivator() {
		return activator;
	}

}
