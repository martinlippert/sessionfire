package com.sessionfive.core.ui;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLContext;
import javax.swing.SwingWorker;

import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class PresentationLoaderTask extends SwingWorker<Void, Void> {
	
	private final File[] files;
	private final ShapeExtensionCreator creator;
	private final Presentation presentation;
	private final GLCanvas canvas;
	private final Layouter layouter;
	private final AnimationFactory animationFactory;

	public PresentationLoaderTask(File[] files, ShapeExtensionCreator creator, Presentation presentation, GLCanvas canvas,
			Layouter layouter, AnimationFactory animationFactory) {
		this.files = files;
		this.creator = creator;
		this.presentation = presentation;
		this.canvas = canvas;
		this.layouter = layouter;
		this.animationFactory = animationFactory;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
        setProgress(0);
		int numberOfFiles= files.length;

		List<Shape> shapes = presentation.getShapes();
		Iterator<Shape> shapesIter = shapes.iterator();
		while (shapesIter.hasNext()) {
			shapesIter.next().release(canvas.getContext());
		}
		
		presentation.removeAllShapes();
		presentation.removeAllAnimations();
		
		Focusable animationStart = presentation;

		GLContext context = canvas.getContext();
		for(int i = 0; i < numberOfFiles; i++) {
			Shape newShape = creator.createShape(files[i]);
			if (newShape != null) {
				newShape.initialize(context);
				presentation.addShape(newShape);
				
				Animation animation = animationFactory.createAnimation(animationStart, newShape);
				presentation.addAnimation(animation);
				animationStart = newShape;
				
				layouter.layout(presentation);
				SessionFiveApplication.getInstance().getAnimationController().resetTo(-1);
			}
            setProgress(Math.min(i * 100 / numberOfFiles, 100));
		}
		
		setProgress(100);
		return null;
	}

}
