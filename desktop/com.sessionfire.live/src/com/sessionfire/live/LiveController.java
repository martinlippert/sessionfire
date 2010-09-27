package com.sessionfire.live;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.PresentationChangedListener;

public class LiveController {
	
	private Live live;
	private Presentation presentation;

	public LiveController() {
		live = new Live();
		
		AnimationController animationController = SessionFiveApplication.getInstance().getAnimationController();
		new LiveSync(animationController, live);
		
		presentation = SessionFiveApplication.getInstance().getPresentation();
	}

	public void upload() {
		new LiveServer().upload(presentation);
	}

	public void setLiveSync(boolean selected) {
		live.setEnabled(selected);
	}

	public void setName(String name) {
		presentation.setName(name);
	}

	public String getName() {
		return presentation.getName();
	}

	public void addPresentationChangedListener(
			PresentationChangedListener presentationChangedListener) {
		presentation.addPresentationChangedListener(presentationChangedListener);
	}

}
