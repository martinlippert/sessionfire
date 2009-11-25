package com.sessionfive.app;

import java.awt.event.KeyListener;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class KeyListenerExtensionReader {
	
	public void addKeyListenerExtensionsTo(MultiplexingKeyListener multiListener) {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint("com.sessionfive.app.keylistener");
		IExtension[] extensions = extensionPoint.getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] configurationElements = extension.getConfigurationElements();
			for (IConfigurationElement iConfigurationElement : configurationElements) {
				if (iConfigurationElement.getName().equals("keylistener")) {
					try {
						KeyListener listener = (KeyListener) iConfigurationElement
								.createExecutableExtension("class");
						multiListener.addKeyListener(listener);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
