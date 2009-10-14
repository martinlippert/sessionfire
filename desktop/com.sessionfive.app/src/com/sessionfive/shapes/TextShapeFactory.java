package com.sessionfive.shapes;

import java.io.File;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFactory;

public class TextShapeFactory implements ShapeFactory {

	public Shape createShape(File resource) {
		return new TextShape("Text of a Shape");
	}

	public boolean matches(File resource) {
		return resource.getName().toLowerCase().endsWith(".txt");
	}

}
