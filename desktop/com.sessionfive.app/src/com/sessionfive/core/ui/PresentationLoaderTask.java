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
import com.sessionfive.core.Camera;
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

		Properties settings = loadPresentationSettings(presentation);
		
		AnimationFactory animationFactory = presentation.getDefaultAnimation();
		Layouter layouter = presentation.getDefaultLayouter();
		
		float rotationX = 0.0f;
		float rotationY = 0.0f;
		float rotationZ = 0.0f;
		
		String rotationXProp = settings.getProperty("rotationX");
		String rotationYProp = settings.getProperty("rotationY");
		String rotationZProp = settings.getProperty("rotationZ");

		if (rotationXProp != null && rotationYProp != null && rotationZProp != null) {
			rotationX = Float.parseFloat(rotationXProp);
			rotationY = Float.parseFloat(rotationYProp);
			rotationZ = Float.parseFloat(rotationZProp);
		}

		GLContext context = canvas.getContext();
		for (int i = 0; i < numberOfFiles; i++) {
			Shape newShape = creator.createShape(files[i]);
			if (newShape != null) {
				newShape.setRotation(rotationX, rotationY, rotationZ);
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
		
		Shape[] allShapes = presentation.getShapes(LayerType.CAMERA_ANIMATED).toArray(new Shape[0]);
		SessionFiveApplication.getInstance().getSelectionService().setSelection(allShapes);
		return null;
	}

	private Properties loadPresentationSettings(Presentation presentation) {
		Properties settings = new Properties();

		File dir = new File(presentation.getPath());
		if (dir.exists()) {
			File presentationFile = new File(dir, "sessionfire.settings");
			try {
				FileInputStream fis = new FileInputStream(presentationFile);
				settings.load(fis);
				fis.close();
				
				String layouterSetting = settings.getProperty("layout");
				Layouter layouter = getLayouter(layouterSetting, allLayouter);
				if (layouter != null) {
					presentation.setDefaultLayouter(layouter);
					layouter.layout(presentation);
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

				String startCameraLocationXS = settings.getProperty("startCameraLocationX");
				if (startCameraLocationXS != null) {
					float cameraLocationX = Float.parseFloat(startCameraLocationXS);
					float cameraLocationY = Float.parseFloat(settings.getProperty("startCameraLocationY"));
					float cameraLocationZ = Float.parseFloat(settings.getProperty("startCameraLocationZ"));
					float cameraTargetY = Float.parseFloat(settings.getProperty("startCameraTargetY"));
					float cameraTargetX = Float.parseFloat(settings.getProperty("startCameraTargetX"));
					float cameraTargetZ = Float.parseFloat(settings.getProperty("startCameraTargetZ"));
					Camera camera = new Camera(cameraLocationX, cameraLocationY, cameraLocationZ, cameraTargetX, cameraTargetY, cameraTargetZ);
					presentation.setStartCamera(camera);
				}
				else {
					presentation.resetStartCamera();
				}

			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return settings;
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
