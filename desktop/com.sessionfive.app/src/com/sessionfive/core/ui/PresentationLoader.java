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
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import com.sessionfive.animation.Point;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;

public class PresentationLoader implements PropertyChangeListener {

	private PresentationLoaderTask task;
	private ProgressMonitor progressMonitor;

	public PresentationLoader() {
	}

	public void loadPresentation(Presentation presentation, GLCanvas canvas, Layouter[] layouter,
			AnimationStyle[] animationStyles) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		chooser.setDialogTitle("Open Presentation");

		int showDialog = chooser.showDialog(canvas, "Choose Presentation");

		if (showDialog == JFileChooser.APPROVE_OPTION) {
			File[] files = getFiles(chooser);

			HierarchicFileStructureNode fileStructure = new HierarchicFileStructureNode(null);
			addFilesToStructure(files, fileStructure);

			if (fileStructure.getElementCount() > 0) {
				presentation.setPath(getPresentationPath(chooser));
				readFiles(presentation, fileStructure, canvas, layouter, animationStyles);
			}
		}
	}

	private void addFilesToStructure(File[] files,
			HierarchicFileStructureNode fileStructure) {
		
		files = sortImageFiles(files);
		fileStructure.setChilds(files);
		
		for(int i = 0; i < fileStructure.getChildCount(); i++) {
			HierarchicFileStructureNode child = fileStructure.getChild(i);
			if (child.getFile().exists() && child.getFile().isDirectory()) {
				File[] newChilds = child.getFile().listFiles();
				addFilesToStructure(newChilds, child);
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
			} catch (NumberFormatException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	private void readFiles(Presentation presentation, HierarchicFileStructureNode fileStructure, GLCanvas canvas,
			Layouter[] layouter, AnimationStyle[] animationStyles) {
		ShapeExtensionCreator creator = new ShapeExtensionCreatorImpl();

		progressMonitor = new ProgressMonitor(canvas, "Loading Presentation", "", 0, 100);
		progressMonitor.setProgress(0);
		
		task = new PresentationLoaderTask(fileStructure, creator, presentation, canvas, layouter,
				animationStyles);
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

	public void savePresentation(final Presentation presentation, final GLCanvas canvas) {
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

					JOptionPane.showMessageDialog(canvas,
							"Presentation settings saved successfully to:\n"
									+ presentationFile.getAbsolutePath(), "Presentation saved...",
							JOptionPane.INFORMATION_MESSAGE);

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

		result.setProperty("layout", presentation.getDefaultLayouter().getName());
		result.setProperty("animation", presentation.getDefaultAnimation().getName());
		result.setProperty("backgroundColor", Integer.toString(presentation.getBackgroundColor()
				.getRGB()));
		
		result.setProperty("layerText", presentation.getLayerText());
		result.setProperty("spaceBetween", Float.toString(presentation.getSpace()));
		result.setProperty("reflection", Boolean
				.toString(presentation.isDefaultReflectionEnabled()));
		result.setProperty("focusscale", Float.toString(presentation.getDefaultFocusScale()));

		Camera startCamera = presentation.getStartCamera();
		Point loc = startCamera.getLocation();
		Point tar = startCamera.getTarget();
		result.setProperty("startCameraLocationX", Float.toString(loc.getX()));
		result.setProperty("startCameraLocationY", Float.toString(loc.getY()));
		result.setProperty("startCameraLocationZ", Float.toString(loc.getZ()));
		result.setProperty("startCameraTargetX", Float.toString(tar.getX()));
		result.setProperty("startCameraTargetY", Float.toString(tar.getY()));
		result.setProperty("startCameraTargetZ", Float.toString(tar.getZ()));

		Shape firstShape = presentation.getShapes(LayerType.CAMERA_ANIMATED).get(0);
		result.setProperty("rotationX", Float.toString(firstShape.getRotationAngleX()));
		result.setProperty("rotationY", Float.toString(firstShape.getRotationAngleY()));
		result.setProperty("rotationZ", Float.toString(firstShape.getRotationAngleZ()));

		return result;
	}

}
