package com.sessionfive.core.ui;

import java.awt.Color;
import java.awt.Component;

import com.sessionfive.core.Presentation;

public class CentralControlPalette {
	
	private final Presentation presentation;

	public CentralControlPalette(Presentation presentation) {
		this.presentation = presentation;
	}
	
	public void show() {
	}

	public void choosePresentation(Component canvas, Layouter layouter, Object object2) {
		PresentationLoader loader = new PresentationLoader();
		loader.loadPresentation(presentation);
		layouter.layout(presentation);

		canvas.requestFocus();
	}

	public void changeLayout(Layouter layouter) {
		System.out.println("layout: " + layouter.getName());
		layouter.layout(presentation);
	}

	public void setBackgroundColor(Color newColor) {
		this.presentation.setBackgroundColor(newColor);
	}

	public Color getBackgroundColor() {
		return this.presentation.getBackgroundColor();
	}
	
	public Layouter[] getLayouter() {
		return new Layouter[] {new LineLayouter(false), new LineLayouter(true)};
	}

}
