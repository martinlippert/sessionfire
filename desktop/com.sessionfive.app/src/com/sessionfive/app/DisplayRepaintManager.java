package com.sessionfive.app;

import java.awt.Canvas;

import com.sessionfive.core.Presentation;
import com.sessionfive.core.PresentationChangedEvent;
import com.sessionfive.core.PresentationChangedListener;

public class DisplayRepaintManager {
	
	private final Display display;
	private final Presentation presentation;

	private PresentationChangedListener presentationListener;
	private DisplayChangedListener displayListener;

	public DisplayRepaintManager(final Display display, final Presentation presentation, final Canvas canvas) {
		this.display = display;
		this.presentation = presentation;
		displayListener = new DisplayChangedListener() {
			@Override
			public void displayChanged(DisplayChangedEvent e) {
				canvas.repaint();
			}
		};
		display.addDisplayChangedListener(displayListener);
		
		presentationListener = new PresentationChangedListener() {
			@Override
			public void presentationChanged(PresentationChangedEvent event) {
				canvas.repaint();
			}
		};
		presentation.addPresentationChangedListener(presentationListener);
	}
	
	public void dispose() {
		display.removeDisplayChangedListener(displayListener);
		presentation.removePresentationChangedListener(presentationListener);
	}

}
