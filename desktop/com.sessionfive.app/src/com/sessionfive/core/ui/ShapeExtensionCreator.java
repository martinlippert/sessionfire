package com.sessionfive.core.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeFactory;

public class ShapeExtensionCreator {
	
	private List<ShapeFactory> factories;

	public ShapeExtensionCreator() {
		factories = new ArrayList<ShapeFactory>();
		
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtensionPoint point = registry.getExtensionPoint("com.sessionfive.app", "shapes");
		IExtension[] extensions = point.getExtensions();
		for (IExtension iExtension : extensions) {
			IConfigurationElement[] elements = iExtension.getConfigurationElements();
			for (IConfigurationElement element : elements) {
				if (element.getName().equals("shape")) {
					try {
						ShapeFactory shapeFactory = (ShapeFactory) element.createExecutableExtension("factoryclass");
						factories.add(shapeFactory);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public Shape createShape(File file) {
		Shape result = null;
		Iterator<ShapeFactory> factoryIter = factories.iterator();
		while (result == null && factoryIter.hasNext()) {
			ShapeFactory factory = factoryIter.next();
			if (factory.matches(file)) {
				try {
					result = factory.createShape(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

}
