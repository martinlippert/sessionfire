package com.sessionfive.core.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.media.opengl.GLCanvas;
import javax.swing.JFileChooser;
import javax.swing.ProgressMonitor;

import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class PresentationLoader implements PropertyChangeListener {

	private PresentationLoaderTask task;
	private ProgressMonitor progressMonitor;

	public PresentationLoader() {
	}

	public void loadPresentation(Presentation presentation, GLCanvas canvas,
			Layouter layouter, AnimationFactory animationFactory) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);

		int showDialog = chooser.showDialog(null, "Choose Presentation");

		if (showDialog == JFileChooser.APPROVE_OPTION) {
			File[] files = getFiles(chooser);

			files = sortImageFiles(files);

			if (files.length > 0) {
				presentation.setPath(getPresentationPath(chooser));
				readFiles(presentation, files, canvas, layouter, animationFactory);
			}
		}
	}

	private File[] sortImageFiles(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File o1, File o2) {
				Long no1 = extractNumber(o1.getName());
				Long no2 = extractNumber(o2.getName());

				if (no1 != null && no2 != null) {
					return (int) (no1.longValue() - no2.longValue());
				}
				return (int) (o1.lastModified() - o2.lastModified());
			}
		});
		return files;
	}

	private File[] getFiles(JFileChooser chooser) {
		File[] selectedFiles = chooser.getSelectedFiles();
		if (selectedFiles != null) {
			if (selectedFiles.length == 1 && selectedFiles[0].isDirectory()) {
				return selectedFiles[0].listFiles();
			} else {
				return selectedFiles;
			}
		} else {
			return new File[0];
		}
	}
	
	private String getPresentationPath(JFileChooser chooser) {
		File[] selectedFiles = chooser.getSelectedFiles();
		if (selectedFiles != null) {
			if (selectedFiles.length == 1 && selectedFiles[0].isDirectory()) {
				return selectedFiles[0].getAbsolutePath();
			} else {
				return chooser.getCurrentDirectory().getAbsolutePath();
			}
		} else {
			return "";
		}
	}

	private Long extractNumber(String name) {
		StringBuilder digits = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			if (Character.isDigit(name.charAt(i))) {
				digits.append(name.charAt(i));
			}
		}

		if (digits.length() > 0) {
			try {
				long parsed = Long.parseLong(digits.toString());
				return parsed;
			}
			catch (NumberFormatException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	private void readFiles(Presentation presentation, File[] files, GLCanvas canvas,
			Layouter layouter, AnimationFactory animationFactory) {
		ShapeExtensionCreator creator = new ShapeExtensionCreator();

		progressMonitor = new ProgressMonitor(null, "Loading Presentation", "",
				0, 100);
		progressMonitor.setProgress(0);

		task = new PresentationLoaderTask(files, creator, presentation, canvas, layouter, animationFactory);
		task.addPropertyChangeListener(this);
		task.execute();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressMonitor.setProgress(progress);
			String message = String.format("Completed %d%%.\n", progress);
			progressMonitor.setNote(message);
			if (progressMonitor.isCanceled() || task.isDone()) {
				if (progressMonitor.isCanceled()) {
					task.cancel(true);
				}
			}
		}
	}

	public void savePresentation(Presentation presentation, String layoutName, String animationName) {
		List<Shape> shapes = presentation.getAllShapes();
		if (shapes.size() > 0) {
			File dir = new File(presentation.getPath());
			if (dir.exists()) {
				File presentationFile = new File(dir, "sessionfire.settings");
				try {
					Properties presentationSettings = createSettingsMap(presentation);
					FileOutputStream fos = new FileOutputStream(presentationFile);
					presentationSettings.store(fos, "sessionfire-settings");
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Properties createSettingsMap(Presentation presentation) {
		Properties result = new Properties();
		
		result.put("layout", presentation.getDefaultLayouter().getName());
		result.put("animation", presentation.getDefaultAnimation().getName());
		result.put("backgroundColor", Integer.toString(presentation.getBackgroundColor().getRGB()));
		result.put("rotationX", Float.toString(presentation.getShapes(LayerType.CAMERA_ANIMATED).get(0).getRotationAngleX()));
		result.put("rotationY", Float.toString(presentation.getShapes(LayerType.CAMERA_ANIMATED).get(0).getRotationAngleY()));
		result.put("rotationZ", Float.toString(presentation.getShapes(LayerType.CAMERA_ANIMATED).get(0).getRotationAngleZ()));
		result.put("layerText", presentation.getLayerText());
		
		return result;
	}

}
