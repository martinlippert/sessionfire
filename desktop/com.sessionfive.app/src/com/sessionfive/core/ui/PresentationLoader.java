package com.sessionfive.core.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class PresentationLoader {

	private Set<String> imageTypes;
	
	public PresentationLoader() {
		imageTypes = new HashSet<String>();
		imageTypes.add("jpg");
		imageTypes.add("jpeg");
		imageTypes.add("png");
		imageTypes.add("gif");
	}

	public void loadPresentation(Presentation presentation) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		
		int showDialog = chooser.showDialog(null, "Choose Presentation");

		if (showDialog == JFileChooser.APPROVE_OPTION) {
			File[] files = getFiles(chooser);
			
			files = filerImageFiles(files);
			files = sortImageFiles(files);
				
			if (files.length > 0) {
				presentation.removeAllShapes();
				readFiles(presentation, files);
			}
		}
	}
	
	private File[] sortImageFiles(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File o1, File o2) {
				Integer no1 = extractNumber(o1.getName());
				Integer no2 = extractNumber(o2.getName());

				if (no1 != null && no2 != null) {
					return no1.intValue() - no2.intValue();
				}
				return (int) (o1.lastModified() - o2.lastModified());
			}
		});
		return files;
	}

	private File[] filerImageFiles(File[] files) {
		List<File> result = new ArrayList<File>();
		
		for (File file : files) {
			String fileName = file.getName();
			int lastIndexOf = fileName.lastIndexOf('.');
			if (lastIndexOf != -1 && lastIndexOf < fileName.length()) {
				String suffix = fileName.substring(lastIndexOf + 1).toLowerCase();
				if (imageTypes.contains(suffix)) result.add(file);
			}
		}

		return result.toArray(new File[result.size()]);
	}

	private File[] getFiles(JFileChooser chooser) {
		File[] selectedFiles = chooser.getSelectedFiles();
		if (selectedFiles != null) {
			if (selectedFiles.length == 1 && selectedFiles[0].isDirectory()) {
				return selectedFiles[0].listFiles();
			}
			else {
				return selectedFiles;
			}
		}
		else {
			return new File[0];
		}
	}

	private void readFiles(Presentation presentation, File[] files) {
		ShapeExtensionCreator creator = new ShapeExtensionCreator();
		for (File file : files) {
			Shape newShape = creator.createShape(file);
			if (newShape != null) presentation.addShape(newShape);
		}
	}

	private Integer extractNumber(String name) {
		StringBuilder digits = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			if (Character.isDigit(name.charAt(i))) {
				digits.append(name.charAt(i));
			}
		}
		if (digits.length() > 0) {
			return Integer.parseInt(digits.toString());
		}
		else {
			return null;
		}
	}

}
