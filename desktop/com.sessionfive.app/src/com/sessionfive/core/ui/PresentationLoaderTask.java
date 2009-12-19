package com.sessionfive.core.ui;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLContext;
import javax.swing.SwingWorker;

import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class PresentationLoaderTask extends SwingWorker<Void, Void> {

	private final File[] files;
	private final ShapeExtensionCreator creator;
	private final Presentation presentation;
	private final GLCanvas canvas;
	private final Layouter[] allLayouter;
	private final AnimationFactory[] allAnimationFactories;

	public PresentationLoaderTask(File[] files, ShapeExtensionCreator creator,
			Presentation presentation, GLCanvas canvas, Layouter[] layouter, AnimationFactory[] animationFactories) {
		this.files = files;
		this.creator = creator;
		this.presentation = presentation;
		this.canvas = canvas;
		this.allLayouter = layouter;
		this.allAnimationFactories = animationFactories;
	}

	@Override
	protected Void doInBackground() throws Exception {

		setProgress(0);
		int numberOfFiles = files.length;

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		Iterator<Shape> shapesIter = shapes.iterator();
		while (shapesIter.hasNext()) {
			shapesIter.next().release(canvas.getContext());
		}

		presentation.removeAllShapes(LayerType.CAMERA_ANIMATED);
		presentation.removeAllAnimations();

		Focusable animationStart = presentation;

		loadPresentationSettings(presentation);
		AnimationFactory animationFactory = presentation.getDefaultAnimation();
		Layouter layouter = presentation.getDefaultLayouter();

		GLContext context = canvas.getContext();
		for (int i = 0; i < numberOfFiles; i++) {
			Shape newShape = creator.createShape(files[i]);
			if (newShape != null) {
				newShape.setRotation(presentation.getDefaultRotationX(),
						presentation.getDefaultRotationY(), presentation
								.getDefaultRotationZ());
				newShape.setReflectionEnabled(presentation.isDefaultReflectionEnabled());
				newShape.setFocusScale(presentation.getDefaultFocusScale());
				newShape.initialize(context);
				presentation.addShape(newShape, LayerType.CAMERA_ANIMATED);

				Animation animation = animationFactory.createAnimation(
						animationStart, newShape);
				presentation.addAnimation(animation);
				animationStart = newShape;

				layouter.layout(presentation);
				SessionFiveApplication.getInstance().getAnimationController()
						.resetTo(-1);
			}
			setProgress(Math.min(i * 100 / numberOfFiles, 100));
		}

		setProgress(100);
		return null;
	}

	private void loadPresentationSettings(Presentation presentation) {
		File dir = new File(presentation.getPath());
		if (dir.exists()) {
			File presentationFile = new File(dir, "sessionfire.settings");
			try {
				Properties settings = new Properties();
				FileInputStream fis = new FileInputStream(presentationFile);
				settings.load(fis);
				fis.close();
				
				String layouterSetting = settings.getProperty("layout");
				Layouter layouter = getLayouter(layouterSetting, allLayouter);
				if (layouter != null) {
					presentation.setDefaultLayouter(layouter);
				}
				
				String animationSetting = settings.getProperty("animation");
				AnimationFactory animationFactory = getAnimationFactory(animationSetting, allAnimationFactories);
				if (animationFactory != null) {
					presentation.setDefaultAnimation(animationFactory);
				}
				
				String color = settings.getProperty("backgroundColor");
				if (color != null) {
					presentation.setBackgroundColor(new Color(Integer
							.parseInt(color)));
				}

				String rotationX = settings.getProperty("rotationX");
				String rotationY = settings.getProperty("rotationY");
				String rotationZ = settings.getProperty("rotationZ");

				if (rotationX != null && rotationY != null && rotationZ != null) {
					presentation.setDefaultRotation(
							Float.parseFloat(rotationX), Float
									.parseFloat(rotationY), Float
									.parseFloat(rotationZ));
				}

				String layerText = settings.getProperty("layerText");
				if (layerText != null) {
					presentation.setLayerText(layerText);
				}

				String space = settings.getProperty("spaceBetween");
				if (space != null) {
					presentation.setSpace(Float.parseFloat(space), presentation
							.getDefaultLayouter());
				}
				
				String reflectionEnabled = settings.getProperty("reflection");
				if (reflectionEnabled != null) {
					presentation.setDefaultReflectionEnabled(Boolean.parseBoolean(reflectionEnabled));
				}

				String focusscale = settings.getProperty("focusscale");
				if (focusscale != null) {
					presentation.setDefaultFocusScale(Float.parseFloat(focusscale));
				}
				
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Layouter getLayouter(String layouterSetting, Layouter[] allLayouter) {
		for (Layouter layouter : allLayouter) {
			if (layouter.getName().equals(layouterSetting))
				return layouter;
		}
		return null;
	}

	private AnimationFactory getAnimationFactory(String animationSetting,
			AnimationFactory[] allAnimationFactories) {
		for (AnimationFactory animationFactory : allAnimationFactories) {
			if (animationFactory.getName().equals(animationSetting))
				return animationFactory;
		}
		return null;
	}

}
