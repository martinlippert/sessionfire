package com.sessionfive.app;

import java.io.File;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFileChooser;

import com.sessionfive.animation.MoveToAnimation;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.shapes.ImageShape;
import com.sessionfive.shapes.TextShape;

public class PresentationSelector {
	
	private final Presentation presentation;

	public PresentationSelector(Presentation presentation) {
		this.presentation = presentation;
	}

	public void selectPresentation(GLCanvas canvas) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int showDialog = chooser.showDialog(null, "Choose Presentation");
		if (showDialog == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (selectedFile.isDirectory()) {
				File[] files = selectedFile.listFiles();
				float x = -40f;
				float z = 0f;
				
				float rot = 0f;
				
				Shape startShape = null;
				for (File file : files) {
					Shape newShape = new ImageShape(file, x, -20f, z, rot, 45f);
					presentation.addShape(newShape);
//					presentation.addAnimation(new ZoomOutZoomInAnimation(startShape, newShape));
					presentation.addAnimation(new MoveToAnimation(startShape, newShape));
					startShape = newShape;
					x += 50f;
					z += 0.01f;
					
					TextShape textShape = new TextShape("Shape " + file.getName(), "SansSerif", 60, x, -20f, z, -rot);
					presentation.addShape(textShape);
//					presentation.addAnimation(new ZoomOutZoomInAnimation(startShape, textShape));
					presentation.addAnimation(new MoveToAnimation(startShape, textShape));
					startShape = textShape;
					x += 50f;
					z += 0.01f;

					rot += 5f;
				}
			}
			canvas.requestFocus();
		}
	}

}
