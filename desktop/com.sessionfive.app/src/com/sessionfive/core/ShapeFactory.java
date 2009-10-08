package com.sessionfive.core;

import java.io.File;

public interface ShapeFactory {
	
	public Shape createShape(File resource) throws Exception;
	public boolean matches(File resource);

}
