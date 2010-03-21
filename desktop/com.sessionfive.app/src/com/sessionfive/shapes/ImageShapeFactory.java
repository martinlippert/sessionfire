package com.sessionfive.shapes;

import java.io.File;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFactory;
import com.sessionfive.core.ShapeSize;

public class ImageShapeFactory implements ShapeFactory {

	public Shape createShape(File resource) {
		ImageShape imageShape = new ImageShape(resource);
		imageShape.setSize(new ShapeSize(45f, 0f, 0f));
		return imageShape;
	}

	public boolean matches(File resource) {
		String name = resource.getName().toLowerCase();
		return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
	}

}
