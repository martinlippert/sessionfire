package com.sessionfive.core.ui;

import javax.swing.JPanel;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.SessionfivePanel;
import com.sessionfive.core.Presentation;

public class PanelExtensionLoader {

	public JPanel laodExtension(AnimationController animationController, Presentation presentation) {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint("com.sessionfive.app.panel");
		IExtension[] extensions = extensionPoint.getExtensions();
		if (extensions.length == 0) {
			return null;
		}
		IExtension firstExtensions = extensions[0];
		IConfigurationElement[] configurationElements = firstExtensions.getConfigurationElements();
		try {
			Object createExecutableExtension = configurationElements[0]
					.createExecutableExtension("class");
			SessionfivePanel panel = (SessionfivePanel) createExecutableExtension;
			//panel.setAnimationController(animationController);
			//panel.setPresentation(presentation);
			return (JPanel) panel;

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

}
