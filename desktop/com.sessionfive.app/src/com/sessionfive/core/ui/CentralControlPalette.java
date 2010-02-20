package com.sessionfive.core.ui;

import java.awt.Color;
import java.util.Iterator;

import javax.media.opengl.GLCanvas;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.animation.GoToAnimationStyle;
import com.sessionfive.animation.MoveToAnimationStyle;
import com.sessionfive.animation.ZoomOutZoomInAnimationStyle;
import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class CentralControlPalette {

	private final Presentation presentation;
	private final AnimationController animationController;

	public CentralControlPalette(Presentation presentation,
			AnimationController animationController) {
		this.presentation = presentation;
		this.animationController = animationController;
	}

	public void show() {
	}

	public void choosePresentation(GLCanvas canvas) {
		PresentationLoader loader = new PresentationLoader();
		loader.loadPresentation(presentation, canvas, getLayouter(),
				getAnimationStyles());

		canvas.requestFocus();
	}

	public void savePresentation(GLCanvas canvas) {
		PresentationLoader loader = new PresentationLoader();
		loader.savePresentation(presentation, canvas);
	}

	public void startPresentation() {
		if (!SessionFiveApplication.getInstance().isFullScreenShowing()) {
			SessionFiveApplication.getInstance().switchFullScreen();
		}
	}

	public void changeLayout(Layouter layouter, AnimationStyle animationStyle) {
		Shape focussedShape = animationController.getLastFocussedShape();
		
		layouter.layout(presentation);
		layouter.animate(presentation, animationStyle);
		
		presentation.setDefaultLayouter(layouter);
		presentation.resetStartCamera();
		animationController
				.readjustSmoothlyTo(focussedShape);
	}

	public void changeAnimation(Layouter layouter, AnimationStyle animationStyle) {
		presentation.setDefaultAnimation(animationStyle);
		layouter.animate(presentation, animationStyle);
	}

	public void setBackgroundColor(Color newColor) {
		this.presentation.setBackgroundColor(newColor);
	}

	public Layouter[] getLayouter() {
		return new Layouter[] { new LineLayouter(), new TileLayouter(),
				new CircleLayouter(), new LineGroupingLayouter() };
	}

	public AnimationStyle[] getAnimationStyles() {
		return new AnimationStyle[] { new ZoomOutZoomInAnimationStyle(),
				new MoveToAnimationStyle(), new GoToAnimationStyle() };
	}

	public void setLayerText(String text) {
		this.presentation.setLayerText(text);
	}

	public void setSpace(float value, Layouter layouter) {
		presentation.setSpace(value, layouter);
	}

	public void setReflectionEnabled(boolean reflectionEnabled) {
		presentation.setDefaultReflectionEnabled(reflectionEnabled);

		Iterator<Shape> iter = presentation.shapeIterator(true);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			shape.setReflectionEnabled(reflectionEnabled);
		}
	}

	public void setFocusScale(float focusScale) {
		presentation.setDefaultFocusScale(focusScale);
		Iterator<Shape> iter = presentation.shapeIterator(true);
		while (iter.hasNext()) {
			Shape shape = iter.next();
			shape.setFocusScale(focusScale);
		}
		animationController.readjustDirectly();
	}

	public PanelExtension[] getExtensionPanels() {
		return new PanelExtensionLoader().loadExtensions(animationController,
				presentation);
	}

}
