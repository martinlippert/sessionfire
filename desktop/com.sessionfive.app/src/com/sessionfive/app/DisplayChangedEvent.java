package com.sessionfive.app;

public class DisplayChangedEvent {
	
	private final Display display;

	public DisplayChangedEvent(Display display) {
		this.display = display;
	}
	
	public Display getDisplay() {
		return display;
	}

}
