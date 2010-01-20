package com.sessionfive.app;

import com.sessionfive.core.Shape;

public class SelectionChangedEvent {
	
	private final Shape[] selectedShapes;

	public SelectionChangedEvent(final Shape[] selectedShapes) {
		this.selectedShapes = selectedShapes;
	}
	
	public Shape[] getSelectedShapes() {
		return selectedShapes;
	}

}
