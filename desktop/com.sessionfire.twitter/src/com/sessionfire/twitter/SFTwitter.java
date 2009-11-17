package com.sessionfire.twitter;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class SFTwitter {

	private final String queryString;
	private long lastId = 0;
	private final int maxsize = 30;
	private CopyOnWriteArrayList<Tweet> tweets = new CopyOnWriteArrayList<Tweet>();

	public List<Tweet> getTweets() {
		return tweets;
	}

	public SFTwitter(String query) {
		this.queryString = query;
	}

	public void update() {
		final Twitter twitter = new Twitter();
		final Query query = new Query(queryString);

		Timer timer = new Timer();
		int period = 30 * 1000; // alle 30 sek, 120 requests/h (150 is limit)
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (tweets.size() > 0) {
					System.out.println("Getting since: " + lastId);
					query.setSinceId(lastId);
				}
				try {
					QueryResult result = twitter.search(query);
					if (result.getTweets().size() > 0) {
						lastId = result.getTweets().get(0).getId();
					}
					tweets.addAll(result.getTweets());
					if (tweets.size() > maxsize) {
						int overcapacity = tweets.size() - maxsize;
						List<Tweet> tomuch = tweets.subList(0, overcapacity);
						tweets.removeAll(tomuch);
					}
				} catch (TwitterException e) {
					if (e.getCause() instanceof UnknownHostException) {
						System.out.println("Can't connect to " + e.getLocalizedMessage());
					} else {
						e.printStackTrace();
					}
				}
			}
		}, 0, period);

	}

	public String getRandomTweet() {
		int index = (int) (Math.random() * tweets.size());
		if (tweets.size() > 0) {
			return tweets.get(index).getText();
		} else {
			return null;
		}
	}

}
