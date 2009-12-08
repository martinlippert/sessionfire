package com.sessionfive.core.ui;

public enum HelpWindowPosition {
	ABOVE, BELOW, CENTER_NO_ARROW;

	public boolean withArrow() {
		return this != CENTER_NO_ARROW;
	}
}
