package com.sessionfive.animation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.app.Display;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Presentation;
import com.sessionfive.shapes.ImageShape;

public class AnimationController {

	private Animator currentAnimator;
	private int currentAnimationNo;

	private Display display;
	private Presentation presentation;

	public void init(Presentation presentation, Display display) {
		this.presentation = presentation;
		this.display = display;
		this.currentAnimationNo = -1;
	}

	public void forward() {
		if (currentAnimationNo >= presentation.getAnimationCount() - 1) {
			return;
		}

		currentAnimationNo++;
		Animation animation = presentation.getAnimation(currentAnimationNo);
		Animator animator = animation.getForwardAnimation(display);
		
		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		currentAnimator.start();
	}

	public void backward() {
		if (currentAnimationNo <= 0) return;

		Animation animation = presentation.getAnimation(currentAnimationNo);
		Animator animator = animation.getBackwardAnimation(display);
		currentAnimationNo--;
		
		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		currentAnimator.start();
	}
	
	public InputStream nextImage() throws IOException {
		return image(currentAnimationNo+1);
	}
	public InputStream prevImage() throws IOException {
		return image(currentAnimationNo-1);
	}
	
	public InputStream image(int imageNo) throws IOException {
		if(imageNo >= 0 && imageNo < presentation.getShapes().size()){
			ImageShape img = (ImageShape) presentation.getShapes().get(imageNo);
			File file = img.getFile();
			FileInputStream fis = new FileInputStream(file);
			return fis;		
		}
		return null;
	}

	public int getNumberOfKeyFrames() {
		return presentation.getAnimationCount();
	}

	public void goTo(int parsedNumber) {
		if (parsedNumber < 0 || parsedNumber >= presentation.getAnimationCount() || parsedNumber == currentAnimationNo) {
			return;
		}

		currentAnimationNo = parsedNumber;
		Animation animation = presentation.getAnimation(currentAnimationNo);
		Animator animator = animation.getForwardAnimation(display);
		
		if (currentAnimator != null && currentAnimator.isRunning()) {
			currentAnimator.stop();
		}

		currentAnimator = animator;
		currentAnimator.start();
	}

}
