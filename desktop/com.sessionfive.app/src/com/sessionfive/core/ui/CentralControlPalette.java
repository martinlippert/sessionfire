package com.sessionfive.core.ui;

import java.awt.Component;
import java.io.File;
import java.security.acl.LastOwnerException;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFileChooser;

import com.sessionfive.animation.ZoomOutZoomInAnimation;
import com.sessionfive.core.Focusable;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.shapes.ImageShape;
import com.sessionfive.shapes.TextShape;

public class CentralControlPalette {
	
	private final Presentation presentation;

	public CentralControlPalette(Presentation presentation) {
		this.presentation = presentation;
	}
	
	public void show() {
	}

	public void choosePresentation(Component canvas) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int showDialog = chooser.showDialog(null, "Choose Presentation");
		if (showDialog == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (selectedFile.isDirectory()) {
				File[] files = selectedFile.listFiles();
				
				Arrays.sort(files, new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						return (int) (o1.lastModified() - o2.lastModified());
					}
				});
				
				float x = -40f;
				float z = 0f;
				
				float rot = 0f;
				
				Focusable startShape = presentation;
				for (File file : files) {
					Shape newShape = new ImageShape(file, x, -20f, z, rot, 45f);
					presentation.addShape(newShape);
					presentation.addAnimation(new ZoomOutZoomInAnimation(startShape, newShape));
//					presentation.addAnimation(new MoveToAnimation(startShape, newShape));
					startShape = newShape;
					x += 50f;
					z += 0.01f;
					
					TextShape textShape = new TextShape("Shape " + file.getName(), "SansSerif", 60, x, -20f, z, -rot);
					presentation.addShape(textShape);
					presentation.addAnimation(new ZoomOutZoomInAnimation(startShape, textShape));
//					presentation.addAnimation(new MoveToAnimation(startShape, textShape));
					startShape = textShape;
					x += 50f;
					z += 0.01f;

					rot += 5f;
				}
			}
		}
		canvas.requestFocus();
	}


}
