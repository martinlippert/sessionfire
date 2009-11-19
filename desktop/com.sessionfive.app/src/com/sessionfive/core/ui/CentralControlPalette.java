package com.sessionfive.core.ui;

import java.awt.Color;
import java.util.List;

import javax.media.opengl.GLCanvas;
import javax.swing.JPanel;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class CentralControlPalette {

	private final Presentation presentation;
	private final AnimationController animationController;

	public CentralControlPalette(Presentation presentation, AnimationController animationController) {
		this.presentation = presentation;
		this.animationController = animationController;
	}

	public void show() {
	}

	public void choosePresentation(GLCanvas canvas, Layouter layouter,
			AnimationFactory animationFactory) {
		PresentationLoader loader = new PresentationLoader();
		loader.loadPresentation(presentation, canvas, layouter, animationFactory);

		canvas.requestFocus();
	}

	public void changeLayout(Layouter layouter) {
		layouter.layout(presentation);
		animationController.resetTo(animationController.getCurrentAnimationNo());
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
		return new Layouter[] { new LineLayouter(), new TileLayouter(), new CircleLayouter() };
	}

	public AnimationFactory[] getAnimators() {
		return new AnimationFactory[] { new ZoomInZoomOutAnimationFactory(),
				new MoveToAnimationFactory() };
	}

	public void setLayerText(String text) {
		 this.presentation.setLayerText(text);
//		this.animationController.animateText(text);
	}

	public String getLayerText() {
		return this.presentation.getLayerText();
	}

	public void setRotation(int x, int y, int z) {
		List<Shape> shapes = presentation.getShapes();
		for (Shape shape : shapes) {
			shape.setRotation(x, y, z);
		}
	}

	public JPanel getExtensionPanel() {
		JPanel panel = new PanelExtensionLoader().laodExtension(animationController, presentation);
		return panel;
	}

}
