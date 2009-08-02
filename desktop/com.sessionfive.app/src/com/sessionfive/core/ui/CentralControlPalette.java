package com.sessionfive.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class CentralControlPalette {
	
	private final Presentation presentation;

	public CentralControlPalette(Presentation presentation) {
		this.presentation = presentation;
	}
	
	public void show() {
	}

	public void choosePresentation(Component canvas, Layouter layouter, AnimationFactory animationFactory) {
		PresentationLoader loader = new PresentationLoader();
		loader.loadPresentation(presentation);
		
		changeLayout(layouter);
		changeAnimation(animationFactory);

		canvas.requestFocus();
	}

	public void changeLayout(Layouter layouter) {
		layouter.layout(presentation);
	}

	public void changeAnimation(AnimationFactory animationFactory) {
		Focusable startShape = presentation;
		presentation.removeAllAnimations();
		
		List<Shape> shapes = presentation.getShapes();
		for (Shape shape : shapes) {
			Animation animation = animationFactory.createAnimation(startShape, shape);
			presentation.addAnimation(animation);
			startShape = shape;
		}
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
	
	public AnimationFactory[] getAnimators() {
		return new AnimationFactory[] {new ZoomInZoomOutAnimationFactory(), new MoveToAnimationFactory()};
	}

}
