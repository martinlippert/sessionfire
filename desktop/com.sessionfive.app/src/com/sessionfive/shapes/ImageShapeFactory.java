package com.sessionfive.shapes;

import java.io.File;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFactory;

public class ImageShapeFactory implements ShapeFactory {

	public Shape createShape(File resource) {
		ImageShape imageShape = new ImageShape(resource);
		imageShape.setWidth(45f);
		return imageShape;
	}

	public boolean matches(File resource) {
		String name = resource.getName().toLowerCase();
		return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
	}

}
