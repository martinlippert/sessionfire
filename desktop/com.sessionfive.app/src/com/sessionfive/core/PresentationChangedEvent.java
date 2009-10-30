package com.sessionfive.core;

public class PresentationChangedEvent {
	
	private final Presentation changedPresentation;

	public PresentationChangedEvent(Presentation changedPresentation) {
		this.changedPresentation = changedPresentation;
	}
	
	public Presentation getChangedPresentation() {
		return changedPresentation;
	}

}
