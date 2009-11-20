package com.sessionfire.twitter.core;

import java.net.UnknownHostException;
import java.util.Calendar;
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

	private String queryString;
	private long lastId = 0;
	private final int maxsize = 30;
	private CopyOnWriteArrayList<Tweet> tweets = new CopyOnWriteArrayList<Tweet>();
	private Timer timer;

	public List<Tweet> getTweets() {
		return tweets;
	}

	public synchronized void start() {
		System.out.println("SFTwitter.start() mit Query:" + queryString);
		final Twitter twitter = new Twitter();
		final Query query = new Query(queryString);
		timer = new Timer();
		int period = 30 * 1000; // alle 30 sek, 120 requests/h (150 is limit)
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				System.out.println("Polling Twitter");
				if (tweets.size() > 0) {
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

	public synchronized void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public synchronized void stop() {
		if (timer != null)
			timer.cancel();
		tweets.clear();
		lastId = 0;
	}

	public synchronized String getRandomTweet() {
		int index = (int) (Math.random() * tweets.size());
		if (tweets.size() > 0) {
			Tweet tweet = tweets.get(index);
			tweets.remove(index);
			String since = getSince(tweet);
			return tweet.getFromUser() + ": " + tweet.getText() + " (" + since + ")";
		} else {
			return null;
		}
	}

	private String getSince(Tweet tweet) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(tweet.getCreatedAt());
		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if (diffSeconds < 10) {
			return "just now";
		}
		if (diffSeconds < 119) {
			return diffSeconds + " seconds ago";
		}
		if (diffMinutes < 119) {
			return diffMinutes + " minutes ago";
		}
		if (diffHours < 49) {
			return diffHours + " hours ago";
		}
		return diffDays + " days ago";
	}

}
