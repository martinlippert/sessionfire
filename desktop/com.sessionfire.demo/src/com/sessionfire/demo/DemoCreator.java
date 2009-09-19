package com.sessionfire.demo;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;

import com.sessionfive.animation.MoveToAnimation;
import com.sessionfive.core.Presentation;
import com.sessionfive.shapes.ImageShape;

public class DemoCreator {

	public void createDemo(Presentation presentation) throws Exception {
		ImageShape titleShape = new ImageShape(getImage("title2.png"), 0, 0, 0, 0, 0, 0, 45);
		presentation.addShape(titleShape);
		presentation.addAnimation(new MoveToAnimation(presentation, titleShape));
	}
	
	protected File getImage(String image) throws Exception {
		URL imageResource = this.getClass().getResource(image);
		URI imageURI = FileLocator.toFileURL(imageResource).toURI();
		return new File(imageURI);
	}

}
