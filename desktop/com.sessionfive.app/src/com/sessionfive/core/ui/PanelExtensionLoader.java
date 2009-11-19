package com.sessionfive.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.Presentation;

public class PanelExtensionLoader {

	public PanelExtension[] loadExtensions(AnimationController animationController, Presentation presentation) {
		List<PanelExtension> result = new ArrayList<PanelExtension>();
		
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint("com.sessionfive.app.panel");
		IExtension[] extensions = extensionPoint.getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] configurationElements = extension.getConfigurationElements();
			for (IConfigurationElement iConfigurationElement : configurationElements) {
				if (iConfigurationElement.getName().equals("panel")) {
					try {
						JPanel panel = (JPanel) iConfigurationElement
								.createExecutableExtension("class");
						String name = iConfigurationElement.getAttribute("name");
						result.add(new PanelExtension(name, panel));
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result.toArray(new PanelExtension[result.size()]);
	}

}
