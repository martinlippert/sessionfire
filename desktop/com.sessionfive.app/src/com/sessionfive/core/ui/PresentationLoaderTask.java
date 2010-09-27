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

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeRotation;

public class PresentationLoaderTask extends SwingWorker<Void, Void> {

	private final HierarchicFileStructureNode fileStructure;
	private final ShapeExtensionCreator creator;
	private final Presentation presentation;
	private final GLCanvas canvas;
	private final Layouter[] allLayouter;
	private final AnimationStyle[] allAnimationStyles;
	private final AnimationPathLayouter[] allAnimationPathLayouter;

	private int counter;
	private int totalNumberOfFiles;

	public PresentationLoaderTask(HierarchicFileStructureNode fileStructure,
			ShapeExtensionCreator creator, Presentation presentation,
			GLCanvas canvas, Layouter[] layouter,
			AnimationStyle[] animationStyles,
			AnimationPathLayouter[] animationPathLayouter) {
		this.fileStructure = fileStructure;
		this.totalNumberOfFiles = fileStructure.getElementCount();
		this.creator = creator;
		this.presentation = presentation;
		this.canvas = canvas;
		this.allLayouter = layouter;
		this.allAnimationStyles = animationStyles;
		this.allAnimationPathLayouter = animationPathLayouter;
	}

	@Override
	protected Void doInBackground() throws Exception {

		setProgress(0);

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		Iterator<Shape> shapesIter = shapes.iterator();
		while (shapesIter.hasNext()) {
			shapesIter.next().release(canvas.getContext());
		}

		presentation.removeAllShapes(LayerType.CAMERA_ANIMATED);
		presentation.removeAllAnimationSteps();

		Properties settings = loadPresentationSettings(presentation);

		AnimationStyle animationStyle = presentation.getDefaultAnimationStyle();
		Layouter layouter = presentation.getDefaultLayouter();
		AnimationPathLayouter pathLayouter = presentation.getDefaultAnimationPathLayouter();

		float rotationX = 0.0f;
		float rotationY = 0.0f;
		float rotationZ = 0.0f;

		String rotationXProp = settings.getProperty("rotationX");
		String rotationYProp = settings.getProperty("rotationY");
		String rotationZProp = settings.getProperty("rotationZ");

		if (rotationXProp != null && rotationYProp != null
				&& rotationZProp != null) {
			rotationX = Float.parseFloat(rotationXProp);
			rotationY = Float.parseFloat(rotationYProp);
			rotationZ = Float.parseFloat(rotationZProp);
		}

		createShapes(layouter, rotationX, rotationY, rotationZ, canvas
				.getContext(), SessionFiveApplication.getInstance()
				.getAnimationController());
		pathLayouter.layoutAnimationPath(presentation, animationStyle);

		setProgress(100);

		SessionFiveApplication.getInstance().getSelectionService()
				.selectAllShapes(presentation);
		return null;
	}

	public void createShapes(Layouter layouter, float rotationX,
			float rotationY, float rotationZ, GLContext context,
			AnimationController animationController) throws Exception {

		totalNumberOfFiles = fileStructure.getElementCount();
		counter = 0;

		int childCount = fileStructure.getChildCount();
		for (int i = 0; i < childCount; i++) {
			HierarchicFileStructureNode child = fileStructure.getChild(i);
			createShapesRecursively(child, null, layouter, rotationX,
					rotationY, rotationZ, context, animationController);
		}
	}

	private void createShapesRecursively(HierarchicFileStructureNode node,
			Shape parentShape, Layouter layouter, float rotationX,
			float rotationY, float rotationZ, GLContext context,
			AnimationController animationController) throws Exception {

		Shape newShape = null;
		File file = node.getFile();
		if (file != null) {
			newShape = createConcreteShape(file, rotationX, rotationY,
					rotationZ, context);
			if (newShape != null) {
				if (parentShape != null) {
					parentShape.addShape(newShape);
				} else {
					presentation.addShape(newShape, LayerType.CAMERA_ANIMATED);
				}
			}

			layouter.layout(presentation);
			animationController.reset();
		}

		int childCount = node.getChildCount();
		if (childCount > 0) {
			if (newShape == null) {
				newShape = new AbstractShape();
				if (parentShape != null) {
					parentShape.addShape(newShape);
				} else {
					presentation.addShape(newShape, LayerType.CAMERA_ANIMATED);
				}

				layouter.layout(presentation);
				animationController.reset();
			}

			for (int i = 0; i < childCount; i++) {
				createShapesRecursively(node.getChild(i), newShape, layouter,
						rotationX, rotationY, rotationZ, context,
						animationController);
			}
		}
	}

	private Shape createConcreteShape(File file, float rotationX,
			float rotationY, float rotationZ, GLContext context)
			throws Exception {

		Shape newShape = creator.createShape(file);
		if (newShape != null) {
			newShape.setRotation(new ShapeRotation(rotationX, rotationY, rotationZ));
			newShape.setReflectionEnabled(presentation
					.isDefaultReflectionEnabled());
			newShape.setFocusScale(presentation.getDefaultFocusScale());
			newShape.initialize(context);
		}

		counter++;
		setProgress(Math.min(counter * 100 / totalNumberOfFiles, 100));

		return newShape;
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
				AnimationStyle animationStyle = getAnimationStyle(
						animationSetting, allAnimationStyles);
				if (animationStyle != null) {
					presentation.setDefaultAnimationStyle(animationStyle);
				}

				String animationPathSetting = settings
						.getProperty("animationPath");
				AnimationPathLayouter animationPathLayouter = getAnimationPathLayouter(
						animationPathSetting, allAnimationPathLayouter);
				if (animationPathLayouter != null) {
					presentation
							.setDefaultAnimationPathLayouter(animationPathLayouter);
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
					presentation.setDefaultReflectionEnabled(Boolean
							.parseBoolean(reflectionEnabled));
				}

				String focusscale = settings.getProperty("focusscale");
				if (focusscale != null) {
					presentation.setDefaultFocusScale(Float
							.parseFloat(focusscale));
				}

				String startCameraLocationXS = settings
						.getProperty("startCameraLocationX");
				if (startCameraLocationXS != null) {
					float cameraLocationX = Float
							.parseFloat(startCameraLocationXS);
					float cameraLocationY = Float.parseFloat(settings
							.getProperty("startCameraLocationY"));
					float cameraLocationZ = Float.parseFloat(settings
							.getProperty("startCameraLocationZ"));
					float cameraTargetY = Float.parseFloat(settings
							.getProperty("startCameraTargetY"));
					float cameraTargetX = Float.parseFloat(settings
							.getProperty("startCameraTargetX"));
					float cameraTargetZ = Float.parseFloat(settings
							.getProperty("startCameraTargetZ"));
					Camera camera = new Camera(cameraLocationX,
							cameraLocationY, cameraLocationZ, cameraTargetX,
							cameraTargetY, cameraTargetZ);
					presentation.setStartCamera(camera);
				} else {
					presentation.resetStartCamera();
				}
				
				String name = settings.getProperty("name");
				presentation.setName(name != null ? name : "");
				
				String id = settings.getProperty("id");
				presentation.setId(id != null ? id : "");

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

	private AnimationStyle getAnimationStyle(String animationSetting,
			AnimationStyle[] allAnimationStyles) {
		for (AnimationStyle animationFactory : allAnimationStyles) {
			if (animationFactory.getName().equals(animationSetting))
				return animationFactory;
		}
		return null;
	}

	private AnimationPathLayouter getAnimationPathLayouter(
			String animationPathLayouterSetting,
			AnimationPathLayouter[] allAnimationPathLayouter) {
		for (AnimationPathLayouter animationPathLayouter : allAnimationPathLayouter) {
			if (animationPathLayouter.getName().equals(
					animationPathLayouterSetting))
				return animationPathLayouter;
		}
		return null;
	}

}
