package com.sessionfive.core.ui;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFileChooser;

import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.shapes.ImageShape;
import com.sessionfive.shapes.TextShape;

public class PresentationLoader {

	public void loadPresentation(Presentation presentation) {
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
				
//				float x = -40f;
//				float z = 0f;
//				
//				float rot = 0f;
//				
//				Focusable startShape = presentation;
				for (File file : files) {
					Shape newShape = new ImageShape(file, 0, 0, 0, 0, 45f);
					presentation.addShape(newShape);
//					presentation.addAnimation(new ZoomOutZoomInAnimation(startShape, newShape));
//					presentation.addAnimation(new MoveToAnimation(startShape, newShape));
//					startShape = newShape;
//					x += 50f;
//					z += 0.01f;
					
					TextShape textShape = new TextShape("Shape " + file.getName(), "SansSerif", 60, 0, 0, 0, 0);
					presentation.addShape(textShape);
//					presentation.addAnimation(new ZoomOutZoomInAnimation(startShape, textShape));
//					presentation.addAnimation(new MoveToAnimation(startShape, textShape));
//					startShape = textShape;
//					x += 50f;
//					z += 0.01f;

//					rot += 5f;
				}
			}
		}
	}

}
