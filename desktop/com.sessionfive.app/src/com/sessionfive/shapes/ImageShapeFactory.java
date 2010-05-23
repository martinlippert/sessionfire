package com.sessionfive.shapes;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFactory;
import com.sessionfive.core.ShapeSize;

public class ImageShapeFactory implements ShapeFactory {

	public Shape createShape(File resource) {
		if (resource != null) {
			try {
				byte[] bytes = FileUtils.readFileToByteArray(resource);
				ImageShape imageShape = new ImageShape(bytes);
				imageShape.setSize(new ShapeSize(45f, 0f, 0f));
				return imageShape;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean matches(File resource) {
		String name = resource.getName().toLowerCase();
		return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
	}

}
